package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import com.fiveguys.RIA.RIA_Backend.pos.model.entity.Pos;
import org.springframework.stereotype.Component;

@Component
public class PosValidator {
  
//   * 역할: POS 1건의 최소 유효성 검증만 담당
//   * - 어떤 필드가 필수인지 정책을 캡슐화
//   * - 잘못된 레코드를 배제하기 위한
  public boolean isValid(Pos pos) {
    if (pos.getStoreTenantMapId() == null) return false;
    if (pos.getAmount() == null) return false;
    if (pos.getPurchaseAt() == null) return false;
    return true;
  }
}
