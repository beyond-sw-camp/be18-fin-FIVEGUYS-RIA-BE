package com.fiveguys.RIA.RIA_Backend.client.model.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientCompanyListResponseDto {
  private Long clientCompanyId;
  private String companyName;
  private String category;
  private LocalDateTime createdAt;
}
