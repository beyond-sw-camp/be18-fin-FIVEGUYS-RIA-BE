package com.fiveguys.RIA.RIA_Backend.client.controller;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientCompanyRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.service.ClientCompanyService;
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
public class ClientController {

  private final ClientCompanyService clientCompanyService;

  // 신규 고객사 등록
  @PostMapping
  public ResponseEntity<ClientCompanyResponseDto> registerClientCompany(
      @Valid @RequestBody ClientCompanyRequestDto dto
  ) {
    ClientCompanyResponseDto response = clientCompanyService.register(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 고객사 목록 조회
  @GetMapping("/customers")
  public ResponseEntity<ClientCompanyListPageResponseDto> getCustomerCompanies(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) ClientCompany.Category category, // Enum 타입으로 받음
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
    return ResponseEntity.ok(
        clientCompanyService.getCustomerCompanies(keyword, category, page, size)
    );
  }

  //고객사 상세 조회
  @GetMapping("/{clientCompanyId}")
  public ResponseEntity<ClientCompanyResponseDto> getClientCompanyDetail(
      @PathVariable Long clientCompanyId
  ) {
    ClientCompanyResponseDto response = clientCompanyService.getClientCompanyDetail(clientCompanyId);
    return ResponseEntity.ok(response);
  }


}
