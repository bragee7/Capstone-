package com.campushire.service;

import com.campushire.dto.student.StudentProfileRequest;
import com.campushire.dto.student.StudentProfileResponse;
import com.campushire.exception.ResourceNotFoundException;
import com.campushire.model.entity.Student;
import com.campushire.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository);
        student = new Student();
        student.setRegistrationNumber("21CS123");
        student.setDepartment("Computer Science");
        student.setCollege("Example College");
        student.setGraduationYear(2027);
        student.setCgpa(BigDecimal.valueOf(8.5));
        student.setSkills("Java, SQL");
        student.setBio("A bio.");
    }

    @Test
    void getProfile_computesCompletionPercentage() {
        when(studentRepository.findByUserId(1L)).thenReturn(Optional.of(student));

        StudentProfileResponse response = studentService.getProfile(1L);

        assertEquals(88, response.profileCompletion());
    }

    @Test
    void getProfile_throwsWhenStudentMissing() {
        when(studentRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getProfile(1L));
    }

    @Test
    void updateProfile_appliesOnlyNonNullFields() {
        student.setResumeUrl(null);
        when(studentRepository.findByUserId(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        StudentProfileRequest request = new StudentProfileRequest(
                null, "Information Technology", null, null, null, null, "https://example.com/resume.pdf", null);

        StudentProfileResponse response = studentService.updateProfile(1L, request);

        assertEquals("Information Technology", response.department());
        assertEquals("https://example.com/resume.pdf", response.resumeUrl());
        assertEquals("21CS123", response.registrationNumber());
        assertEquals(100, response.profileCompletion());
        verify(studentRepository).save(any(Student.class));
    }
}
