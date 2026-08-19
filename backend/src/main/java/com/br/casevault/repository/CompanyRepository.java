package com.br.casevault.repository;

import com.br.casevault.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByUserId(Long userId);
    List<Company> findByVerified(boolean verified);
    long countByVerified(boolean verified);
}
