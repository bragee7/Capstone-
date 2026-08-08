package com.campushire.service;

import com.campushire.dto.application.ApplicationRequest;
import com.campushire.dto.application.ApplicationResponse;
import com.campushire.dto.application.ApplicationStatusRequest;
import com.campushire.exception.BadRequestException;
import com.campushire.exception.ForbiddenException;
import com.campushire.exception.ResourceNotFoundException;
import com.campushire.model.entity.Application;
import com.campushire.model.entity.Company;
import com.campushire.model.entity.HiringDrive;
import com.campushire.model.entity.Student;
import com.campushire.model.entity.User;
import com.campushire.model.enums.ApplicationStatus;
import com.campushire.model.enums.DriveStatus;
import com.campushire.repository.ApplicationRepository;
import com.campushire.repository.CompanyRepository;
import com.campushire.repository.HiringDriveRepository;
import com.campushire.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final HiringDriveRepository driveRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public ApplicationResponse apply(Long userId, ApplicationRequest request) {
        Student student = findStudentByUserId(userId);
        HiringDrive drive = driveRepository.findById(request.driveId())
                .orElseThrow(() -> new ResourceNotFoundException("Hiring drive not found."));

        if (drive.getStatus() != DriveStatus.PUBLISHED) {
            throw new BadRequestException("This hiring drive is not open for applications.");
        }
        if (drive.getApplicationDeadline().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("The application deadline for this drive has passed.");
        }
        if (applicationRepository.existsByStudentIdAndDriveId(student.getId(), drive.getId())) {
            throw new BadRequestException("You have already applied to this drive.");
        }
        checkEligibility(student, drive);

        Application application = Application.builder()
                .student(student)
                .drive(drive)
                .coverMessage(request.coverMessage())
                .resumeUrl(request.resumeUrl())
                .status(ApplicationStatus.APPLIED)
                .appliedAt(LocalDateTime.now())
                .build();

        return toResponse(applicationRepository.save(application));
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> myApplications(Long userId) {
        Student student = findStudentByUserId(userId);
        return applicationRepository.findByStudentIdOrderByAppliedAtDesc(student.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ApplicationResponse getApplication(Long userId, Long applicationId) {
        Student student = findStudentByUserId(userId);
        Application application = applicationRepository.findByIdAndStudentId(applicationId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found."));
        return toResponse(application);
    }

    @Transactional
    public void withdraw(Long userId, Long applicationId) {
        Student student = findStudentByUserId(userId);
        Application application = applicationRepository.findByIdAndStudentId(applicationId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found."));
        if (application.getStatus() != ApplicationStatus.APPLIED) {
            throw new BadRequestException("Only applications with status APPLIED can be withdrawn.");
        }
        applicationRepository.delete(application);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> listForDrive(Long recruiterUserId, Long driveId) {
        Company company = findCompanyByUserId(recruiterUserId);
        HiringDrive drive = driveRepository.findByIdAndCompanyId(driveId, company.getId())
                .orElseThrow(() -> new ForbiddenException("You do not own this hiring drive."));
        return applicationRepository.findByDriveIdOrderByAppliedAtDesc(drive.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> listAllForRecruiter(Long recruiterUserId) {
        Company company = findCompanyByUserId(recruiterUserId);
        return applicationRepository.findByDriveCompanyIdOrderByAppliedAtDesc(company.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ApplicationResponse updateStatus(Long recruiterUserId, Long driveId, Long applicationId,
                                            ApplicationStatusRequest request) {
        Company company = findCompanyByUserId(recruiterUserId);
        HiringDrive drive = driveRepository.findByIdAndCompanyId(driveId, company.getId())
                .orElseThrow(() -> new ForbiddenException("You do not own this hiring drive."));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found."));
        if (!application.getDrive().getId().equals(drive.getId())) {
            throw new BadRequestException("The application does not belong to this drive.");
        }

        application.setStatus(request.status());
        return toResponse(applicationRepository.save(application));
    }

    private void checkEligibility(Student student, HiringDrive drive) {
        if (drive.getMinimumCgpa() != null && student.getCgpa() != null
                && student.getCgpa().doubleValue() < drive.getMinimumCgpa()) {
            throw new BadRequestException("Your CGPA does not meet the minimum requirement of " + drive.getMinimumCgpa() + ".");
        }
        if (isNotBlank(drive.getEligibleDepartments()) && isNotBlank(student.getDepartment())) {
            boolean eligible = Arrays.stream(drive.getEligibleDepartments().split(","))
                    .map(String::trim)
                    .anyMatch(d -> d.equalsIgnoreCase(student.getDepartment().trim()));
            if (!eligible) {
                throw new BadRequestException("Your department is not eligible for this drive.");
            }
        }
    }

    private Student findStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for the current user."));
    }

    private Company findCompanyByUserId(Long userId) {
        return companyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Company profile not found for the current user."));
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private ApplicationResponse toResponse(Application application) {
        HiringDrive drive = application.getDrive();
        Student student = application.getStudent();
        User studentUser = student.getUser();
        return new ApplicationResponse(
                application.getId(),
                application.getStatus(),
                application.getAppliedAt(),
                application.getCoverMessage(),
                application.getResumeUrl(),
                drive.getId(),
                drive.getTitle(),
                drive.getJobType(),
                drive.getCompany().getCompanyName(),
                student.getId(),
                studentUser.getName(),
                studentUser.getEmail(),
                student.getDepartment(),
                student.getCgpa(),
                student.getGraduationYear());
    }
}
