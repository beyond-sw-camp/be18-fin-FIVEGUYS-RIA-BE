package com.fiveguys.RIA.RIA_Backend.auth.service;

import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthErrorCode;
import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthException;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsLoader {

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public CustomUserDetails loadByEmployeeNo(String employeeNo) {
    User user = userRepository.findByEmployeeNoWithRole(employeeNo)
        .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));
    return new CustomUserDetails(user);
  }
}
