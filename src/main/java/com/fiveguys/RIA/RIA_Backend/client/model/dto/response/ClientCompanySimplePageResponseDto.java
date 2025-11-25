package com.fiveguys.RIA.RIA_Backend.client.model.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientCompanySimplePageResponseDto {

  private List<ClientCompanySimpleResponseDto> content;
  private long totalCount;
  private int currentPage;
  private int pageSize;
}