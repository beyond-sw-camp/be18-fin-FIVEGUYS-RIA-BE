package com.fiveguys.RIA.RIA_Backend.notification.model.component;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.NotificationErrorCode;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.Notification;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetType;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class NotificationValidator {

    // 알림 접근 권한 검증
    public void validateReceiverAccess(Notification notification, User currentUser) {
        if (!notification.getReceiver().getId().equals(currentUser.getId())) {
            throw new CustomException(NotificationErrorCode.ACCESS_DENIED);
        }
    }

    // 알림 읽음 검증
    public void validateNotAlreadyRead(Notification notification) {
        if (notification.isRead()) {
            throw new CustomException(NotificationErrorCode.NOTIFICATION_ALREADY_READ);
        }
    }

    // 알림 삭제 여부 검증
    public void validateNotDeleted(Notification notification) {
        if (notification.isDeleted()) {
            throw new CustomException(NotificationErrorCode.NOTIFICATION_ALREADY_DELETED);
        }
    }

    // 알림 타입 검증 (targetType + targetAction)
    public void validateType(NotificationTargetType targetType, NotificationTargetAction targetAction) {
        if (targetType == null || targetAction == null) {
            throw new CustomException(NotificationErrorCode.TYPE_NOT_FOUND);
        }

        // ENUM 값이라 정확성 체크는 생략 가능
    }
}
