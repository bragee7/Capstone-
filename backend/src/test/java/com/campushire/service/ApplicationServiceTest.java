package com.campushire.service;

import com.campushire.dto.application.ApplicationRequest;
import com.campushire.exception.BadRequestException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private HiringDriveRepository driveRepository;
    @Mock
    private CompanyRepository companyRepository;

    private ApplicationService applicationService;
    private Student student;
    private Company company;
    private HiringDrive publishedDrive;

    @BeforeEach
    void setUp() {
        applicationService = new ApplicationService(applicationRepository, studentRepository,
                driveRepository, companyRepository);

        User studentUser = new User();
        studentUser.setName("John");
        studentUser.setEmail("john@example.com");

        student = new Student();
        student.setId(1L);
        student.setUser(studentUser);
        student.setDepartment("Computer Science");
        student.setCgpa(BigDecimal.valueOf(8.0));

        company = new Company();
        company.setId(10L);
        company.setCompanyName("TestCorp");

        publishedDrive = new HiringDrive();
        publishedDrive.setId(1L);
        publishedDrive.setCompany(company);
        publishedDrive.setTitle("Intern");
        publishedDrive.setApplicationDeadline(LocalDateTime.now().plusDays(30));
        publishedDrive.setStatus(DriveStatus.PUBLISHED);
        publishedDrive.setMinimumCgpa(6.5);
        publishedDrive.setEligibleDepartments("Computer Science, Electronics");
    }

    @Test
    void apply_rejectsNonPublishedDrive() {
        publishedDrive.setStatus(DriveStatus.DRAFT);
        when(studentRepository.findByUserId(5L)).thenReturn(Optional.of(student));
        when(driveRepository.findById(1L)).thenReturn(Optional.of(publishedDrive));

        assertThrows(BadRequestException.class, () -> applicationService.apply(5L, new ApplicationRequest(1L, null, null)));
    }

    @Test
    void apply_rejectsWhenDeadlinePassed() {
        publishedDrive.setApplicationDeadline(LocalDateTime.now().minusDays(1));
        when(studentRepository.findByUserId(5L)).thenReturn(Optional.of(student));
        when(driveRepository.findById(1L)).thenReturn(Optional.of(publishedDrive));

        assertThrows(BadRequestException.class, () -> applicationService.apply(5L, new ApplicationRequest(1L, null, null)));
    }

    @Test
    void apply_rejectsDuplicateApplication() {
        when(studentRepository.findByUserId(5L)).thenReturn(Optional.of(student));
        when(driveRepository.findById(1L)).thenReturn(Optional.of(publishedDrive));
        when(applicationRepository.existsByStudentIdAndDriveId(1L, 1L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> applicationService.apply(5L, new ApplicationRequest(1L, null, null)));
    }

    @Test
    void apply_rejectsWhenCgpaBelowMinimum() {
        student.setCgpa(BigDecimal.valueOf(5.0));
        when(studentRepository.findByUserId(5L)).thenReturn(Optional.of(student));
        when(driveRepository.findById(1L)).thenReturn(Optional.of(publishedDrive));
        when(applicationRepository.existsByStudentIdAndDriveId(1L, 1L)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> applicationService.apply(5L, new ApplicationRequest(1L, null, null)));
    }

    @Test
    void apply_rejectsWhenDepartmentNotEligible() {
        student.setDepartment("Mechanical");
        when(studentRepository.findByUserId(5L)).thenReturn(Optional.of(student));
        when(driveRepository.findById(1L)).thenReturn(Optional.of(publishedDrive));
        when(applicationRepository.existsByStudentIdAndDriveId(1L, 1L)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> applicationService.apply(5L, new ApplicationRequest(1L, null, null)));
    }

    @Test
    void withdraw_rejectsNonAppliedStatus() {
        var application = new com.campushire.model.entity.Application();
        application.setId(1L);
        application.setStatus(ApplicationStatus.SHORTLISTED);
        when(studentRepository.findByUserId(5L)).thenReturn(Optional.of(student));
        when(applicationRepository.findByIdAndStudentId(1L, 1L)).thenReturn(Optional.of(application));

        assertThrows(BadRequestException.class, () -> applicationService.withdraw(5L, 1L));
    }
}
