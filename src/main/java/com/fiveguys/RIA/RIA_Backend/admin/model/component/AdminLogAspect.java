package com.fiveguys.RIA.RIA_Backend.admin.model.component;

import com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request.AdminLogRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminLogService;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminLogAspect {

    private final CurrentUserProvider currentUserProvider;
    private final AdminLogService adminLogService;
    private final HttpServletRequest request;

    @Around("execution(* com.fiveguys.RIA.RIA_Backend..controller..*(..))")


    public Object logAdminActions(ProceedingJoinPoint pjp) throws Throwable {

        String method = request.getMethod();
        String uri = request.getRequestURI();

        // GET 요청은 굳이 가져 올 필요 없을거 같아 제외함
        if (method.equals("GET") && uri.contains("/storages/") && uri.endsWith("/download")) {
        } else if ("GET".equalsIgnoreCase(method)) {
            return pjp.proceed();
        }
        Object result;
        String state = "SUCCESS";

        try {
            // 실제 컨트롤러 로직 실행
            result = pjp.proceed();
        } catch (Throwable ex) {
            state = "FAIL";
            throw ex;
        } finally {
            try {
                User actor = currentUserProvider.getCurrentUser();

                String className = pjp.getSignature().getDeclaringType().getSimpleName();
                String methodName = pjp.getSignature().getName();
                String logName = className + "." + methodName;
                String resource = method + " " + uri;

                AdminLogRequestDto dto = AdminLogRequestDto.builder()
                                                           .actorId(actor.getId())
                                                           .logName(logName)
                                                           .resource(resource)
                                                           .state(state)
                                                           .build();

                adminLogService.save(dto);
            } catch (Exception e) {
            }
        }

        return result;
    }
}
