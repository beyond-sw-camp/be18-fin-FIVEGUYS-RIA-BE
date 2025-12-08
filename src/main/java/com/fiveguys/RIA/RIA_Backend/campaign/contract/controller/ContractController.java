package com.fiveguys.RIA.RIA_Backend.campaign.contract.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.CreateContractRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.UpdateContractRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractDeleteResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractCompleteResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractEstimateDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractEstimateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.UpdateContractResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.CreateContractResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.service.ContractService;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public ResponseEntity<CreateContractResponseDto> createContract(
            @RequestBody CreateContractRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        CreateContractResponseDto response = contractService.createContract(requestDto, userDetails.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/estimates")
    public ResponseEntity<List<ContractEstimateResponseDto>> getEstimateList(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "clientCompanyId", required = false) Long clientCompanyId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false, defaultValue = "COMPLETED") Estimate.Status status,
            @RequestParam(value = "limit", required = false, defaultValue = "50") Integer limit,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        List<ContractEstimateResponseDto> response = contractService.getEstimateList(
                projectId, clientCompanyId, keyword, status, limit, userDetails.getUserId()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/estimates/{estimateId}")
    public ResponseEntity<ContractEstimateDetailResponseDto> getEstimateDetail(
            @PathVariable Long estimateId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        ContractEstimateDetailResponseDto response =
                contractService.getEstimateDetail(estimateId, userDetails.getUserId());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ContractPageResponseDto<ContractListResponseDto>> getContractList(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "clientCompanyId", required = false) Long clientCompanyId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Contract.Status status,
            @RequestParam(value = "contractDate", required = false) LocalDate contractDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        ContractPageResponseDto<ContractListResponseDto> response =
                contractService.getContractList(projectId, clientCompanyId, keyword, status, contractDate, page, size, userDetails.getUserId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<ContractDetailResponseDto> getContractDetail(
            @PathVariable("contractId") Long contractId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ContractDetailResponseDto response = contractService.getContractDetail(contractId, userDetails.getUserId());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{contractId}/complete")
    public ResponseEntity<ContractCompleteResponseDto> completeContract(
            @PathVariable("contractId") Long contractId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ContractCompleteResponseDto response = contractService.completeContract(contractId, userDetails.getUserId());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("{contractId}")
    public ResponseEntity<UpdateContractResponseDto> updateContract(
            @PathVariable("contractId") Long contractId,
            @RequestBody UpdateContractRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UpdateContractResponseDto response = contractService.updateContract(contractId, requestDto, userDetails.getUserId());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{contractId}/cancel")
    public ResponseEntity<ContractDeleteResponseDto> deleteContract(
            @PathVariable("contractId") Long contractId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ContractDeleteResponseDto response = contractService.deleteContract(contractId, userDetails.getUserId());

        return ResponseEntity.ok(response);
    }
}

