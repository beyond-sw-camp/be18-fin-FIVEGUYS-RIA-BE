package com.fiveguys.RIA.RIA_Backend.event.listener;

import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.event.proposal.ProposalCreateEvent;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.context.SalesNotificationContext;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetType;
import com.fiveguys.RIA.RIA_Backend.notification.model.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProposalNotificationListener {

    private final NotificationService notificationService;

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 별도 트랜잭션
    public void handleProposalCreated(ProposalCreateEvent event) {

        // Proposal 정보
        Proposal proposal = event.getProposal();

        // SalesNotificationContext 생성
        SalesNotificationContext context = SalesNotificationContext.builder()
                .notificationTargetType(NotificationTargetType.PROPOSAL)
                .notificationTargetAction(NotificationTargetAction.CREATED)
                .targetId(proposal.getProposalId())
                .message("새 제안이 생성되었습니다: " + proposal.getTitle())
                .build();

        // 알림 생성
        notificationService.createNotification(
                event.getSenderId(),           // sender
                event.getReceiverId(),         // receiver
                NotificationTargetType.PROPOSAL,
                NotificationTargetAction.CREATED,
                proposal.getProposalId(),
                context
        );
    }
}
