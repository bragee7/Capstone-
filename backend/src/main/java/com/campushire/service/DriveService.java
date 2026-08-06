package com.campushire.service;

import com.campushire.dto.drive.DriveRequest;
import com.campushire.dto.drive.DriveResponse;
import com.campushire.exception.BadRequestException;
import com.campushire.exception.ForbiddenException;
import com.campushire.exception.ResourceNotFoundException;
import com.campushire.model.entity.Company;
import com.campushire.model.entity.HiringDrive;
import com.campushire.model.enums.DriveStatus;
import com.campushire.repository.ApplicationRepository;
import com.campushire.repository.CompanyRepository;
import com.campushire.repository.HiringDriveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriveService {

    private final HiringDriveRepository driveRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    public List<DriveResponse> listPublished() {
        return driveRepository.findByStatusOrderByCreatedAtDesc(DriveStatus.PUBLISHED).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DriveResponse getPublishedById(Long driveId) {
        HiringDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Hiring drive not found."));
        if (drive.getStatus() != DriveStatus.PUBLISHED) {
            throw new ResourceNotFoundException("Hiring drive not found.");
        }
        return toResponse(drive);
    }

    @Transactional(readOnly = true)
    public List<DriveResponse> myDrives(Long userId) {
        Company company = findByCompanyUserId(userId);
        return driveRepository.findByCompanyIdOrderByCreatedAtDesc(company.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public DriveResponse create(Long userId, DriveRequest request) {
        Company company = findByCompanyUserId(userId);
        HiringDrive drive = HiringDrive.builder()
                .company(company)
                .title(request.title().trim())
                .description(request.description())
                .jobType(request.jobType())
                .location(request.location())
                .stipend(request.stipend())
                .salaryPackage(request.salaryPackage())
                .minimumCgpa(request.minimumCgpa())
                .eligibleDepartments(request.eligibleDepartments())
                .requiredSkills(request.requiredSkills())
                .applicationDeadline(request.applicationDeadline())
                .driveDate(request.driveDate())
                .status(DriveStatus.DRAFT)
                .build();
        return toResponse(driveRepository.save(drive));
    }

    @Transactional
    public DriveResponse update(Long userId, Long driveId, DriveRequest request) {
        HiringDrive drive = findOwnedDrive(userId, driveId);
        if (drive.getStatus() == DriveStatus.CLOSED) {
            throw new BadRequestException("A closed hiring drive cannot be edited.");
        }

        drive.setTitle(request.title().trim());
        drive.setDescription(request.description());
        drive.setJobType(request.jobType());
        drive.setLocation(request.location());
        drive.setStipend(request.stipend());
        drive.setSalaryPackage(request.salaryPackage());
        drive.setMinimumCgpa(request.minimumCgpa());
        drive.setEligibleDepartments(request.eligibleDepartments());
        drive.setRequiredSkills(request.requiredSkills());
        drive.setApplicationDeadline(request.applicationDeadline());
        drive.setDriveDate(request.driveDate());
        return toResponse(driveRepository.save(drive));
    }

    @Transactional
    public DriveResponse publish(Long userId, Long driveId) {
        HiringDrive drive = findOwnedDrive(userId, driveId);
        if (drive.getStatus() == DriveStatus.PUBLISHED) {
            throw new BadRequestException("The hiring drive is already published.");
        }
        if (drive.getStatus() == DriveStatus.CLOSED) {
            throw new BadRequestException("A closed hiring drive cannot be published.");
        }
        if (drive.getApplicationDeadline().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Cannot publish a drive whose application deadline has already passed.");
        }
        if (drive.getJobType() == null || drive.getLocation() == null || drive.getLocation().isBlank()) {
            throw new BadRequestException("Set a job type and location before publishing the drive.");
        }
        drive.setStatus(DriveStatus.PUBLISHED);
        return toResponse(driveRepository.save(drive));
    }

    @Transactional
    public DriveResponse close(Long userId, Long driveId) {
        HiringDrive drive = findOwnedDrive(userId, driveId);
        if (drive.getStatus() == DriveStatus.CLOSED) {
            throw new BadRequestException("The hiring drive is already closed.");
        }
        drive.setStatus(DriveStatus.CLOSED);
        return toResponse(driveRepository.save(drive));
    }

    private HiringDrive findOwnedDrive(Long userId, Long driveId) {
        Company company = findByCompanyUserId(userId);
        return driveRepository.findByIdAndCompanyId(driveId, company.getId())
                .orElseThrow(() -> new ForbiddenException("You do not own this hiring drive."));
    }

    private Company findByCompanyUserId(Long userId) {
        return companyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Company profile not found for the current user."));
    }

    private DriveResponse toResponse(HiringDrive drive) {
        Company company = drive.getCompany();
        return new DriveResponse(
                drive.getId(),
                company.getId(),
                company.getCompanyName(),
                drive.getTitle(),
                drive.getDescription(),
                drive.getJobType(),
                drive.getLocation(),
                drive.getStipend(),
                drive.getSalaryPackage(),
                drive.getMinimumCgpa(),
                drive.getEligibleDepartments(),
                drive.getRequiredSkills(),
                drive.getApplicationDeadline(),
                drive.getDriveDate(),
                drive.getStatus(),
                applicationRepository.countByDriveId(drive.getId()),
                drive.getCreatedAt());
    }
}
