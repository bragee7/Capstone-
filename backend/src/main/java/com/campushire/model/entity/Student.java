package com.campushire.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student extends BaseEntity {

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "registration_number", length = 40)
    private String registrationNumber;

    @Column(length = 80)
    private String department;

    @Column(length = 120)
    private String college;

    @Column(name = "graduation_year")
    private Integer graduationYear;

    @Column(precision = 4, scale = 2)
    private BigDecimal cgpa;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(name = "resume_url", length = 500)
    private String resumeUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;
}
