package com.fiveguys.RIA.RIA_Backend.notification.model.component;

import com.fiveguys.RIA.RIA_Backend.notification.model.dto.response.BaseNotificationResponseDto;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.Notification;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
