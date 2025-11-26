package com.fiveguys.RIA.RIA_Backend.admin.model.component;

import com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request.AdminLogRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.AdminLogResponseDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.entity.AdminLog;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AdminLogMapper {

    public AdminLogResponseDto toResponseDto(AdminLog log) {
        if (log == null) {
            return null;
        }

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

    public AdminLog toEntity(AdminLogRequestDto dto, User actor, String actorName, String actorEmployeeNo) {
        return AdminLog.builder()
                       .actor(actor)
                       .logName(dto.getLogName())
                       .userName(actorName)
                       .employeeNo(actorEmployeeNo)
                       .resource(dto.getResource())
                       .state(dto.getState())
                       .build();
    }
}
