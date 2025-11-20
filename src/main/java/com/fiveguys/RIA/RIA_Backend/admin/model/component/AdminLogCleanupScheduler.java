package com.fiveguys.RIA.RIA_Backend.admin.model.component;

import com.fiveguys.RIA.RIA_Backend.admin.model.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminLogCleanupScheduler {

    private final AdminRepository adminRepository;

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void cleanOldLogs() {

        LocalDateTime threshold = LocalDateTime.now().minusDays(180);

        int deletedCount = adminRepository.deleteByCreatedAtBefore(threshold);

        log.info("관리자 로그 자동 정리: {}건 삭제 ({} 이전 로그)", deletedCount, threshold);
    }
}
