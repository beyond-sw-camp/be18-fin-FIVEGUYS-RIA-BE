package com.fiveguys.RIA.RIA_Backend.event.project;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProjectNotificationEvent extends ApplicationEvent {
    private final Long senderId;
    private final String senderName;
    private final String senderRole;
    private final Long receiverId;
    private final Project project;
    private final NotificationTargetAction targetAction;

    @Builder
    public ProjectNotificationEvent(Object source,
                                    Long senderId,
                                    String senderName,
                                    String senderRole,
                                    Long receiverId,
                                    Project project,
                                    NotificationTargetAction targetAction) {
        super(source);
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderRole = senderRole;
        this.receiverId = receiverId;
        this.project = project;
        this.targetAction = targetAction;
    }
}
