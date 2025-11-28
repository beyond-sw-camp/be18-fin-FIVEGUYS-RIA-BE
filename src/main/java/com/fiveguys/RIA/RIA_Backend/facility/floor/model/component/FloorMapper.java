package com.fiveguys.RIA.RIA_Backend.facility.floor.model.component;

import com.fiveguys.RIA.RIA_Backend.facility.floor.model.dto.response.FloorListResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.dto.response.FloorResponseDto;
import com.fiveguys.RIA.RIA_Backend.facility.floor.model.entity.Floor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FloorMapper {
    public FloorResponseDto toDto(Floor floor) {
        return FloorResponseDto.builder()
                .floorId(floor.getFloorId())
                .floorName(floor.getFloorName().name())
                .category(floor.getCategory().name())
                .createdAt(floor.getCreatedAt().toString())
                .updatedAt(floor.getUpdatedAt().toString())
                .build();
    }

    public FloorListResponseDto toListDto(Long zoneId, List<Floor> floors) {
        return FloorListResponseDto.builder()
                .zoneId(zoneId)
                .floors(
                        floors.stream()
                                .map(this::toDto)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
