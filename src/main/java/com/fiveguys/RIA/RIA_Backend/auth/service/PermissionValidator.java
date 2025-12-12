package com.fiveguys.RIA.RIA_Backend.auth.service;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.CommonErrorCode;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PermissionValidator {

  // 접근 권한 확인
  public void validateOwnerOrLeadOrAdmin(User owner, CustomUserDetails user) {
    if (owner == null || user == null) {
      throw new CustomException(CommonErrorCode.INVALID_REQUEST);
    }

    Long requesterId = user.getUserId();
    boolean isOwner = owner.getId().equals(requesterId);
    boolean isLeadOrAdmin = user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(role ->
            role.equals("ROLE_SALES_LEAD") ||
                role.equals("ROLE_ADMIN")
        );

    if (!isOwner && !isLeadOrAdmin) {
      log.warn("권한 거부: requesterId={}, ownerId={}, roles={}",
          requesterId, owner.getId(), user.getAuthorities());
      throw new CustomException(CommonErrorCode.FORBIDDEN);
    }
  }
}
