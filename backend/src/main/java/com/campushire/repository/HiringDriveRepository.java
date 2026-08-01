package com.campushire.repository;

import com.campushire.model.entity.HiringDrive;
import com.campushire.model.enums.DriveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HiringDriveRepository extends JpaRepository<HiringDrive, Long> {

    List<HiringDrive> findAllByOrderByCreatedAtDesc();

    List<HiringDrive> findByStatusOrderByCreatedAtDesc(DriveStatus status);

    List<HiringDrive> findByCompanyIdOrderByCreatedAtDesc(Long companyId);

    Optional<HiringDrive> findByIdAndCompanyId(Long id, Long companyId);

    boolean existsByIdAndCompanyId(Long id, Long companyId);
}
