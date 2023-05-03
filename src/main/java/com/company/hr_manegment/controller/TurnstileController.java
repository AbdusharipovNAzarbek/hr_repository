package com.company.hr_manegment.controller;

import com.company.hr_manegment.payload.ApiResponse;
import com.company.hr_manegment.payload.TurnstileDto;
import com.company.hr_manegment.service.TurnstileService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/turnstile")
public class TurnstileController {

    @Autowired
    TurnstileService turnstileService;

    @PostMapping("/entry")
    public HttpEntity<?> entry(@RequestBody TurnstileDto turnstileDto) {
        ApiResponse apiResponse = turnstileService.entry(turnstileDto);
        return ResponseEntity.status(apiResponse.isStatus() ? 202 : 409).body(apiResponse);
    }

    @PostMapping("/exit")
    public HttpEntity<?> exit(@RequestBody TurnstileDto turnstileDto) {
        ApiResponse apiResponse = turnstileService.exit(turnstileDto);
        return ResponseEntity.status(apiResponse.isStatus() ? 202 : 409).body(apiResponse);
    }
}
