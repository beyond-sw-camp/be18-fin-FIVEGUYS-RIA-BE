package com.fiveguys.RIA.RIA_Backend.campaign.contract.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.request.CreateContractRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractCompleteResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractDeleteResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractEstimateDetailResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractEstimateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractListResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.ContractPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response.CreateContractResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.service.ContractService;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContractControllerTest {

    @Mock
    private ContractService contractService;

    @InjectMocks
    private ContractController contractController;

    @Test
    @DisplayName("createContract: 계약 생성 성공 시 201 CREATED + 응답 바디 반환")
    void createContract_success() {

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        Long userId = 1L;
        given(userDetails.getUserId()).willReturn(userId);

        CreateContractRequestDto requestDto = mock(CreateContractRequestDto.class);
        CreateContractResponseDto responseDto = mock(CreateContractResponseDto.class);

        given(contractService.createContract(requestDto, userId)).willReturn(responseDto);

        ResponseEntity<CreateContractResponseDto> result =
                contractController.createContract(requestDto, userDetails);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isSameAs(responseDto);

        verify(contractService).createContract(requestDto, userId);
    }

    @Test
    @DisplayName("getEstimateList: 견적 리스트 조회 성공 시 200 OK + 응답 바디 반환")
    void getEstimateList_success() {

        Long projectId = 1L;
        Long clientCompanyId = 2L;
        String keyword = "검색어";
        Estimate.Status status = Estimate.Status.COMPLETED;
        Integer limit = 50;

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        Long userId = 99L;
        given(userDetails.getUserId()).willReturn(userId);

        ContractEstimateResponseDto item = mock(ContractEstimateResponseDto.class);
        List<ContractEstimateResponseDto> expectedList = List.of(item);

        given(contractService.getEstimateList(
                projectId, clientCompanyId, keyword, status, limit, userId
        )).willReturn(expectedList);

        ResponseEntity<List<ContractEstimateResponseDto>> result =
                contractController.getEstimateList(
                        projectId, clientCompanyId, keyword, status, limit, userDetails
                );

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(expectedList);

        verify(contractService).getEstimateList(
                projectId, clientCompanyId, keyword, status, limit, userId
        );
    }

    @Test
    @DisplayName("getEstimateDetail: 견적 상세 조회 성공 시 200 OK + 응답 바디 반환")
    void getEstimateDetail_success() {

        Long estimateId = 10L;

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        Long userId = 99L;
        given(userDetails.getUserId()).willReturn(userId);

        ContractEstimateDetailResponseDto responseDto =
                mock(ContractEstimateDetailResponseDto.class);

        given(contractService.getEstimateDetail(estimateId, userId))
                .willReturn(responseDto);

        ResponseEntity<ContractEstimateDetailResponseDto> result =
                contractController.getEstimateDetail(estimateId, userDetails);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(responseDto);

        verify(contractService).getEstimateDetail(estimateId, userId);
    }

    @Test
    @DisplayName("getContractList: 계약 리스트 조회 성공 시 200 OK + 페이지 DTO 반환")
    void getContractList_success() {

        Long projectId = 1L;
        Long clientCompanyId = 2L;
        String keyword = "검색";
        Contract.Status status = Contract.Status.COMPLETED;
        LocalDate contractDate = LocalDate.of(2025, 1, 1);
        int page = 3;
        int size = 50;

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        Long userId = 999L;
        given(userDetails.getUserId()).willReturn(userId);

        ContractPageResponseDto<ContractListResponseDto> responseDto =
                mock(ContractPageResponseDto.class);

        given(contractService.getContractList(
                projectId,
                clientCompanyId,
                keyword,
                status,
                contractDate,
                page,
                size,
                userId
        )).willReturn(responseDto);

        ResponseEntity<ContractPageResponseDto<ContractListResponseDto>> result =
                contractController.getContractList(
                        projectId,
                        clientCompanyId,
                        keyword,
                        status,
                        contractDate,
                        page,
                        size,
                        userDetails
                );

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(responseDto);

        verify(contractService).getContractList(
                projectId,
                clientCompanyId,
                keyword,
                status,
                contractDate,
                page,
                size,
                userId
        );
    }

    @Test
    @DisplayName("getContractDetail: 계약 상세 조회 성공 시 200 OK + 응답 바디 반환")
    void getContractDetail_success() {

        // given
        Long contractId = 123L;

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        Long userId = 777L;
        given(userDetails.getUserId()).willReturn(userId);

        ContractDetailResponseDto responseDto = mock(ContractDetailResponseDto.class);

        given(contractService.getContractDetail(contractId, userId))
                .willReturn(responseDto);

        // when
        ResponseEntity<ContractDetailResponseDto> result =
                contractController.getContractDetail(contractId, userDetails);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(responseDto);

        verify(contractService).getContractDetail(contractId, userId);
    }

    @Test
    @DisplayName("completeContract: 계약 완료 성공 시 200 OK + 응답 바디 반환")
    void completeContract_success() {

        Long contractId = 55L;

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        Long userId = 999L;
        given(userDetails.getUserId()).willReturn(userId);

        ContractCompleteResponseDto responseDto = mock(ContractCompleteResponseDto.class);

        given(contractService.completeContract(contractId, userId))
                .willReturn(responseDto);

        ResponseEntity<ContractCompleteResponseDto> result =
                contractController.completeContract(contractId, userDetails);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(responseDto);

        verify(contractService).completeContract(contractId, userId);
    }

    @Test
    @DisplayName("deleteContract: 계약 삭제 성공 시 200 OK + 응답 바디 반환")
    void deleteContract_success() {

        Long contractId = 88L;

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        Long userId = 1234L;
        given(userDetails.getUserId()).willReturn(userId);

        ContractDeleteResponseDto responseDto = mock(ContractDeleteResponseDto.class);

        given(contractService.deleteContract(contractId, userId))
                .willReturn(responseDto);

        ResponseEntity<ContractDeleteResponseDto> result =
                contractController.deleteContract(contractId, userDetails);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(responseDto);

        verify(contractService).deleteContract(contractId, userId);
    }
}
