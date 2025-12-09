package com.fiveguys.RIA.RIA_Backend.dashboard.model.component;// package com.fiveguys.RIA.RIA_Backend.dashboard.model.component;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.CommonErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class DashboardAuthorization {

  private static final String ROLE_SALES_LEAD = "ROLE_SALES_LEAD";

  // 팀장 전용 API용 가드
  public void ensureSalesLead() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !auth.isAuthenticated()) {
      throw new CustomException(CommonErrorCode.ACCESS_DENIED);
    }

    boolean hasRoleLead = auth.getAuthorities().stream()
        .anyMatch(a -> ROLE_SALES_LEAD.equals(a.getAuthority()));

    if (!hasRoleLead) {
      throw new CustomException(CommonErrorCode.FORBIDDEN);
    }
  }
}
