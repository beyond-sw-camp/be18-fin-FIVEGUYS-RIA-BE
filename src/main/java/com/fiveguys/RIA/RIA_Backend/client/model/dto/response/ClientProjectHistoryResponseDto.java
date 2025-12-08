package com.fiveguys.RIA.RIA_Backend.client.model.dto.response;

import java.util.List;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientProjectHistoryResponseDto {

  private Long clientId;
  private List<ClientProjectHistoryItemResponseDto> projects;
}
