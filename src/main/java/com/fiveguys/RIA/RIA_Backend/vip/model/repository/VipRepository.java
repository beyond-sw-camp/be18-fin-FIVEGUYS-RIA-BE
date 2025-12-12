package com.fiveguys.RIA.RIA_Backend.vip.model.repository;

import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip.VipGrade;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VipRepository extends JpaRepository<Vip, Long> {

  // 단순 등급별 페이징 조회 (필터 없이 사용할 때)
  Page<Vip> findByGrade(VipGrade grade, Pageable pageable);

  // 등급별 카운트 (통계용)
  long countByGrade(VipGrade grade);

  // 등급 + 키워드(이름, 연락처) 검색 통합
  @Query("""
      select v
      from Vip v
      where (:grade is null or v.grade = :grade)
        and (
          :keyword is null
          or lower(v.name) like lower(concat('%', :keyword, '%'))
          or lower(v.phone) like lower(concat('%', :keyword, '%'))
        )
      """)
  Page<Vip> searchVip(
      @Param("grade") VipGrade grade,
      @Param("keyword") String keyword,
      Pageable pageable
  );

  boolean existsByCustomerId(Long customerId);
}
