package com.fiveguys.RIA.RIA_Backend.notification.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.context.NotificationContext;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.context.SalesNotificationContext;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.request.NotificationCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.BaseNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.DeleteNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.ReadNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.service.NotificationService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    @DisplayName("createNotification: 알림 생성 성공 시 200 OK + 생성된 알림 반환")
    void createNotification_success() {

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        given(userDetails.getUserId()).willReturn(2L);

        NotificationCreateRequestDto requestDto = mock(NotificationCreateRequestDto.class);
        given(requestDto.getReceiverId()).willReturn(5L);

        NotificationContext context = mock(SalesNotificationContext.class);
        given(requestDto.getContext()).willReturn(context);

        BaseNotificationResponseDto responseDto = mock(BaseNotificationResponseDto.class);

        given(notificationService.createNotification(2L, 5L, context))
                .willReturn(responseDto);

        ResponseEntity<BaseNotificationResponseDto> result =
                notificationController.createNotification(requestDto, userDetails);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(responseDto);

        verify(notificationService).createNotification(2L, 5L, context);
    }

    @Test
    @DisplayName("getNotificationList: 알림 리스트 조회 성공 시 200 OK + 리스트 반환")
    void getNotificationList_success() {

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        Long userId = 2L;
        given(userDetails.getUserId()).willReturn(userId);

        BaseNotificationResponseDto item1 = mock(BaseNotificationResponseDto.class);
        BaseNotificationResponseDto item2 = mock(BaseNotificationResponseDto.class);
        List<BaseNotificationResponseDto> expectedList = List.of(item1, item2);

        given(notificationService.getNotifications(userId))
                .willReturn(expectedList);

        ResponseEntity<List<BaseNotificationResponseDto>> result =
                notificationController.getNotificationList(userDetails);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(expectedList);

        verify(notificationService).getNotifications(userId);
    }

    @Test
    @DisplayName("readNotification: 단일 알림 읽음 처리 성공 시 200 OK + 응답 DTO 반환")
    void readNotification_success() {

        Long notificationId = 10L;

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        Long userId = 2L;
        given(userDetails.getUserId()).willReturn(userId);

        ReadNotificationResponseDto responseDto = mock(ReadNotificationResponseDto.class);

        given(notificationService.readNotification(notificationId, userId))
                .willReturn(responseDto);

        ResponseEntity<ReadNotificationResponseDto> result =
                notificationController.readNotification(notificationId, userDetails);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(responseDto);

        verify(notificationService).readNotification(notificationId, userId);
    }

    @Test
    @DisplayName("readAllNotifications: 전체 알림 읽음 처리 성공 시 200 OK + 응답 리스트 반환")
    void readAllNotifications_success() {

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        Long userId = 2L;
        given(userDetails.getUserId()).willReturn(userId);

        ReadNotificationResponseDto item1 = mock(ReadNotificationResponseDto.class);
        ReadNotificationResponseDto item2 = mock(ReadNotificationResponseDto.class);
        List<ReadNotificationResponseDto> responseList = List.of(item1, item2);

        given(notificationService.readAllNotifications(userId))
                .willReturn(responseList);

        ResponseEntity<List<ReadNotificationResponseDto>> result =
                notificationController.readAllNotifications(userDetails);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(responseList);

        verify(notificationService).readAllNotifications(userId);
    }

    @Test
    @DisplayName("deleteNotification: 단일 알림 삭제 성공 시 200 OK + 응답 DTO 반환")
    void deleteNotification_success() {

        Long notificationId = 10L;

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        Long userId = 2L;
        given(userDetails.getUserId()).willReturn(userId);

        DeleteNotificationResponseDto responseDto = mock(DeleteNotificationResponseDto.class);

        given(notificationService.deleteNotification(notificationId, userId))
                .willReturn(responseDto);

        ResponseEntity<DeleteNotificationResponseDto> result =
                notificationController.deleteNotification(notificationId, userDetails);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(responseDto);

        verify(notificationService).deleteNotification(notificationId, userId);
    }

    @Test
    @DisplayName("deleteAllNotification: 전체 알림 삭제 성공 시 200 OK + 응답 리스트 반환")
    void deleteAllNotification_success() {

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        Long userId = 2L;
        given(userDetails.getUserId()).willReturn(userId);

        DeleteNotificationResponseDto item1 = mock(DeleteNotificationResponseDto.class);
        DeleteNotificationResponseDto item2 = mock(DeleteNotificationResponseDto.class);
        List<DeleteNotificationResponseDto> responseList = List.of(item1, item2);

        given(notificationService.deleteAllNotification(userId))
                .willReturn(responseList);

        ResponseEntity<List<DeleteNotificationResponseDto>> result =
                notificationController.deleteAllNotification(userDetails);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(responseList);

        verify(notificationService).deleteAllNotification(userId);
    }

}
