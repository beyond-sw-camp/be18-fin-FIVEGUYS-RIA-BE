package com.fiveguys.RIA.RIA_Backend.client.model.service;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientListResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientProjectHistoryResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientSimplePageResponseDto;

public interface ClientService {

  ClientResponseDto register(Long clientCompanyId, ClientRequestDto dto);

  ClientListResponseDto getClientsByCompany(Long clientCompanyId, int page, int size);

  ClientSimplePageResponseDto getSimpleClientsByCompany(
      Long clientCompanyId,
      String keyword,
      int page,
      int size
  );

  ClientProjectHistoryResponseDto getClientProjectHistory(Long clientId);

}
