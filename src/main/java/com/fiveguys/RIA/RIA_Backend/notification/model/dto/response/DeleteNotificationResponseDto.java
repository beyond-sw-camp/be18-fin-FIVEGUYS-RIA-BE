package com.fiveguys.RIA.RIA_Backend.notification.model.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class DeleteNotificationResponseDto extends BaseNotificationResponseDto {

    private final boolean isDeleteEvent; // 삭제 이벤트임을 표시
}
