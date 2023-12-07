package com.intesoft.syncworks.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PasswordUpdateDto {
    private String oldPassword;
    private String newPassword;
    
}
