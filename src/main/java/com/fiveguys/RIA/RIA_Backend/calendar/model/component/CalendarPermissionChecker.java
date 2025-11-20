package com.fiveguys.RIA.RIA_Backend.calendar.model.component;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarErrorCode;
import com.fiveguys.RIA.RIA_Backend.calendar.model.exception.CalendarException;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class CalendarPermissionChecker {
    private final UserRepository userRepository;

    /** 메모 작성자와 로그인 유저가 같은지 확인 */
    public void checkOwnerPermission(String eventCreatorEmail) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
            throw new CalendarException(CalendarErrorCode.NO_EVENT_PERMISSION);
        }

        CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
        String loginEmail = details.getUser().getEmail();

        // 관리자면 무조건 허용
        if (details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) return;

         // 작성자 아니면 권한 없음
        if (!loginEmail.equals(eventCreatorEmail)) {
            throw new CalendarException(CalendarErrorCode.NO_EVENT_PERMISSION);
        }
    }

    /** 현재 로그인한 사용자의 이메일 가져오기 */
    public String getLoginUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails details)) {
            throw new CalendarException(CalendarErrorCode.NO_EVENT_PERMISSION);
        }

        return details.getUser().getEmail();
    }
}
