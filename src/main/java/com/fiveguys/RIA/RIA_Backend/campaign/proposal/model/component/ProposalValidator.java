package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.repository.ProposalRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProposalErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProposalValidator {

  private final ProposalRepository proposalRepository;

  public void validateCreate(ProposalCreateRequestDto dto) {

    if (dto.getClientCompanyId() == null) {
      throw new CustomException(ProposalErrorCode.CLIENT_COMPANY_REQUIRED);
    }

    if (dto.getClientId() == null) {
      throw new CustomException(ProposalErrorCode.CLIENT_REQUIRED);
    }

    if (dto.getTitle() == null || dto.getTitle().isBlank()) {
      throw new CustomException(ProposalErrorCode.TITLE_REQUIRED);
    }

    if (dto.getSubmitDate() == null) {
      throw new CustomException(ProposalErrorCode.SUBMIT_DATE_REQUIRED);
    }
  }

  public void validateUpdate(ProposalUpdateRequestDto dto) {

    // 제목
    if (dto.getTitle() != null && dto.getTitle().isBlank()) {
      throw new CustomException(ProposalErrorCode.TITLE_REQUIRED);
    }

    // 제출일
    if (dto.getSubmitDate() != null && dto.getSubmitDate() == null) {
      throw new CustomException(ProposalErrorCode.SUBMIT_DATE_REQUIRED);
    }
  }
// 고객사랑 고객 일치
  public void validateClientMatchesCompany(Client client, ClientCompany company) {
    if (!client.getClientCompany().getId().equals(company.getId())) {
      throw new CustomException(ProposalErrorCode.CLIENT_COMPANY_MISMATCH);
    }
  }

  // 제안서 타이틀 중복 검사
  public void validateDuplicateTitle(String title, ClientCompany company) {
    if (proposalRepository.existsByTitleAndClientCompany(title, company)) {
      throw new CustomException(ProposalErrorCode.DUPLICATE_PROPOSAL);
    }
  }
  public void validateStatus(Proposal proposal) {
    if (proposal.getStatus() == Proposal.Status.CANCELED) {
      throw new CustomException(ProposalErrorCode.CANNOT_MODIFY_CANCELED_PROPOSAL);
    }
  }
  
  //고객사 고객담당자 일치 검증
  public void validateClientCompanyChange(
      Client oldClient,
      Client newClient,
      ClientCompany oldCompany,
      ClientCompany newCompany
  ) {
    ClientCompany targetCompany = (newCompany != null) ? newCompany : oldCompany;
    Client targetClient = (newClient != null) ? newClient : oldClient;

    if (!targetClient.getClientCompany().getId().equals(targetCompany.getId())) {
      throw new CustomException(ProposalErrorCode.CLIENT_COMPANY_MISMATCH);
    }
  }

}