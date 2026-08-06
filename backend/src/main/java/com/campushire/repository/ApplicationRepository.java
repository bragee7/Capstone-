package com.campushire.repository;

import com.campushire.model.entity.Application;
import com.campushire.model.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByStudentIdOrderByAppliedAtDesc(Long studentId);

    Optional<Application> findByIdAndStudentId(Long id, Long studentId);

    List<Application> findByDriveIdOrderByAppliedAtDesc(Long driveId);

    List<Application> findByDriveCompanyIdOrderByAppliedAtDesc(Long companyId);

    List<Application> findByDriveIdAndStatus(Long driveId, ApplicationStatus status);

    boolean existsByStudentIdAndDriveId(Long studentId, Long driveId);

    long countByStudentId(Long studentId);

    long countByDriveId(Long driveId);
}
