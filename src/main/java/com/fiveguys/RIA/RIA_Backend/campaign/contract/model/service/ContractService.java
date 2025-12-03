package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.service;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.ContractCompleteRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.CreateContractRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractCompleteResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractEstimateDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractEstimateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.CreateContractResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;

import java.time.LocalDate;
import java.util.List;

public interface ContractService {
    List<ContractEstimateResponseDto> getEstimateList(Long projectId, Long clientCompanyId, String keyword, Estimate.Status status, Integer limit, Long userId);

    ContractEstimateDetailResponseDto getEstimateDetail(Long estimateId, Long userId);

    CreateContractResponseDto createContract(CreateContractRequestDto requestDto, Long userId);

    ContractPageResponseDto<ContractListResponseDto> getContractList(Long projectId, Long clientCompanyId, String keyword, Contract.Status status, LocalDate contractDate, int page, int size, Long userId);

    ContractDetailResponseDto getContractDetail(Long contractId, Long userId);

    ContractCompleteResponseDto completeContract(Long contractId, Long userId);
}
