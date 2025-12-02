package com.fiveguys.RIA.RIA_Backend.storage.controller;

import com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones.PageResponse;
import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.request.StorageUploadRequestDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageDeleteResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageDownloadResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.dto.response.StorageUploadResponseDto;
import com.fiveguys.RIA.RIA_Backend.storage.model.entity.Storage;
import com.fiveguys.RIA.RIA_Backend.storage.model.service.StorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * StorageController 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
class StorageControllerTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private StorageController storageController;

    @Test
    @DisplayName("getStorages: 전체 파일 목록 조회 성공")
    void getStorages_success() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        CustomUserDetails loginUser = mock(CustomUserDetails.class);
        Long userId = 1L;
        given(loginUser.getUserId()).willReturn(userId);

        StorageResponseDto dto = StorageResponseDto.builder()
                                                   .fileId(100L)
                                                   .originalName("test.pdf")
                                                   .employeeNo("E001")
                                                   .mimeType("application/pdf")
                                                   .size(1024L)
                                                   .createdAt(LocalDateTime.now())
                                                   .canEdit(true)
                                                   .canDelete(true)
                                                   .build();

        List<StorageResponseDto> content = List.of(dto);
        Page<StorageResponseDto> page = new PageImpl<>(content, pageable, content.size());

        given(storageService.getStorages(pageable, userId)).willReturn(page);

        // when
        ResponseEntity<PageResponse<StorageResponseDto>> result =
                storageController.getStorages(pageable, loginUser);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        PageResponse<StorageResponseDto> body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getContent()).containsExactly(dto);
        assertThat(body.getPage()).isEqualTo(pageable.getPageNumber());
        assertThat(body.getSize()).isEqualTo(pageable.getPageSize());
        assertThat(body.getTotalElements()).isEqualTo(1L);
        assertThat(body.getTotalPages()).isEqualTo(1);
        assertThat(body.isFirst()).isTrue();
        assertThat(body.isLast()).isTrue();

        verify(storageService).getStorages(pageable, userId);
    }

    @Test
    @DisplayName("createUploadUrl: 업로드 URL 생성 성공")
    void createUploadUrl_success() {
        // given
        CustomUserDetails loginUser = mock(CustomUserDetails.class);
        Long userId = 2L;
        given(loginUser.getUserId()).willReturn(userId);

        StorageUploadRequestDto request = StorageUploadRequestDto.builder()
                                                                 .originalName("sample.png")
                                                                 .mimeType("image/png")
                                                                 .size(2048L)
                                                                 .type(Storage.StorageType.PROPOSAL)
                                                                 .build();

        StorageUploadResponseDto responseDto = StorageUploadResponseDto.builder()
                                                                       .fileId(10L)
                                                                       .uploadUrl("https://s3/upload-url")
                                                                       .s3Key("storages/abc-123.png")
                                                                       .originalName("sample.png")
                                                                       .mimeType("image/png")
                                                                       .build();

        given(storageService.createUploadUrl(request, userId)).willReturn(responseDto);

        // when
        ResponseEntity<StorageUploadResponseDto> result =
                storageController.createUploadUrl(request, loginUser);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).isEqualTo(responseDto);

        verify(storageService).createUploadUrl(request, userId);
    }

    @Test
    @DisplayName("deleteFile: 파일 삭제 성공 시 서비스 호출 및 성공 메시지 반환")
    void deleteFile_success() {
        // given
        Long fileId = 50L;
        CustomUserDetails loginUser = mock(CustomUserDetails.class);
        Long userId = 3L;
        given(loginUser.getUserId()).willReturn(userId);

        // when
        ResponseEntity<StorageDeleteResponseDto> result =
                storageController.deleteFile(fileId, loginUser);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        StorageDeleteResponseDto body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getMessage()).isEqualTo("파일이 정상적으로 삭제되었습니다.");

        verify(storageService).deleteFile(fileId, userId);
    }

    @Test
    @DisplayName("getDownload: 다운로드 URL 생성 성공")
    void getDownload_success() {
        // given
        Long fileId = 77L;
        CustomUserDetails loginUser = mock(CustomUserDetails.class);
        Long userId = 4L;
        given(loginUser.getUserId()).willReturn(userId);

        StorageDownloadResponseDto responseDto = StorageDownloadResponseDto.builder()
                                                                           .fileId(fileId)
                                                                           .originalName("report.xlsx")
                                                                           .mimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                                                                           .downloadUrl("https://s3/download-url")
                                                                           .build();

        given(storageService.createDownloadUrl(fileId, userId)).willReturn(responseDto);

        // when
        ResponseEntity<StorageDownloadResponseDto> result =
                storageController.getDownload(fileId, loginUser);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).isEqualTo(responseDto);

        verify(storageService).createDownloadUrl(fileId, userId);
    }

    @Test
    @DisplayName("getMyFiles: 내가 올린 파일 목록 조회 성공")
    void getMyFiles_success() {
        // given
        // 👉 헷갈리지 않게 0페이지(첫 페이지)로 맞춤
        Pageable pageable = PageRequest.of(0, 5);

        CustomUserDetails loginUser = mock(CustomUserDetails.class);
        Long userId = 5L;
        given(loginUser.getUserId()).willReturn(userId);

        StorageResponseDto dto1 = StorageResponseDto.builder()
                                                    .fileId(1L)
                                                    .originalName("my-proposal.pdf")
                                                    .employeeNo("E002")
                                                    .mimeType("application/pdf")
                                                    .size(4096L)
                                                    .createdAt(LocalDateTime.now())
                                                    .canEdit(true)
                                                    .canDelete(true)
                                                    .build();

        StorageResponseDto dto2 = StorageResponseDto.builder()
                                                    .fileId(2L)
                                                    .originalName("my-estimate.xlsx")
                                                    .employeeNo("E002")
                                                    .mimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                                                    .size(8192L)
                                                    .createdAt(LocalDateTime.now())
                                                    .canEdit(true)
                                                    .canDelete(true)
                                                    .build();

        List<StorageResponseDto> content = List.of(dto1, dto2);

        // 👉 전체 개수 = 2로 가정 (content.size())
        Page<StorageResponseDto> page =
                new PageImpl<>(content, pageable, content.size());

        given(storageService.getMyStorages(pageable, userId)).willReturn(page);

        // when
        ResponseEntity<PageResponse<StorageResponseDto>> result =
                storageController.getMyFiles(pageable, loginUser);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        PageResponse<StorageResponseDto> body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getContent()).containsExactly(dto1, dto2);
        assertThat(body.getPage()).isEqualTo(pageable.getPageNumber());
        assertThat(body.getSize()).isEqualTo(pageable.getPageSize());

        // ✅ 전체 개수 = 2
        assertThat(body.getTotalElements()).isEqualTo(2L);

        // ✅ 페이지 사이즈 5, 전체 2 → 총 페이지 수 1
        assertThat(body.getTotalPages()).isEqualTo(1);

        // ✅ 0페이지이므로 first = true, last = true
        assertThat(body.isFirst()).isTrue();
        assertThat(body.isLast()).isTrue();

        verify(storageService).getMyStorages(pageable, userId);
    }
}
