package com.fiveguys.RIA.RIA_Backend.client.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany.ClientCompanyContractLoader;
import com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany.ClientCompanyLeadSummaryLoader;
import com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany.ClientCompanySummaryLoader;
import com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany.ClientCompanyLoader;
import com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany.ClientCompanyMapper;
import com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany.ClientCompanyValidator;
import com.fiveguys.RIA.RIA_Backend.client.model.component.clientcompany.LeadActivityLoader;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.request.ClientCompanyRequestDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanySummaryResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanyResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.ClientCompanySimplePageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.dto.response.LeadCompanyListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Category;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientCompanyRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.service.ClientCompanyService;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientCompanyServiceImpl implements ClientCompanyService {

  private final ClientCompanyRepository clientCompanyRepository;
  private final ClientCompanyLoader clientCompanyLoader;
  private final ClientCompanyValidator clientCompanyValidator;
  private final ClientCompanyMapper clientCompanyMapper;
  private final ClientCompanyContractLoader clientCompanyContractLoader;
  private final ClientCompanySummaryLoader clientCompanySummaryLoader;
  private final ClientCompanyLeadSummaryLoader clientCompanyLeadSummaryLoader;
  private final LeadActivityLoader leadActivityLoader;


  //   LEAD / CUSTOMER 등록
  @Override
  public ClientCompanyResponseDto registerLead(ClientCompanyRequestDto dto) {
    return registerInternal(dto, ClientCompany.Type.LEAD);
  }

  @Override
  public ClientCompanyResponseDto registerClient(ClientCompanyRequestDto dto) {
    return registerInternal(dto, ClientCompany.Type.CLIENT);
  }

  private ClientCompanyResponseDto registerInternal(
      ClientCompanyRequestDto dto,
      ClientCompany.Type forcedType
  ) {

    // 1. 입력값 검증
    clientCompanyValidator.validateRegister(dto);

    // 2. DTO 재조립
    ClientCompanyRequestDto fixedDto = ClientCompanyRequestDto.builder()
        .companyName(dto.getCompanyName())
        .category(dto.getCategory())
        .type(forcedType)
        .businessNumber(dto.getBusinessNumber())
        .phone(dto.getPhone())
        .fax(dto.getFax())
        .website(dto.getWebsite())
        .zipCode(dto.getZipCode())
        .address(dto.getAddress())
        .build();

    // 3. 엔티티 생성
    ClientCompany entity = clientCompanyMapper.toEntity(fixedDto);

    // 4. 저장
    ClientCompany saved = clientCompanyRepository.save(entity);

    // 5. 응답 DTO 변환
    return clientCompanyMapper.toDetailDto(saved);
  }


  // 고객사 목록 조회
  // 고객사 목록 조회 (실제 거래 CLIENT)
  @Override
  @Transactional(readOnly = true)
  public ClientCompanyListPageResponseDto getClientCompanies(
      String keyword,
      Category category,
      int page,
      int size
  ) {
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("companyName").ascending());

    Page<ClientCompany> result =
        clientCompanyLoader.loadCustomerCompanies(keyword, category, pageable);

    // 페이지 내 고객사들에 대한 최신 COMPLETED 계약 맵
    Map<Long, Contract> latestMap =
        clientCompanyContractLoader.loadLatestCompletedMap(result.getContent());

    return clientCompanyMapper.toListPageDto(result, latestMap, page, size);
  }

  // 잠재고객사 목록 조회
  @Override
  @Transactional(readOnly = true)
  public LeadCompanyListPageResponseDto getLeadCompanies(
      String keyword,
      Category category,
      int page,
      int size
  ) {
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("companyName").ascending());

    Page<ClientCompany> result =
        clientCompanyLoader.loadLeadCompanies(keyword, category, pageable);

    // 1) 회사별 메인 담당자
    Map<Long, Client> mainContactMap =
        clientCompanyLeadSummaryLoader.loadMainContacts(result.getContent());

    // 2) 회사별 최신 활동일 (메인 담당자 기준)
    Map<Long, LocalDateTime> activityMap =
        leadActivityLoader.loadLatestActivityMap(mainContactMap);

    // 3) DTO 변환
    return clientCompanyMapper.toLeadListPageDto(
        result,
        mainContactMap,
        activityMap,
        page,
        size
    );
  }

  //   상세 조회
  @Override
  @Transactional(readOnly = true)
  public ClientCompanyResponseDto getClientCompanyDetail(Long clientCompanyId) {

    ClientCompany company = clientCompanyLoader.loadCompany(clientCompanyId);

    return clientCompanyMapper.toDetailDto(company);
  }

  // 내부용 고객사 목록 조회
  @Override
  public ClientCompanySimplePageResponseDto getSimpleCompanies(
      String type,
      String keyword,
      int page,
      int size
  ) {
    // 1) 파라미터 정제 (Validator)
    ClientCompany.Type filterType = clientCompanyValidator.parseType(type);
    String kw = clientCompanyValidator.normalizeKeyword(keyword);

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("companyName").ascending());

    // 2) 데이터 조회 (Loader)
    Page<ClientCompany> result = clientCompanyLoader.loadCompanies(filterType, kw, pageable);

    // 3) DTO 변환 (Mapper)
    return clientCompanyMapper.toSimplePageDto(result, page, size);
  }

  @Override
  @Transactional(readOnly = true)
  public ClientCompanySummaryResponseDto getClientCompanyLeaseSummary(Long clientCompanyId) {
    return clientCompanySummaryLoader.load(clientCompanyId);
  }
}
