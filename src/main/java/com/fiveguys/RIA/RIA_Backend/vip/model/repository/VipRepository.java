package com.fiveguys.RIA.RIA_Backend.vip.model.repository;

import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip.VipGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VipRepository extends JpaRepository<Vip, Long> {

  Page<Vip> findByGrade(VipGrade grade, Pageable pageable);

  long countByGrade(VipGrade grade);
}