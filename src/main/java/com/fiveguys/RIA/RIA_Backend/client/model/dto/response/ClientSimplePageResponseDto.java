package com.fiveguys.RIA.RIA_Backend.client.model.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientSimplePageResponseDto {

  private final List<ClientSimpleResponseDto> content;
  private final long totalCount;
  private final int currentPage;
  private final int pageSize;
}