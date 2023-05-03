package com.company.hr_manegment.service;

import com.company.hr_manegment.config.SpringSecurityAuditingAware;
import com.company.hr_manegment.entity.Task;
import com.company.hr_manegment.entity.User;
import com.company.hr_manegment.entity.enums.RoleName;
import com.company.hr_manegment.entity.enums.TaskStatus;
import com.company.hr_manegment.payload.ApiResponse;
import com.company.hr_manegment.payload.StatusDto;
import com.company.hr_manegment.payload.TaskDto;
import com.company.hr_manegment.repository.TaskRepository;
import com.company.hr_manegment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service

public class TaskService {

    @Autowired
    TaskRepository taskRepository;
    @Autowired

    UserRepository userRepository;
    @Autowired
    SpringSecurityAuditingAware springSecurityAuditingAware;
    @Autowired
    JavaMailSender javaMailSender;

    public List<Task> getAllTasks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAll(pageable).getContent();
    }

    public List<Task> getTasksForExecutor(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAllByUsersId(getCurrenUser().getId(), pageable).getContent();
    }

    public Task getOneTask(Integer id) {
        return taskRepository.findByIdAndUsersId(id, getCurrenUser().getId()).orElse(null);
    }

    public ApiResponse createTaskForEmployee(TaskDto taskDto) {

        Optional<User> optionalUser = userRepository.findById(taskDto.getExecutorId());
        if (optionalUser.isEmpty()) {
            return new ApiResponse("Bunday xodim mavjud emas", false);
        }
        User user = optionalUser.get();
        if (!user.getRoles().contains(RoleName.EMPLOYEE)) {
            return new ApiResponse("Siz bu hodimga vazifa biriktirish huquqiga ega emassiz", false);
        }


        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setDeadline(taskDto.getDeadline());
        task.setResponsible(getCurrenUser());
        Set<User> users = task.getUsers();
        users.add(user);
        task.setUsers(users);

        taskRepository.save(task);
        sendMessageForExecuter(user.getEmail(), task.getName(), task.getDeadline());
        return new ApiResponse("Vazifa" + user.getFirstName() + " ga xodimga biriltirildi", true);

    }


    public ApiResponse createTaskForManager(TaskDto taskDto) {
        Optional<UUID> optionalResponsible = springSecurityAuditingAware.getCurrentAuditor();
        if (optionalResponsible.isEmpty()) {
            return new ApiResponse("Siz tizimga kirmagansiz", false);
        }

        Optional<User> optionalUser = userRepository.findById(taskDto.getExecutorId());
        if (optionalUser.isEmpty()) {
            return new ApiResponse("Bunday xodim mavjud emas", false);
        }
        User user = optionalUser.get();
        if (!user.getRoles().contains(RoleName.MANAGER)) {
            return new ApiResponse("Siz bu hodimga vazifa biriktirish huquqiga ega emassiz", false);
        }


        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setDeadline(taskDto.getDeadline());
        task.setResponsible(getCurrenUser());
        Set<User> users = task.getUsers();
        users.add(user);
        task.setUsers(users);

        taskRepository.save(task);
        sendMessageForExecuter(user.getEmail(), task.getName(), task.getDeadline());
        return new ApiResponse("Vazifa" + user.getFirstName() + " ga xodimga biriltirildi", true);

    }


    public boolean sendMessageForExecuter(String email, String taskName, Timestamp deadline) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("noReply@hr.com");
            mailMessage.setSubject("Topshiriq xabari");
            mailMessage.setText("Sizga : " + taskName + " nomli vazifa biriktirildi\n" +
                    "Deadline: " + deadline + " .");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean sendMessageForResponsible(String email, String taskName) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("noReply@hr.com");
            mailMessage.setSubject("Topshiriq bajarilganligi xaqidagi xabar");
            mailMessage.setText(taskName + " nomli vazifa Bajarildi\n");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User getCurrenUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public ApiResponse editTaskStatus(Integer id, StatusDto statusDto) {
        Optional<Task> optionalTask = taskRepository.findByIdAndUsersId(id, getCurrenUser().getId());
        if (optionalTask.isEmpty()) {
            return new ApiResponse("Bunday vazifa topilmadi", false);
        }
        Task task = optionalTask.get();

        if (statusDto.getStatusId() == 2) {
            task.setStatus(TaskStatus.JARAYONDA);
            taskRepository.save(task);
            return new ApiResponse("Muvvafaqiyatli o'zgartirildi", true);
        } else if (statusDto.getStatusId() == 3) {
            task.setStatus(TaskStatus.BAJARILGAN);
            taskRepository.save(task);
            sendMessageForResponsible(task.getResponsible().getEmail(), task.getName());
            return new ApiResponse("Muvvafaqiyatli o'zgartirildi", true);

        }
        return new ApiResponse("Xatolik", false);
    }
}
