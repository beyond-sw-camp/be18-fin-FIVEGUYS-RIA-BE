package com.fiveguys.RIA.RIA_Backend.notification.model.component;

import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.NotificationErrorCode;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.Notification;
import com.fiveguys.RIA.RIA_Backend.notification.model.repository.NotificationRepository;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationLoader {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    // 사용자 로딩
    public User loadUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(NotificationErrorCode.USER_NOT_FOUND));
    }

    // 알림 로딩
    public Notification loadNotification(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));
    }

    // 알림 목록 로딩
    public List<Notification> loadNotifications(Long receiverId) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(receiverId);
    }

    // 읽지 않은 알림 목록 로딩
    public List<Notification> loadUnreadNotifications(Long receiverId) {
        return notificationRepository.findByReceiverIdAndIsReadFalseOrderByCreatedAtDesc(receiverId);
    }
}
