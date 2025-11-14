package com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.dto.request.ProposalUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ProposalErrorCode;
import org.springframework.stereotype.Component;

@Component
public class ProposalValidator {

  public void validateCreate(ProposalCreateRequestDto dto) {

    if (dto.getClientCompanyId() == null) {
      throw new CustomException(ProposalErrorCode.CLIENT_COMPANY_NOT_FOUND);
    }

    if (dto.getClientId() == null) {
      throw new CustomException(ProposalErrorCode.CLIENT_NOT_FOUND);
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
}