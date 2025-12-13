package com.fiveguys.RIA.RIA_Backend.event.proposal;

import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProposalNotificationEvent extends ApplicationEvent {
    private final Long senderId;
    private final String senderName;
    private final String senderRole;
    private final Long receiverId;
    private final Proposal proposal;
    private final NotificationTargetAction targetAction;

    public ProposalNotificationEvent(Object source, Long senderId, String senderName, String senderRole, Long receiverId, Proposal proposal, NotificationTargetAction targetAction) {
        super(source);
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderRole = senderRole;
        this.receiverId = receiverId;
        this.proposal = proposal;
        this.targetAction = targetAction;
    }
}
