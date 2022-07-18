package com.belvenix.dynamickafkaconsumer.repository;

import com.belvenix.dynamickafkaconsumer.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
