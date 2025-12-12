package com.fiveguys.RIA.RIA_Backend.ai.model.component;

import com.fiveguys.RIA.RIA_Backend.ai.model.exception.AiErrorCode;
import com.fiveguys.RIA.RIA_Backend.ai.model.exception.AiException;
import com.fiveguys.RIA.RIA_Backend.pos.model.repository.PosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AiValidator {

    public void validateBrandProductStatsExists(
            List<PosRepository.BrandProductStats> stats,
            Long vipId
    ) {
        if (stats == null || stats.isEmpty()) {
            throw new AiException(
                    AiErrorCode.NO_POS_DATA_FOR_VIP,
                    "VIP ID " + vipId + "에 대한 POS 데이터가 없습니다."
            );
        }
    }
}
