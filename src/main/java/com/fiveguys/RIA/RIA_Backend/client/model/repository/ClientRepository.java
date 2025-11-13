package com.fiveguys.RIA.RIA_Backend.client.model.repository;

import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

  /** 특정 고객사 내 동일한 이름 + 전화번호 조합 존재 여부 */
  boolean existsByClientCompanyAndNameAndPhone(ClientCompany company, @NotBlank(message = "담당자 이름은 필수값입니다.") String name, String phone);

  Page<Client> findByClientCompanyIdAndIsDeletedFalse(Long clientCompanyId, Pageable pageable);

}
