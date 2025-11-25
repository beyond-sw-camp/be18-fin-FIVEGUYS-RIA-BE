package com.fiveguys.RIA.RIA_Backend.admin.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.admin.model.component.AdminLoader;
import com.fiveguys.RIA.RIA_Backend.admin.model.component.AdminLogMapper;
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
    private final AdminLogMapper adminLogMapper;

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

        AdminLog log = adminLogMapper.toEntity(dto, actor, actorName, actorEmployeeNo);

        adminRepository.save(log);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminLogResponseDto> getLogs(Pageable pageable) {
        return adminRepository.findAll(pageable)
                              .map(adminLogMapper::toResponseDto);
    }

}
