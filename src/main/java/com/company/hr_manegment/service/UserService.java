package com.company.hr_manegment.service;

import com.company.hr_manegment.entity.Role;
import com.company.hr_manegment.entity.User;
import com.company.hr_manegment.entity.enums.RoleName;
import com.company.hr_manegment.payload.ApiResponse;
import com.company.hr_manegment.payload.UserDto;
import com.company.hr_manegment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JavaMailSender javaMailSender;

    public ApiResponse createManager(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isEmpty()) {
            return new ApiResponse("Bunday email oldin ro'yxatdan o'tgan", false);
        }
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleName.MANAGER));
        user.setRoles(roles);
        String emailCode = UUID.randomUUID().toString();
        user.setEmailCode(emailCode);
        userRepository.save(user);

        sendMessageToEmail(user.getEmail(), user.getEmailCode());

        return new ApiResponse("Foydalanuvchi ro'yxatdan o'tkazildi", true);

    }

    public ApiResponse createHrManager(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isEmpty()) {
            return new ApiResponse("Bunday email oldin ro'yxatdan o'tgan", false);
        }
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleName.HR_MANAGER));
        user.setRoles(roles);
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);

        sendMessageToEmail(user.getEmail(), user.getEmailCode());

        return new ApiResponse("Foydalanuvchi ro'yxatdan o'tkazildi", true);
    }

    public ApiResponse createEmployee(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isEmpty()) {
            return new ApiResponse("Bunday email oldin ro'yxatdan o'tgan", false);
        }
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleName.EMPLOYEE));
        user.setRoles(roles);
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);

        sendMessageToEmail(user.getEmail(), user.getEmailCode());

        return new ApiResponse("Foydalanuvchi ro'yxatdan o'tkazildi", true);
    }

    public boolean sendMessageToEmail(String email, String emailCode) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        try {
            mailMessage.setFrom("noReply@gmail.com");
            mailMessage.setTo(email);
            mailMessage.setSubject("Parol o'rnatish");
            mailMessage.setText("http://localhost:8080/api/auth/setPassword?email=" + email + "&emailCode=" + emailCode);
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<User> getManager(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Set<Role> roles = new HashSet<>();
        Role role = new Role(RoleName.MANAGER);
        roles.add(role);
        return userRepository.findAllByRoles(roles, pageable).getContent();
    }

    public List<User> getEmployee(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Set<Role> roles = new HashSet<>();
        Role role = new Role(RoleName.EMPLOYEE);
        roles.add(role);
        return userRepository.findAllByRoles(roles, pageable).getContent();
    }

    public User getEmployee(UUID id) {
        Set<Role> roles = new HashSet<>();
        Role role = new Role(RoleName.EMPLOYEE);
        roles.add(role);
        return userRepository.findByIdAndRoles(id, roles).orElse(null);

    }
    public User getManager(UUID id) {
        Set<Role> roles = new HashSet<>();
        Role role = new Role(RoleName.MANAGER);
        roles.add(role);
        return userRepository.findByIdAndRoles(id, roles).orElse(null);

    }
}