package com.fiveguys.RIA.RIA_Backend.auth.service;

import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private final Long userId;
  private final String employeeNo;
  private final String email;
  private final String password;
  private final String name;
  private final String department;
  private final User.Status status;
  private final String roleName;

  public CustomUserDetails(User user) {
    this.userId = user.getId();
    this.employeeNo = user.getEmployeeNo();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.name = user.getName();
    this.department = user.getDepartment().name();
    this.status = user.getStatus();
    this.roleName = user.getRole().getRoleName().name();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(roleName));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    // 로그인 ID = employeeNo
    return employeeNo;
  }

  public String getEmail() {
    return email;
  }


  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // INACTIVE만 잠금 처리, TEMP_PASSWORD는 로그인 허용
    return status != User.Status.INACTIVE;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return status != User.Status.INACTIVE;
  }

  public Long getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

  public String getDepartment() {
    return department;
  }

  public User.Status getStatusEnum() {
    return status;
  }

  public String getRoleName() {
    return roleName;
  }
}
