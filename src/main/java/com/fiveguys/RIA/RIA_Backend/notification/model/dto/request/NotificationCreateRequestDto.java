package com.fiveguys.RIA.RIA_Backend.notification.model.dto.request;

import com.fiveguys.RIA.RIA_Backend.notification.model.dto.context.NotificationContext;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationCreateRequestDto {

    private Long senderId;
    private Long receiverId;
    private NotificationTargetType targetType;
    private NotificationTargetAction targetAction;
    private Long targetId;
    private final NotificationContext context; // Type에 맞는 Key-Value를 주기 위함
}
