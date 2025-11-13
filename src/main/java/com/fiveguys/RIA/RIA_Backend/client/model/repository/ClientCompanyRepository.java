package com.fiveguys.RIA.RIA_Backend.client.model.repository;

import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface ClientCompanyRepository extends JpaRepository<ClientCompany, Long> {

  /** 회사명이 이미 존재하는지 확인 (중복 등록 방지용) */
  boolean existsByCompanyName(String companyName);

  @Query("""
        SELECT c FROM ClientCompany c
        WHERE c.type = 'CUSTOMER'
          AND c.isDeleted = false
          AND (:keyword IS NULL OR c.companyName LIKE %:keyword%)
          AND (:category IS NULL OR c.category = :category)
    """)
  Page<ClientCompany> findCustomerCompanies(@Param("keyword") String keyword,
      @Param("category") ClientCompany.Category category,
      Pageable pageable);
}
