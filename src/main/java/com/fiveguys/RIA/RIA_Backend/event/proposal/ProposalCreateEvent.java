package com.fiveguys.RIA.RIA_Backend.event.proposal;

import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProposalCreateEvent extends ApplicationEvent {
    private final Long senderId;
    private final Long receiverId;
    private final Proposal proposal;

    public ProposalCreateEvent(Object source, Long senderId, Long receiverId, Proposal proposal) {
        super(source);
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.proposal = proposal;
    }
}
