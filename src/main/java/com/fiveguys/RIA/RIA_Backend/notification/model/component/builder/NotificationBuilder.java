package com.fiveguys.RIA.RIA_Backend.notification.model.component.builder;

import com.fiveguys.RIA.RIA_Backend.notification.model.entity.Notification;
import com.fiveguys.RIA.RIA_Backend.notification.model.dto.context.NotificationContext;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;

public interface NotificationBuilder {

    Notification build(User sender, User receiver, NotificationContext context);
}
