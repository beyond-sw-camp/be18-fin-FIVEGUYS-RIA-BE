package com.fiveguys.RIA.RIA_Backend.client.model.repository;

import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Category;

@Repository
public interface ClientCompanyRepository extends JpaRepository<ClientCompany, Long> {

  boolean existsByCompanyName(String companyName);

  @Query("""
        SELECT c FROM ClientCompany c
        WHERE c.type = 'CLIENT'
          AND c.isDeleted = false
          AND (:keyword IS NULL OR c.companyName LIKE %:keyword%)
          AND (:category IS NULL OR c.category = :category)
    """)
  Page<ClientCompany> findCustomerCompanies(
      @Param("keyword") String keyword,
      @Param("category") Category category,
      Pageable pageable
  );

  @Query("""
    SELECT c FROM ClientCompany c
    WHERE c.type = 'LEAD'
      AND (:keyword IS NULL OR c.companyName LIKE %:keyword%)
      AND (:category IS NULL OR c.category = :category)
      AND c.isDeleted = false
""")
  Page<ClientCompany> findLeadCompanies(
      @Param("keyword") String keyword,
      @Param("category") Category category,
      Pageable pageable
  );

  boolean existsByBusinessNumber(String businessNumber);

  boolean existsByWebsite(String website);
}

