package com.fiveguys.RIA.RIA_Backend.ai.controller;

import com.fiveguys.RIA.RIA_Backend.ai.model.dto.AiResponseDto;
import com.fiveguys.RIA.RIA_Backend.ai.model.dto.RecommendResponseDto;
import com.fiveguys.RIA.RIA_Backend.ai.model.service.AiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AiControllerTest {

    @Mock
    private AiService aiService;

    @InjectMocks
    private AiController aiController;

    @Test
    @DisplayName("generate: AI 추천 생성 성공 시 200 응답과 RecommendResponseDto 반환")
    void generate_success() {
        // given
        Long vipId = 10L;

        RecommendResponseDto responseDto = RecommendResponseDto.builder()
                                                               .success(true)
                                                               .message("생성 완료")
                                                               .build();

        given(aiService.RecommendationsVip(vipId)).willReturn(responseDto);

        // when
        ResponseEntity<RecommendResponseDto> result = aiController.generate(vipId);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).isEqualTo(responseDto);

        verify(aiService).RecommendationsVip(vipId);
    }

    @Test
    @DisplayName("get: 추천 목록 조회 성공 시 List<AiResponseDto> 반환")
    void get_success() {
        // given
        Long vipId = 5L;

        AiResponseDto dto1 = AiResponseDto.builder()
                                          .reason("최근 구매 기록 기반")
                                          .build();

        AiResponseDto dto2 = AiResponseDto.builder()
                                          .reason("선호 카테고리 분석")
                                          .build();

        List<AiResponseDto> list = List.of(dto1, dto2);

        given(aiService.getRecommendations(vipId)).willReturn(list);

        // when
        List<AiResponseDto> result = aiController.get(vipId);

        // then
        assertThat(result).containsExactly(dto1, dto2);

        verify(aiService).getRecommendations(vipId);
    }
}

