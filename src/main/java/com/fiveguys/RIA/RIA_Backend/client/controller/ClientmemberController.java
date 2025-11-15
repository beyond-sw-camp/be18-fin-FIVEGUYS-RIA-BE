package com.fiveguys.RIA.RIA_Backend.client.controller;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientListResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class ClientmemberController {

  private final ClientService clientService;

  // 고객 담당자 등록
  @PostMapping("/{clientCompanyId}/clients")
  public ResponseEntity<ClientResponseDto> registerClient(
      @PathVariable Long clientCompanyId,
      @Valid @RequestBody ClientRequestDto dto
  ) {
    ClientResponseDto response = clientService.register(clientCompanyId, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  //특정 고객사의 고객 담당자 목록 조회하기
  @GetMapping("/{clientCompanyId}/clients")
  public ResponseEntity<ClientListResponseDto> getClientsByCompany(
      @PathVariable Long clientCompanyId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
    return ResponseEntity.ok(clientService.getClientsByCompany(clientCompanyId, page, size));
  }
}