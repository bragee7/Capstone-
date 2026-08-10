package com.campushire.service;

import com.campushire.dto.drive.DriveRequest;
import com.campushire.exception.BadRequestException;
import com.campushire.model.entity.Company;
import com.campushire.model.entity.HiringDrive;
import com.campushire.model.enums.DriveStatus;
import com.campushire.model.enums.JobType;
import com.campushire.repository.ApplicationRepository;
import com.campushire.repository.CompanyRepository;
import com.campushire.repository.HiringDriveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DriveServiceTest {

    @Mock
    private HiringDriveRepository driveRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private ApplicationRepository applicationRepository;

    private DriveService driveService;
    private Company company;

    @BeforeEach
    void setUp() {
        driveService = new DriveService(driveRepository, companyRepository, applicationRepository);
        company = new Company();
        company.setId(10L);
        company.setCompanyName("TestCorp");
    }

    private DriveRequest validRequest() {
        return new DriveRequest(
                "Summer Internship",
                "Description",
                JobType.INTERNSHIP,
                "Pune",
                20000.0,
                "3 LPA",
                6.5,
                "Computer Science",
                "Java",
                LocalDateTime.now().plusDays(30),
                null);
    }

    private HiringDrive driveWithStatus(DriveStatus status) {
        HiringDrive drive = new HiringDrive();
        drive.setId(1L);
        drive.setCompany(company);
        drive.setTitle("Summer Internship");
        drive.setJobType(JobType.INTERNSHIP);
        drive.setLocation("Pune");
        drive.setApplicationDeadline(LocalDateTime.now().plusDays(30));
        drive.setStatus(status);
        return drive;
    }

    @Test
    void create_returnsDriveInDraftStatus() {
        when(companyRepository.findByUserId(5L)).thenReturn(Optional.of(company));
        when(driveRepository.save(any(HiringDrive.class))).thenAnswer(inv -> inv.getArgument(0));
        when(applicationRepository.countByDriveId(any())).thenReturn(0L);

        var response = driveService.create(5L, validRequest());

        assertEquals(DriveStatus.DRAFT, response.status());
        assertEquals("Summer Internship", response.title());
        verify(driveRepository).save(any(HiringDrive.class));
    }

    @Test
    void publish_rejectsWhenDeadlinePassed() {
        HiringDrive drive = driveWithStatus(DriveStatus.DRAFT);
        drive.setApplicationDeadline(LocalDateTime.now().minusDays(1));
        when(companyRepository.findByUserId(5L)).thenReturn(Optional.of(company));
        when(driveRepository.findByIdAndCompanyId(1L, 10L)).thenReturn(Optional.of(drive));

        assertThrows(BadRequestException.class, () -> driveService.publish(5L, 1L));
    }

    @Test
    void publish_rejectsWhenJobTypeAndLocationMissing() {
        HiringDrive drive = driveWithStatus(DriveStatus.DRAFT);
        drive.setJobType(null);
        drive.setLocation(null);
        when(companyRepository.findByUserId(5L)).thenReturn(Optional.of(company));
        when(driveRepository.findByIdAndCompanyId(1L, 10L)).thenReturn(Optional.of(drive));

        assertThrows(BadRequestException.class, () -> driveService.publish(5L, 1L));
    }

    @Test
    void publish_succeedsForCompleteDrive() {
        HiringDrive drive = driveWithStatus(DriveStatus.DRAFT);
        when(companyRepository.findByUserId(5L)).thenReturn(Optional.of(company));
        when(driveRepository.findByIdAndCompanyId(1L, 10L)).thenReturn(Optional.of(drive));
        when(driveRepository.save(any(HiringDrive.class))).thenAnswer(inv -> inv.getArgument(0));
        when(applicationRepository.countByDriveId(any())).thenReturn(0L);

        var response = driveService.publish(5L, 1L);

        assertEquals(DriveStatus.PUBLISHED, response.status());
    }

    @Test
    void close_marksDriveClosed() {
        HiringDrive drive = driveWithStatus(DriveStatus.PUBLISHED);
        when(companyRepository.findByUserId(5L)).thenReturn(Optional.of(company));
        when(driveRepository.findByIdAndCompanyId(1L, 10L)).thenReturn(Optional.of(drive));
        when(driveRepository.save(any(HiringDrive.class))).thenAnswer(inv -> inv.getArgument(0));
        when(applicationRepository.countByDriveId(any())).thenReturn(1L);

        var response = driveService.close(5L, 1L);

        assertEquals(DriveStatus.CLOSED, response.status());
    }
}
