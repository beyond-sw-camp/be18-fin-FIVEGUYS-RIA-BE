package com.fiveguys.RIA.RIA_Backend.user.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthErrorCode;
import com.fiveguys.RIA.RIA_Backend.auth.exception.AuthException;
import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.user.model.dto.request.PasswordChangeRequestDto;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import com.fiveguys.RIA.RIA_Backend.user.model.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void changePassword(PasswordChangeRequestDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new AuthException(AuthErrorCode.UNAUTHORIZED);
        }

        String employeeNo = userDetails.getUsername();

        User user = userRepository.findByEmployeeNo(employeeNo)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new AuthException(AuthErrorCode.PASSWORD_MISMATCH);
        }

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new AuthException(AuthErrorCode.PASSWORD_DUPLICATE);
        }

        user.changePassword(passwordEncoder.encode(dto.getNewPassword()));
        user.activateAccount();

        userRepository.save(user);
    }
}
