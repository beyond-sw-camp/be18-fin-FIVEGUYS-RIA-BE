package com.fiveguys.RIA.RIA_Backend.notification.model.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ReadNotificationResponseDto extends BaseNotificationResponseDto {

    private final boolean isReadEvent; // 읽음 이벤트임을 표시
}
