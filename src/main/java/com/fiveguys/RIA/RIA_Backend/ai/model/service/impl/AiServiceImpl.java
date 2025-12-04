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
import com.fiveguys.RIA.RIA_Backend.pos.model.service.PosStatsService;
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
    private final PosStatsService posStatsService;
    private final AiRepository aiRepository;
    private final AiGenerator aiGenerator;
    private final AiValidator aiValidator;
    private final AiMapper aiMapper;

    @Override
    @Transactional
    public RecommendResponseDto RecommendationsVip(Long vipId) {

        Vip vip = vipLoader.loadById(vipId);

        // 1) POS에서 브랜드+상품 기준 통계 조회
        List<PosRepository.BrandProductStats> stats =
                posStatsService.getBrandProduct(vip.getCustomerId());

        // 데이터 존재 여부 검증 (BrandProduct 용)
        aiValidator.validateBrandProductStatsExists(stats, vipId);

        // 상위 5개만 사용
        List<PosRepository.BrandProductStats> topStats = stats.stream()
                                                              .limit(3)
                                                              .toList();

        // 2) 한 번에 LLM 호출해서 "브랜드|상품" 기준 reason 맵으로 받기
        Map<String, String> reasonByTarget =
                aiGenerator.generateReasonsBulk(vip, topStats);

        // 3) 엔티티로 변환
        List<Ai> recoList = topStats.stream()
                                    .map(stat -> {
                                        String key = buildTargetKey(stat.getBrandName(), stat.getProductName());
                                        String reason = reasonByTarget.get(key);
                                        return aiMapper.toAiEntityFromBrandProduct(vip, stat, reason);
                                    })
                                    .toList();

        // 4) 한 번에 저장
        aiRepository.saveAll(recoList);

        return RecommendResponseDto.builder()
                                   .success(true)
                                   .message("AI 추천 생성 완료")
                                   .build();
    }

    private String buildTargetKey(String brandName, String productName) {
        // AiGenerator 프롬프트에서 사용한 키 포맷과 반드시 동일해야 함
        return brandName + "|" + (productName == null ? "" : productName);
    }

    @Override
    public List<AiResponseDto> getRecommendations(Long vipId) {
        List<Ai> recommendations = aiRepository.findTop3(vipId);
        return aiMapper.toResponseDtoList(recommendations);
    }
}
