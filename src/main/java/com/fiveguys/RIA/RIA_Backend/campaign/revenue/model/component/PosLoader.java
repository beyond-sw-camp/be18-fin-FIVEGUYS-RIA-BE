package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Pos;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.repository.PosRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PosLoader {

  private final PosRepository posRepository;

  /**
   * 역할: targetDate 하루치 POS만 로딩하는 책임
   * - 집계/도메인 로직은 절대 넣지 않는다.
   * - 데이터 접근(infra) 책임만 가진다.
   */
  public List<Pos> loadFor(LocalDate targetDate) {
    LocalDateTime from = targetDate.atStartOfDay();
    LocalDateTime to = targetDate.plusDays(1).atStartOfDay();
    return posRepository.findByPurchaseAtBetween(from, to);
  }
}
