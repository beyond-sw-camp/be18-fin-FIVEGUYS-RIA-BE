package com.fiveguys.RIA.RIA_Backend.client.model.service;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientListResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;

public interface ClientService {

  ClientResponseDto register(ClientRequestDto dto);

  ClientListResponseDto getClientsByCompany(Long clientCompanyId, int page, int size);

}
