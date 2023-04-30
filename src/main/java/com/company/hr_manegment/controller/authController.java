package com.company.hr_manegment.controller;

import com.company.hr_manegment.payload.ApiResponse;
import com.company.hr_manegment.payload.LoginDto;
import com.company.hr_manegment.payload.PasswordDto;
import com.company.hr_manegment.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class authController {
    @Autowired
    AuthService authService;


    @PostMapping("/login")
    public HttpEntity<?> login(LoginDto loginDto) {
        ApiResponse apiResponse = authService.login(loginDto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 403).body(apiResponse);
    }

    @PostMapping("/setPassword")
    public HttpEntity<?> setPassword(@RequestBody PasswordDto passwordDto,
                                     @RequestParam String username,
                                     @RequestParam String emailCode) {
        ApiResponse apiResponse = authService.setPassword(passwordDto, username, emailCode);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);

    }

}
