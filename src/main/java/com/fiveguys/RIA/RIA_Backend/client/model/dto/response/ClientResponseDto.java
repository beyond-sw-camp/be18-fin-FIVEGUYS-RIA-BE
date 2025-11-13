package com.fiveguys.RIA.RIA_Backend.client.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClientResponseDto {

  private Long clientId;
  private Long clientCompanyId;
  private String name;
  private String position;
  private String email;
  private String phone;
  private Client.Type type;
  private LocalDateTime createdAt;
}
