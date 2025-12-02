package com.fiveguys.RIA.RIA_Backend.ai.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.ai.model.component.AiGenerator;
import com.fiveguys.RIA.RIA_Backend.ai.model.component.AiMapper;
import com.fiveguys.RIA.RIA_Backend.ai.model.component.AiValidator;
import com.fiveguys.RIA.RIA_Backend.ai.model.dto.AiResponseDto;
import com.fiveguys.RIA.RIA_Backend.ai.model.dto.RecommendResponseDto;
import com.fiveguys.RIA.RIA_Backend.ai.model.entity.Ai;
import com.fiveguys.RIA.RIA_Backend.ai.model.repository.AiRepository;
import com.fiveguys.RIA.RIA_Backend.ai.model.service.AiService;
import com.fiveguys.RIA.RIA_Backend.pos.model.repository.PosRepository;
import com.fiveguys.RIA.RIA_Backend.vip.model.component.VipLoader;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiServiceImpl implements AiService {

    private final VipLoader vipLoader;
    private final PosRepository posRepository;
    private final AiRepository aiRepository;
    private final AiGenerator aiGenerator;
    private final AiValidator aiValidator;
    private final AiMapper aiMapper;

    @Override
    @Transactional
    public RecommendResponseDto RecommendationsVip(Long vipId) {

        Vip vip = vipLoader.loadById(vipId);

        var stats = posRepository.findBrand(vip.getCustomerId());

        aiValidator.validateBrandStatsExists(stats, vipId);

        stats.stream()
             .limit(5)
             .forEach(stat -> {
                 String reason = aiGenerator.generateReason(
                         vip.getName(),
                         stat.getBrandName(),
                         stat.getTotalAmount(),
                         stat.getPurchaseCount()
                 );

                 Ai reco = aiMapper.toAiEntity(vip, stat, reason);
                 aiRepository.save(reco);
             });

        return RecommendResponseDto.builder()
                                   .success(true)
                                   .message("AI 추천 생성 완료")
                                   .build();
    }

    @Override
    public List<AiResponseDto> getRecommendations(Long vipId) {
        List<Ai> recommendations = aiRepository.findTop5(vipId);
        return aiMapper.toResponseDtoList(recommendations);
    }
}
