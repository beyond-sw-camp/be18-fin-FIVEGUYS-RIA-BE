package com.fiveguys.RIA.RIA_Backend.notification.model.component;

import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.BaseNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.DeleteNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.ReadNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.Notification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    public BaseNotificationResponseDto toResponseDto(Notification n) {

        return BaseNotificationResponseDto.builder()
                .notificationId(n.getNotificationId())
                .senderId(n.getSender() != null ? n.getSender().getId() : null)
                .receiverId(n.getReceiver() != null ? n.getReceiver().getId() : null)

                .targetType(n.getTargetType())
                .targetAction(n.getTargetAction())
                .targetId(n.getTargetId())

                .message(n.getMessage())
                .isRead(n.isRead())
                .createdAt(n.getCreatedAt())
                .build();
    }

    public List<BaseNotificationResponseDto> toResponseDtoList(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toResponseDto)
                .toList();
    }

    public ReadNotificationResponseDto toReadDto(Notification n) {
        return ReadNotificationResponseDto.builder()
                .notificationId(n.getNotificationId())
                .senderId(n.getSender() != null ? n.getSender().getId() : null)
                .receiverId(n.getReceiver() != null ? n.getReceiver().getId() : null)
                .targetType(n.getTargetType())
                .targetAction(n.getTargetAction())
                .targetId(n.getTargetId())
                .message(n.getMessage())
                .isRead(n.isRead())
                .createdAt(n.getCreatedAt())
                .isReadEvent(true)
                .build();
    }

    public List<ReadNotificationResponseDto> toReadDtoList(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toReadDto)
                .toList();
    }

    public DeleteNotificationResponseDto toDeleteDto(Notification n) {
        return DeleteNotificationResponseDto.builder()
                .notificationId(n.getNotificationId())
                .senderId(n.getSender() != null ? n.getSender().getId() : null)
                .receiverId(n.getReceiver() != null ? n.getReceiver().getId() : null)
                .targetType(n.getTargetType())
                .targetAction(n.getTargetAction())
                .targetId(n.getTargetId())
                .message(n.getMessage())
                .isRead(n.isRead())
                .createdAt(n.getCreatedAt())
                .isDeleteEvent(true)
                .build();
    }

    public List<DeleteNotificationResponseDto> toDeleteDtoList(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toDeleteDto)
                .toList();
    }
}
