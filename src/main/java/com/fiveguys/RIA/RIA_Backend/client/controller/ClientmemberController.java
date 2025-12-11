package com.fiveguys.RIA.RIA_Backend.client.controller;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientListResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientProjectHistoryResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientSimplePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
@Tag(name = "ClientMember", description = "고객사 담당자 관리 API")
public class ClientmemberController {

  private final ClientService clientService;

  // 고객 담당자 등록
  @PostMapping("/{clientCompanyId}/clients")
  @Operation(
      summary = "고객사 담당자 등록",
      description = "특정 고객사(clientCompanyId)에 속한 담당자를 등록한다."
  )
  @Parameters({
      @Parameter(name = "clientCompanyId", description = "고객사 ID", required = true)
  })
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "담당자 등록 성공",
          content = @Content(schema = @Schema(implementation = ClientResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "404", description = "고객사 없음"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  public ResponseEntity<ClientResponseDto> registerClient(
      @PathVariable Long clientCompanyId,
      @Valid @RequestBody ClientRequestDto dto
  ) {
    ClientResponseDto response = clientService.register(clientCompanyId, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 특정 고객사의 고객 담당자 목록 조회하기
  @GetMapping("/{clientCompanyId}/clients")
  @Operation(
      summary = "고객사 담당자 목록 조회",
      description = "특정 고객사(clientCompanyId)에 소속된 담당자 리스트를 조회한다."
  )
  @Parameters({
      @Parameter(name = "clientCompanyId", description = "고객사 ID", required = true),
      @Parameter(name = "page", description = "페이지 번호(1부터 시작)", example = "1"),
      @Parameter(name = "size", description = "페이지 크기", example = "20")
  })
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = ClientListResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "고객사 없음"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  public ResponseEntity<ClientListResponseDto> getClientsByCompany(
      @PathVariable Long clientCompanyId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
    return ResponseEntity.ok(clientService.getClientsByCompany(clientCompanyId, page, size));
  }

  // 특정 고객사의 고객 담당자 목록 조회(내부용)
  @GetMapping("/{clientCompanyId}/clients/simple")
  @Operation(
      summary = "고객사 담당자 간단 목록 조회(내부용)",
      description = "검색 및 선택용 간단 담당자 목록을 조회한다."
  )
  @Parameters({
      @Parameter(name = "clientCompanyId", description = "고객사 ID", required = true),
      @Parameter(name = "keyword", description = "검색 키워드"),
      @Parameter(name = "page", description = "페이지 번호", example = "1"),
      @Parameter(name = "size", description = "페이지 크기", example = "10")
  })
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = ClientSimplePageResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "고객사 없음"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  public ResponseEntity<ClientSimplePageResponseDto> getSimpleClientsByCompany(
      @PathVariable Long clientCompanyId,
      @RequestParam(value = "keyword", required = false) String keyword,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
        clientService.getSimpleClientsByCompany(clientCompanyId, keyword, page, size)
    );
  }

  // 고객 담당자의 프로젝트 이력 조회
  @GetMapping("/{clientId}/history/projects")
  @Operation(
      summary = "고객 담당자의 프로젝트 이력 조회",
      description = "특정 고객 담당자(clientId)가 참여한 프로젝트 이력을 조회한다."
  )
  @Parameters({
      @Parameter(name = "clientId", description = "고객 담당자 ID", required = true)
  })
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = ClientProjectHistoryResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "해당 고객 담당자 없음"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  public ResponseEntity<ClientProjectHistoryResponseDto> getClientProjectHistory(
      @PathVariable Long clientId
  ) {
    return ResponseEntity.ok(clientService.getClientProjectHistory(clientId));
  }
}
