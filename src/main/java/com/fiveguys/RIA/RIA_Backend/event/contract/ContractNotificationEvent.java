package com.fiveguys.RIA.RIA_Backend.event.contract;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ContractNotificationEvent extends ApplicationEvent {
    private final Long senderId;
    private final String senderName;
    private final String senderRole;
    private final Long receiverId;
    private final Contract contract;
    private final NotificationTargetAction targetAction;

    public ContractNotificationEvent(
            Object source,
            Long senderId,
            String senderName,
            String senderRole,
            Long receiverId,
            Contract contract,
            NotificationTargetAction targetAction
    ) {
        super(source);
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderRole = senderRole;
        this.receiverId = receiverId;
        this.contract = contract;
        this.targetAction = targetAction;
    }
}
