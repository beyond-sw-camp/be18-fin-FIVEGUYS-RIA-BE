package com.fiveguys.RIA.RIA_Backend.event.estimate;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EstimateNotificationEvent extends ApplicationEvent {
    private final Long senderId;
    private final String senderName;
    private final String senderRole;
    private final Long receiverId;
    private final Estimate estimate;
    private final NotificationTargetAction targetAction;

    public EstimateNotificationEvent(Object source, Long senderId, String senderName, String senderRole, Long receiverId, Estimate estimate, NotificationTargetAction targetAction) {
        super(source);
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderRole = senderRole;
        this.receiverId = receiverId;
        this.estimate = estimate;
        this.targetAction = targetAction;
    }
}
