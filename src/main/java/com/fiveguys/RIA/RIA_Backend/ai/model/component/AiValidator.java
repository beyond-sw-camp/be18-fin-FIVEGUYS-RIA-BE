package com.fiveguys.RIA.RIA_Backend.ai.model.component;

import com.fiveguys.RIA.RIA_Backend.pos.model.repository.PosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AiValidator {

    public void validateBrandStatsExists(List<PosRepository.BrandStats> stats, Long vipId) {
        if (stats == null || stats.isEmpty()) {
            throw new IllegalStateException("해당 VIP에 대한 브랜드 매출 데이터가 없습니다. vipId=" + vipId);
        }
    }
}
