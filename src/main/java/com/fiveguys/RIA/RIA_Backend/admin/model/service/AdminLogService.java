package com.fiveguys.RIA.RIA_Backend.admin.model.service;

import com.fiveguys.RIA.RIA_Backend.admin.model.dto.Request.AdminLogRequestDto;
import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.AdminLogResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminLogService {

    void save(AdminLogRequestDto dto);

    Page<AdminLogResponseDto> getLogs(Pageable pageable);

}
