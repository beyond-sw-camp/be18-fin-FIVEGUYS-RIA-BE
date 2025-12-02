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
                            .recoId(entity.getId())
                            .recoType(entity.getRecoType())
                            .targetName(entity.getTargetName())
                            .score(entity.getScore())
                            .reason(entity.getReason())
                            .build();
    }

    public List<AiResponseDto> toResponseDtoList(List<Ai> entities) {
        return entities.stream()
                       .map(this::toResponseDto)
                       .toList();
    }

    public Ai toAiEntity(Vip vip,
                         PosRepository.BrandStats stat,
                         String reason) {

        return Ai.builder()
                 .vip(vip)
                 .recoType("BRAND")
                 .targetName(stat.getBrandName())
                 .score(stat.getTotalAmount())
                 .reason(reason)
                 .build();
    }
}
