package com.company.hr_manegment.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data

public class PasswordDto {
    @NotNull
    private String password;
    @NotNull
    private String rePassword;

}
