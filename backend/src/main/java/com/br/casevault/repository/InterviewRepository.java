package com.br.casevault.repository;

import com.br.casevault.model.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByApplicationId(Long applicationId);
    List<Interview> findByApplicationStudentId(Long studentId);
    List<Interview> findByApplicationJobPostingCompanyId(Long companyId);
}
