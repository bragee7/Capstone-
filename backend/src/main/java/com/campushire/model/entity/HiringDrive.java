package com.campushire.model.entity;

import com.campushire.model.enums.DriveStatus;
import com.campushire.model.enums.JobType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hiring_drives")
public class HiringDrive extends BaseEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", length = 20)
    private JobType jobType;

    @Column(length = 160)
    private String location;

    private Double stipend;

    @Column(name = "salary_package", length = 60)
    private String salaryPackage;

    @Column(name = "minimum_cgpa")
    private Double minimumCgpa;

    @Column(name = "eligible_departments", length = 500)
    private String eligibleDepartments;

    @Column(name = "required_skills", length = 500)
    private String requiredSkills;

    @Column(name = "application_deadline", nullable = false)
    private LocalDateTime applicationDeadline;

    @Column(name = "drive_date")
    private LocalDate driveDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DriveStatus status = DriveStatus.DRAFT;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "drive")
    private List<Application> applications = new ArrayList<>();
}
