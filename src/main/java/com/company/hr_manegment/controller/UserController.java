package com.company.hr_manegment.controller;

import com.company.hr_manegment.entity.User;
import com.company.hr_manegment.payload.ApiResponse;
import com.company.hr_manegment.payload.UserDto;
import com.company.hr_manegment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;
    @PreAuthorize(value = "hasRole('DIRECTOR')")
    @PostMapping("/manager")
    public HttpEntity<?> createManager(@RequestBody UserDto userDto) {
        ApiResponse apiResponse = userService.createManager(userDto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 409).body(apiResponse);
    }
    @PreAuthorize(value = "hasRole('DIRECTOR')")
    @PostMapping("/hr_manager")
    public HttpEntity<?> createHrManager(@RequestBody UserDto userDto) {
        ApiResponse apiResponse = userService.createHrManager(userDto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 409).body(apiResponse);
    }
    @PreAuthorize(value = "hasRole('EMPLOYEE')")
    @PostMapping("/employee")
    public HttpEntity<?> createEmployee(@RequestBody UserDto userDto) {
        ApiResponse apiResponse = userService.createEmployee(userDto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 409).body(apiResponse);
    }
    @PreAuthorize(value = "hasAnyRole('DIRECTOR','HR_MANAGER')")
    @GetMapping("/manager")
    public HttpEntity<?> getManger(@RequestParam int page, @RequestParam int size) {
        List<User> users = userService.getManager(page, size);
        return ResponseEntity.ok(users);
    }
    @PreAuthorize(value = "hasAnyRole('DIRECTOR','HR_MANAGER')")
    @GetMapping("/employee")
    public HttpEntity<?> getEmployee(@RequestParam int page, @RequestParam int size) {
        List<User> users = userService.getEmployee(page, size);
        return ResponseEntity.ok(users);
    }
    @PreAuthorize(value = "hasAnyRole('DIRECTOR','HR_MANAGER')")
    @GetMapping("/employee/{id}")
    public HttpEntity<?> getEmployee(@PathVariable UUID id) {
        User user = userService.getEmployee(id);
        return ResponseEntity.status(user != null ? 200 : 409).body(user);
    }
    @PreAuthorize(value = "hasAnyRole('DIRECTOR','HR_MANAGER')")
    @GetMapping("/manager/{id}")
    public HttpEntity<?> getManager(@PathVariable UUID id) {
        User user = userService.getManager(id);
        return ResponseEntity.status(user != null ? 200 : 409).body(user);
    }

}
