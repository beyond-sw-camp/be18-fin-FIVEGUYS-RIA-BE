package com.fiveguys.RIA.RIA_Backend.client.model.dto.response;

import lombok.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientCompanyListPageResponseDto {
  private long totalCount;
  private int page;
  private int size;
  private List<ClientCompanyListResponseDto> data;
}
