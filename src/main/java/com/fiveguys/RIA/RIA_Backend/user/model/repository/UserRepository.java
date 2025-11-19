package com.fiveguys.RIA.RIA_Backend.user.model.repository;

import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmployeeNo(String employeeNo);

    boolean existsByEmployeeNo(@NotBlank(message = "사번은 필수 입력값입니다.") String employeeNo);

}
