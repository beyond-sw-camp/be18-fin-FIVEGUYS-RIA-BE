package com.fiveguys.RIA.RIA_Backend.notification.model.component.builder;

import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class NotificationBuilderSelector {

    private final Map<NotificationTargetType, NotificationBuilder> builderMap;

    public NotificationBuilderSelector(SalesNotificationBuilder salesNotificationBuilder) {
        builderMap = new EnumMap<>(NotificationTargetType.class);

        // 현재는 모든 타입을 Sales 빌더가 처리
        for (NotificationTargetType type : NotificationTargetType.values()) {
            builderMap.put(type, salesNotificationBuilder);
        }
    }

    public NotificationBuilder selectBuilder(NotificationTargetType type) {
        return builderMap.get(type);
    }
}
