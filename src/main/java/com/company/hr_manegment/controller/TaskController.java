package com.company.hr_manegment.controller;

import com.company.hr_manegment.entity.Task;
import com.company.hr_manegment.payload.ApiResponse;
import com.company.hr_manegment.payload.StatusDto;
import com.company.hr_manegment.payload.TaskDto;
import com.company.hr_manegment.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping("/getTasks")
    public HttpEntity<?> getTasksForExecutor(@RequestParam int page, @RequestParam int size) {
        List<Task> tasks = taskService.getTasksForExecutor(page, size);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/getTasks/{id}")
    public HttpEntity<?> getOneTask(@PathVariable Integer id) {
        Task task = taskService.getOneTask(id);
        return ResponseEntity.status(task != null ? 200 : 404).body(task);
    }
@PreAuthorize(value = "hasRole('MANAGER')")
    @PostMapping("/create_task_employee")
    public HttpEntity<?> createTaskForEmployee(@RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskService.createTaskForEmployee(taskDto);
        return ResponseEntity.status(apiResponse.isStatus() ? 202 : 409).body(apiResponse);
    }
    @PreAuthorize(value = "hasRole('DIRECTOR')")
    @PostMapping("/create_task_manager")
    public HttpEntity<?> createTaskForManager(@RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskService.createTaskForManager(taskDto);
        return ResponseEntity.status(apiResponse.isStatus() ? 202 : 409).body(apiResponse);
    }
    @PreAuthorize(value = "hasAnyRole('MANAGER','EMPLOYEE')")
    @PutMapping("/edit{id}")
    public HttpEntity<?> editTaskStatus(@PathVariable Integer id, @RequestBody StatusDto statusDto) {
        ApiResponse apiResponse = taskService.editTaskStatus(id, statusDto);
        return ResponseEntity.status(apiResponse.isStatus() ? 203 : 409).body(apiResponse);
    }

}
