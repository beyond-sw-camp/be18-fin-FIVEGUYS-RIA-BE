package com.fiveguys.RIA.RIA_Backend.event.project;

import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProjectNotificationEvent extends ApplicationEvent {
    private final Long senderId;
    private final Long receiverId;
    private final Long projectId;
    private final String title;
    private final NotificationTargetAction action;

    @Builder
    public ProjectNotificationEvent(Object source,   // 반드시 필요
                                    Long senderId,
                                    Long receiverId,
                                    Long projectId,
                                    String title,
                                    NotificationTargetAction action) {
        super(source);   // 부모 생성자 호출 필수
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.projectId = projectId;
        this.title = title;
        this.action = action;
    }
}
