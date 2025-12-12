package com.fiveguys.RIA.RIA_Backend.client.controller;

import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientCompanyRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanySummaryResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanySimplePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.LeadCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Category;
import com.fiveguys.RIA.RIA_Backend.client.model.service.ClientCompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
@Tag(name = "ClientCompany", description = "고객사/잠재 고객사 관리 API")
public class ClientController {

  private final ClientCompanyService clientCompanyService;

  // 신규 고객사 등록
  @PostMapping("/clients")
  @Operation(
      summary = "신규 고객사 등록",
      description = "실제 거래 중인 고객사를 신규 등록한다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "신규 고객사 등록 성공",
          content = @Content(schema = @Schema(implementation = ClientCompanyResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
  })
  public ResponseEntity<ClientCompanyResponseDto> registerClient(
      @Valid @RequestBody ClientCompanyRequestDto dto
  ) {
    ClientCompanyResponseDto response = clientCompanyService.registerClient(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  //신규 잠재 고객사 등록
  @PostMapping("/leads")
  @Operation(
      summary = "신규 잠재 고객사 등록",
      description = "영업 파이프라인 상의 잠재 고객사를 등록한다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "잠재 고객사 등록 성공",
          content = @Content(schema = @Schema(implementation = ClientCompanyResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
  })
  public ResponseEntity<ClientCompanyResponseDto> registerLead(
      @Valid @RequestBody ClientCompanyRequestDto dto
  ) {
    ClientCompanyResponseDto response = clientCompanyService.registerLead(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 고객사 목록 조회
  @GetMapping("/clients")
  @Operation(
      summary = "고객사 목록 조회",
      description = "실제 거래 중인 고객사 목록을 검색/페이징하여 조회한다."
  )
  @Parameters({
      @Parameter(name = "keyword", description = "고객사명/메모 등 키워드 검색", required = false),
      @Parameter(name = "category", description = "업종 카테고리 enum", required = false,
          schema = @Schema(implementation = Category.class)),
      @Parameter(name = "page", description = "페이지 번호(1부터 시작)", example = "1"),
      @Parameter(name = "size", description = "페이지 당 데이터 개수", example = "20")
  })
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = ClientCompanyListPageResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
  })
  public ResponseEntity<ClientCompanyListPageResponseDto> getCustomerCompanies(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) Category category,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
    return ResponseEntity.ok(
        clientCompanyService.getClientCompanies(keyword, category, page, size)
    );
  }

  @GetMapping("/leads")
  @Operation(
      summary = "잠재 고객사 목록 조회",
      description = "영업 리드(잠재 고객사) 목록을 검색/페이징하여 조회한다."
  )
  @Parameters({
      @Parameter(name = "keyword", description = "고객사명/메모 등 키워드 검색", required = false),
      @Parameter(name = "category", description = "업종 카테고리 enum", required = false,
          schema = @Schema(implementation = Category.class)),
      @Parameter(name = "page", description = "페이지 번호(1부터 시작)", example = "1"),
      @Parameter(name = "size", description = "페이지 당 데이터 개수", example = "20")
  })
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = LeadCompanyListPageResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
  })
  public ResponseEntity<LeadCompanyListPageResponseDto> getLeadCompanies(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) Category category,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
    return ResponseEntity.ok(
        clientCompanyService.getLeadCompanies(keyword, category, page, size)
    );
  }


  //고객사 상세 조회
  @GetMapping("/{clientCompanyId}")
  @Operation(
      summary = "고객사 상세 조회",
      description = "고객사 ID로 상세 정보를 조회한다."
  )
  @Parameters({
      @Parameter(name = "clientCompanyId", description = "고객사 ID", required = true)
  })
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = ClientCompanyResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "해당 ID의 고객사 없음", content = @Content),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
  })
  public ResponseEntity<ClientCompanyResponseDto> getClientCompanyDetail(
      @PathVariable Long clientCompanyId
  ) {
    ClientCompanyResponseDto response = clientCompanyService.getClientCompanyDetail(clientCompanyId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/simple")
  @Operation(
      summary = "고객사 간단 목록 조회",
      description = "드롭다운/검색용으로 사용하는 간단 고객사 목록을 조회한다."
  )
  @Parameters({
      @Parameter(name = "type", description = "조회 타입 (예: client / lead 등)", required = false),
      @Parameter(name = "keyword", description = "고객사명 검색 키워드", required = false),
      @Parameter(name = "page", description = "페이지 번호(1부터 시작)", example = "1"),
      @Parameter(name = "size", description = "페이지 당 데이터 개수", example = "10")
  })
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = ClientCompanySimplePageResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
  })
  public ResponseEntity<ClientCompanySimplePageResponseDto> getSimpleCompanies(
      @RequestParam(value = "type", required = false) String type,
      @RequestParam(value = "keyword", required = false) String keyword,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    ClientCompanySimplePageResponseDto result =
        clientCompanyService.getSimpleCompanies(type, keyword, page, size);

    return ResponseEntity.ok(result);
  }

  @GetMapping("/clients/{clientCompanyId}/summary")
  @Operation(
      summary = "고객사 입점/계약 요약 조회",
      description = "입주명, 면적, 입점일, 계약 기간, 총 임대료, 매출 수수료율을 조회한다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(
              implementation = ClientCompanySummaryResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "고객사 없음", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
  })
  public ResponseEntity<ClientCompanySummaryResponseDto> getClientCompanyLeaseSummary(
      @PathVariable Long clientCompanyId
  ) {
    return ResponseEntity.ok(
        clientCompanyService.getClientCompanyLeaseSummary(clientCompanyId)
    );
  }

}
