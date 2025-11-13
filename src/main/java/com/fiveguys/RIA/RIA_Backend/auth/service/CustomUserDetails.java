package com.fiveguys.RIA.RIA_Backend.auth.service;

import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(user.getRole().getRoleName().name()));
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    // 로그인 ID = employeeNo
    return user.getEmployeeNo();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // INACTIVE만 잠금 처리, TEMP_PASSWORD는 로그인 허용
    return user.getStatus() != User.Status.INACTIVE;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    // INACTIVE만 로그인 불가, TEMP_PASSWORD는 로그인 허용
    return user.getStatus() != User.Status.INACTIVE;
  }

  public Long getUserId() {
    return user.getId();
  }

  public String getName() {
    return user.getName();
  }

  public String getDepartment() {
    return user.getDepartment().name();
  }

  public User getUser() {
    return user;
  }

}