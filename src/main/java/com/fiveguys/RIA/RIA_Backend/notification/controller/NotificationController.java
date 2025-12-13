package com.fiveguys.RIA.RIA_Backend.notification.controller;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.request.NotificationCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.BaseNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.DeleteNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.ReadNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<BaseNotificationResponseDto>> getNotificationList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<BaseNotificationResponseDto> responseDtoList =
                notificationService.getNotifications(userDetails.getUserId());

        return ResponseEntity.ok(responseDtoList);
    }

//    알림 클릭으로 변경 예정 -> 클릭 시 페이지가 넘어가던지, 상세정보가 뜨던지의 방식으로
//    @GetMapping("/{notificationId}")
//    public ResponseEntity<BaseNotificationResponseDto> getNotification(
//            @PathVariable Long notificationId,
//            @AuthenticationPrincipal CustomUserDetails userDetails
//    ) {
//        BaseNotificationResponseDto responseDto =
//                notificationService.getNotification(notificationId, userDetails.getUserId());
//
//        return ResponseEntity.ok(responseDto);
//    }

    @PostMapping
    public ResponseEntity<BaseNotificationResponseDto> createNotification(
            @RequestBody NotificationCreateRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BaseNotificationResponseDto responseDto =
                notificationService.createNotification(userDetails.getUserId(), requestDto.getReceiverId(), requestDto.getContext());

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{notificationId}")
    public ResponseEntity<ReadNotificationResponseDto> readNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ReadNotificationResponseDto responseDto =
                notificationService.readNotification(notificationId, userDetails.getUserId());

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping
    public ResponseEntity<List<ReadNotificationResponseDto>> readAllNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<ReadNotificationResponseDto> responseDtoList =
                notificationService.readAllNotifications(userDetails.getUserId());
        return ResponseEntity.ok(responseDtoList);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<DeleteNotificationResponseDto> deleteNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        DeleteNotificationResponseDto responseDto =
                notificationService.deleteNotification(notificationId, userDetails.getUserId());

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<List<DeleteNotificationResponseDto>> deleteAllNotification(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<DeleteNotificationResponseDto> responseDtoList =
                notificationService.deleteAllNotification(userDetails.getUserId());

        return ResponseEntity.ok(responseDtoList);
    }
}
