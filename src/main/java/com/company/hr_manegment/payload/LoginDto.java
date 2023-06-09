package com.company.hr_manegment.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginDto {
    @Email
    private String username;
    @NotNull
    private String password;
}
