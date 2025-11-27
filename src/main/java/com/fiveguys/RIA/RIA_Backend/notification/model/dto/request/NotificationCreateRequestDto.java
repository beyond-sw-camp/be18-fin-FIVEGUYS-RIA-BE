package com.fiveguys.RIA.RIA_Backend.notification.model.dto.request;

import com.fiveguys.RIA.RIA_Backend.notification.model.dto.context.NotificationContext;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationCreateRequestDto {

    private final Long senderId;
    private final Long receiverId;
    private final NotificationTargetType targetType;
    private final NotificationTargetAction targetAction;
    private final Long targetId;
    private final NotificationContext context; // Type에 맞는 Key-Value를 주기 위함
}
