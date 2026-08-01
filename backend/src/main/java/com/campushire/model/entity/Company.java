package com.campushire.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company extends BaseEntity {

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "company_name", nullable = false, length = 160)
    private String companyName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 80)
    private String industry;

    @Column(length = 200)
    private String website;

    @Column(length = 160)
    private String location;

    @Column(nullable = false)
    private boolean verified = false;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "company")
    private List<HiringDrive> hiringDrives = new ArrayList<>();
}
