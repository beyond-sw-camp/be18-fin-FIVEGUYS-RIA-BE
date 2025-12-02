package com.fiveguys.RIA.RIA_Backend.vip.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component.VipCustomerLookupPort;
import com.fiveguys.RIA.RIA_Backend.vip.model.repository.VipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VipCustomerLookupAdapter implements VipCustomerLookupPort {

  private final VipRepository vipRepository;

/* 역할: VipCustomerLookupPort 의 구현체
  내부적으로 VIP 도메인의 VipRepository를 사용해 조회
  revenue 도메인은 VipRepository를 직접 모르고 이 포트만 의존*/
  @Override
  public boolean isVipCustomer(Long customerId) {
    if (customerId == null) return false;
    return vipRepository.existsByCustomerId(customerId);
  }
}
