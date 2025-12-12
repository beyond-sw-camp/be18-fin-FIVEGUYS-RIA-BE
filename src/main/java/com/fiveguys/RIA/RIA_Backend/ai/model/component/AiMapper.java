package com.fiveguys.RIA.RIA_Backend.ai.model.component;

import com.fiveguys.RIA.RIA_Backend.ai.model.dto.AiResponseDto;
import com.fiveguys.RIA.RIA_Backend.ai.model.entity.Ai;
import com.fiveguys.RIA.RIA_Backend.pos.model.repository.PosRepository;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AiMapper {

    public AiResponseDto toResponseDto(Ai entity) {
        return AiResponseDto.builder()
                            .reason(entity.getReason())
                            .build();
    }

    public List<AiResponseDto> toResponseDtoList(List<Ai> entities) {
        return entities.stream()
                       .map(this::toResponseDto)
                       .toList();
    }

    public Ai toAiEntityFromBrandProduct(
            Vip vip,
            PosRepository.BrandProductStats stat,
            String reason
    ) {
        String targetName = stat.getBrandName() + "|" + stat.getProductName();

        return Ai.builder()
                 .vip(vip)
                 .recoType("PRODUCT") // 혹은 enum/string 상수
                 .targetName(targetName)
                 .score(stat.getTotalAmount()) // 일단 총매출을 점수로 쓰거나, 다른 로직 가능
                 .reason(reason)
                 .build();
    }

}
