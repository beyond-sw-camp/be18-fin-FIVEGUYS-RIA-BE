package com.fiveguys.RIA.RIA_Backend.notification.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.notification.model.component.NotificationLoader;
import com.fiveguys.RIA.RIA_Backend.notification.model.component.NotificationMapper;
import com.fiveguys.RIA.RIA_Backend.notification.model.component.NotificationValidator;
import com.fiveguys.RIA.RIA_Backend.notification.model.component.builder.NotificationBuilder;
import com.fiveguys.RIA.RIA_Backend.notification.model.component.builder.NotificationBuilderSelector;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.context.NotificationContext;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.request.NotificationCreateRequestDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.BaseNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.DeleteNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.ReadNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.Notification;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetType;
import com.fiveguys.RIA.RIA_Backend.notification.model.repository.NotificationRepository;
import com.fiveguys.RIA.RIA_Backend.notification.model.service.NotificationService;
import com.fiveguys.RIA.RIA_Backend.notification.model.service.NotificationSseService;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationLoader notificationLoader;
    private final NotificationValidator notificationValidator;
    private final NotificationMapper notificationMapper;
    private final NotificationBuilderSelector notificationBuilderSelector;
    private final NotificationSseService notificationSseService;

    // 알림 목록 조회
    @Override
    public List<BaseNotificationResponseDto> getNotifications(Long receiverId) {

        // 1. 사용자 로딩
        User receiver = notificationLoader.loadUser(receiverId);

        // 2. 알림 목록 로딩
        List<Notification> notifications = notificationLoader.loadNotifications(receiverId);

        // 3. 접근 권한 검증 + 삭제된 알림 제외
        List<Notification> validNotifications = notifications.stream()
                .filter(notification -> {
                    try {
                        notificationValidator.validateReceiverAccess(notification, receiver);
                        notificationValidator.validateNotDeleted(notification); // 삭제 여부 검증
                        return true; // 유효한 알림만
                    } catch (CustomException e) {
                        return false; // 예외 발생 시 건너뜀
                    }
                })
                .toList();

        // 4. DTO 변환
        return notificationMapper.toResponseDtoList(validNotifications);
    }

    // 알림 단건 조회 (클릭?)
    @Override
    @Transactional
    public BaseNotificationResponseDto getNotification(Long notificationId, Long receiverId) {

        // 1. 사용자 로딩
        User receiver = notificationLoader.loadUser(receiverId);

        // 2. 알림 로딩
        Notification notification = notificationLoader.loadNotification(notificationId);

        // 3. 접근 권한 검증
        notificationValidator.validateReceiverAccess(notification, receiver);

        // 4. 삭제 여부 검증
        notificationValidator.validateNotDeleted(notification);

        // 5. 읽음 처리
        if (!notification.isRead()) {
            notification.markAsRead();
        }

        // 6. DTO 변환 후 반환
        return notificationMapper.toResponseDto(notification);
    }

    // 알림 생성
    @Override
    public BaseNotificationResponseDto createNotification(Long senderId, Long receiverId, NotificationContext context) {

        // 1. 발신자, 수신자 로딩 + Context 검증 추가 예정 (제견계매프 + 스케줄러? 완성되면)
        User sender = notificationLoader.loadUser(senderId);
        User receiver = notificationLoader.loadUser(receiverId);

        // 2. Builder 선택
        NotificationBuilder notificationBuilder = notificationBuilderSelector.selectBuilder(context.getTargetType());

        // 3. Notification 생성
        Notification notification = notificationBuilder.build(sender, receiver, context);

        // 4. DTO 변환
        BaseNotificationResponseDto responseDto = notificationMapper.toResponseDto(notification);

        // 5. 알림 저장
        notificationRepository.save(notification);

        // SSE 전송
        notificationSseService.sendNotification(receiver.getId(), responseDto);
        
        return responseDto;
    }

    @Override
    @Transactional
    public ReadNotificationResponseDto readNotification(Long notificationId, Long receiverId) {

        // 1. 사용자 로딩
        User receiver = notificationLoader.loadUser(receiverId);

        // 2. 알림 로딩
        Notification notification = notificationLoader.loadNotification(notificationId);

        // 3. 접근 권한 검증
        notificationValidator.validateReceiverAccess(notification, receiver);

        // 4. 삭제 여부 검증
        notificationValidator.validateNotDeleted(notification);

        // 5. 읽음 처리
        if (!notification.isRead()) {
            notification.markAsRead();
        }

        // 6. 저장
        notificationRepository.save(notification);

        // 7. DTO 변환
        ReadNotificationResponseDto readDto = notificationMapper.toReadDto(notification);

        // 8. SSE 전송
        notificationSseService.sendNotification(receiverId, readDto);

        return readDto;
    }

    @Override
    @Transactional
    public List<ReadNotificationResponseDto> readAllNotifications(Long receiverId) {

        // 1. 사용자 로딩
        User receiver = notificationLoader.loadUser(receiverId);

        // 2. 읽지 않은 알림 로딩
        List<Notification> notifications = notificationLoader.loadUnreadNotifications(receiverId);

        // 3. 접근 권한 검증 + 삭제된 알림 제외
        List<Notification> validNotifications = notifications.stream()
                .filter(notification -> {
                    try {
                        notificationValidator.validateReceiverAccess(notification, receiver);
                        notificationValidator.validateNotDeleted(notification);
                        return true; // 유효한 알림만
                    } catch (CustomException e) {
                        return false; // 예외 발생 시 건너뜀
                    }
                })
                .toList();

        // 4. 읽음 처리
        validNotifications.forEach(Notification::markAsRead);

        // 5. DB 저장
        notificationRepository.saveAll(validNotifications);

        // 6. DTO 변환
        List<ReadNotificationResponseDto> readDtoList = notificationMapper.toReadDtoList(validNotifications);

        // 7. SSE 전송
        readDtoList.forEach(readDto -> notificationSseService.sendNotification(receiverId, readDto));

        return readDtoList;
    }

    @Override
    @Transactional
    public DeleteNotificationResponseDto deleteNotification(Long notificationId, Long receiverId) {
        // 1. 사용자 로딩
        User receiver = notificationLoader.loadUser(receiverId);

        // 2. 알림 로딩
        Notification notification = notificationLoader.loadNotification(notificationId);

        // 3. 접근 권한 검증
        notificationValidator.validateReceiverAccess(notification, receiver);

        // 4. 삭제 여부 검증
        notificationValidator.validateNotDeleted(notification);

        // 5. 삭제 처리
        notification.softDelete();

        // 6. 저장
        notificationRepository.save(notification);

        // 7. DTO 변환
        DeleteNotificationResponseDto deleteDto = notificationMapper.toDeleteDto(notification);

        // 8. SSE 전송
        notificationSseService.sendNotification(receiverId, deleteDto);

        return deleteDto;
    }

    @Override
    @Transactional
    public List<DeleteNotificationResponseDto> deleteAllNotification(Long receiverId) {
        // 1. 사용자 로딩
        User user = notificationLoader.loadUser(receiverId);

        // 2. 알림 목록 로딩
        List<Notification> notifications = notificationLoader.loadNotifications(receiverId);

        // 3. 접근 권한 검증 + 삭제 안된 알림만
        List<Notification> validNotifications = notifications.stream()
                .filter(notification -> {
                    try {
                        notificationValidator.validateReceiverAccess(notification, user);
                        notificationValidator.validateNotDeleted(notification);
                        return true;
                    } catch (CustomException e) {
                        return false;
                    }
                })
                .toList();

        // 4. 삭제 처리
        validNotifications.forEach(Notification::softDelete);

        // 5. DB 저장
        notificationRepository.saveAll(validNotifications);

        // 6. DTO 변환
        List<DeleteNotificationResponseDto> deleteDtoList = notificationMapper.toDeleteDtoList(validNotifications);

        // 7. SSE 전송
        deleteDtoList.forEach(deleteDto -> notificationSseService.sendNotification(receiverId, deleteDto));

        return deleteDtoList;
    }
}
