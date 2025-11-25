package com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String employeeNo;
    private String name;
    private String email;
    private String department;
    private String position;
    private String state;
    private Long roleId;

}
