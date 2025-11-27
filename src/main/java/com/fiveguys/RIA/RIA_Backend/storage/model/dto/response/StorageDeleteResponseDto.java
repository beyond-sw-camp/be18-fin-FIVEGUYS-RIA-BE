package com.fiveguys.RIA.RIA_Backend.storage.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StorageDeleteResponseDto {

    private final boolean success;
    private final String message;
}
