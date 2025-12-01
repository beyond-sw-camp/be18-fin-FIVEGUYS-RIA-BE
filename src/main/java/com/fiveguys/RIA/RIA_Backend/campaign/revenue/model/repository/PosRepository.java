package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Pos;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosRepository extends JpaRepository<Pos,Long> {
  List<Pos> findByPurchaseAtBetween(LocalDateTime from, LocalDateTime to);

}
