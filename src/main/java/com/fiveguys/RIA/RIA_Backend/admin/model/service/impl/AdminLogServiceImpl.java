package com.fiveguys.RIA.RIA_Backend.admin.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.admin.model.component.AdminLoader;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request.AdminLogRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.AdminLogResponseDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.entity.AdminLog;
import com.fiveguys.RIA.RIA_Backend.admin.model.repository.AdminRepository;
import com.fiveguys.RIA.RIA_Backend.admin.model.service.AdminLogService;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminLogServiceImpl implements AdminLogService {

    private final AdminRepository adminRepository;
    private final AdminLoader adminLoader;

    @Override
    @Transactional
    public void save(AdminLogRequestDto dto) {

        User actor = null;
        String actorName;
        String actorEmployeeNo;

        // actor_user_id 가 있으면 로그인 성공
        if (dto.getActorId() != null) {
            actor = adminLoader.loadUser(dto.getActorId());

            actorName = actor.getName();
            actorEmployeeNo = actor.getEmployeeNo();
        }
        // actor_user_id 가 없으면 로그인 실패
        else {
            actorName = "UNKNOWN";
            actorEmployeeNo = null;
        }

        AdminLog log = AdminLog.builder()
                               .actor(actor)
                               .logName(dto.getLogName())
                               .userName(actorName)
                               .employeeNo(actorEmployeeNo)
                               .resource(dto.getResource())
                               .state(dto.getState())
                               .build();

        adminRepository.save(log);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminLogResponseDto> getLogs(Pageable pageable) {
        return adminRepository.findAll(pageable)
                                 .map(this::toResponseDto);
    }

    // DB -> Json 변환 -> 클라이언트
    private AdminLogResponseDto toResponseDto(AdminLog log) {
        User actor = log.getActor();

        return AdminLogResponseDto.builder()
                                  .logId(log.getLogId())
                                  .actorId(actor != null ? actor.getId() : null)
                                  .userName(log.getUserName())
                                  .employeeNo(log.getEmployeeNo())
                                  .logName(log.getLogName())
                                  .resource(log.getResource())
                                  .state(log.getState())
                                  .createdAt(log.getCreatedAt())
                                  .build();
    }
}
