package com.fiveguys.RIA.RIA_Backend.client.model.repository;

import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

  /** 특정 고객사 내 동일한 이름 + 전화번호 조합 존재 여부 */
  boolean existsByClientCompanyAndNameAndPhone(ClientCompany company, @NotBlank(message = "담당자 이름은 필수값입니다.") String name, String phone);

  Page<Client> findByClientCompanyIdAndIsDeletedFalse(Long clientCompanyId, Pageable pageable);

  //이메일 중복 확인
  boolean existsByEmail(String email);

  /** 특정 고객사 + 이름 검색 */
  @Query("""
      SELECT c
      FROM Client c
      WHERE c.isDeleted = false
        AND c.clientCompany.id = :clientCompanyId
        AND LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
      """)
  Page<Client> searchByCompanyAndName(
      @Param("clientCompanyId") Long clientCompanyId,
      @Param("keyword") String keyword,
      Pageable pageable
  );

  @Query("""
      select c
      from Client c
      where c.clientCompany.id in :companyIds
        and c.isDeleted = false
      order by c.clientCompany.id asc, c.createdAt desc
      """)
  List<Client> findLatestByCompanyIds(@Param("companyIds") List<Long> companyIds);
}
