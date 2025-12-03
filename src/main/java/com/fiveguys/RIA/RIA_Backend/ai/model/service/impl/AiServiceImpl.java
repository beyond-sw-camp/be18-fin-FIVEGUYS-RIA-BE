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
import java.util.Map;

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


        // 1) 상위 5개만 추리기
        List<PosRepository.BrandStats> stats = posRepository.findBrand(vip.getCustomerId());

        List<PosRepository.BrandStats> topStats = stats.stream()
                                                       .limit(5)
                                                       .toList();

        aiValidator.validateBrandStatsExists(stats, vipId);

        // 2) 한 번에 LLM 호출해서 브랜드별 reason 맵으로 받기
        Map<String, String> reasonByBrand =
                aiGenerator.generateReasonsBulk(vip, topStats);

        // 3) 엔티티로 변환
        List<Ai> recoList = topStats.stream()
                                    .map(stat -> {
                                        String reason = reasonByBrand.get(stat.getBrandName());
                                        return aiMapper.toAiEntity(vip, stat, reason);
                                    })
                                    .toList();

        // 4) 한 번에 저장
        aiRepository.saveAll(recoList);

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
