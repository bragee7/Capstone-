package com.campushire.service;

import com.campushire.dto.student.StudentProfileRequest;
import com.campushire.dto.student.StudentProfileResponse;
import com.campushire.exception.ResourceNotFoundException;
import com.campushire.model.entity.Student;
import com.campushire.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private static final int PROFILE_FIELD_COUNT = 8;

    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public StudentProfileResponse getProfile(Long userId) {
        return toResponse(findByUserId(userId));
    }

    @Transactional
    public StudentProfileResponse updateProfile(Long userId, StudentProfileRequest request) {
        Student student = findByUserId(userId);

        if (request.registrationNumber() != null) {
            student.setRegistrationNumber(request.registrationNumber().trim());
        }
        if (request.department() != null) {
            student.setDepartment(request.department().trim());
        }
        if (request.college() != null) {
            student.setCollege(request.college().trim());
        }
        if (request.graduationYear() != null) {
            student.setGraduationYear(request.graduationYear());
        }
        if (request.cgpa() != null) {
            student.setCgpa(request.cgpa());
        }
        if (request.skills() != null) {
            student.setSkills(request.skills().trim());
        }
        if (request.resumeUrl() != null) {
            student.setResumeUrl(request.resumeUrl().trim());
        }
        if (request.bio() != null) {
            student.setBio(request.bio().trim());
        }

        return toResponse(studentRepository.save(student));
    }

    private Student findByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for the current user."));
    }

    private StudentProfileResponse toResponse(Student student) {
        return new StudentProfileResponse(
                student.getId(),
                student.getRegistrationNumber(),
                student.getDepartment(),
                student.getCollege(),
                student.getGraduationYear(),
                student.getCgpa(),
                student.getSkills(),
                student.getResumeUrl(),
                student.getBio(),
                profileCompletion(student));
    }

    private int profileCompletion(Student student) {
        int filled = 0;
        if (isNotBlank(student.getRegistrationNumber())) filled++;
        if (isNotBlank(student.getDepartment())) filled++;
        if (isNotBlank(student.getCollege())) filled++;
        if (student.getGraduationYear() != null) filled++;
        if (student.getCgpa() != null) filled++;
        if (isNotBlank(student.getSkills())) filled++;
        if (isNotBlank(student.getResumeUrl())) filled++;
        if (isNotBlank(student.getBio())) filled++;
        return (int) Math.round((filled * 100.0) / PROFILE_FIELD_COUNT);
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
