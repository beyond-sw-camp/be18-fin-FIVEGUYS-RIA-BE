package com.fiveguys.RIA.RIA_Backend.notification.model.dto.context;

import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetType;

// 마커 인터페이스
public interface NotificationContext {
    // 과거의 잔재
    // 나중에도 안쓰면 리팩토링 예정

    NotificationTargetType getTargetType();
    NotificationTargetAction getTargetAction();
    Long getTargetId();
    String getMessage();
}