package com.company.hr_manegment.service;

import com.company.hr_manegment.entity.Turnstile;
import com.company.hr_manegment.entity.User;
import com.company.hr_manegment.payload.ApiResponse;
import com.company.hr_manegment.payload.TurnstileDto;
import com.company.hr_manegment.repository.TurnstileRepository;
import com.company.hr_manegment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurnstileService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskService taskService;
    @Autowired
    TurnstileRepository turnstileRepository;

    public ApiResponse entry(TurnstileDto turnstileDto) {
        Optional<User> optionalUser = userRepository.findById(turnstileDto.getUser_id());
        if (optionalUser.isEmpty()) {
            return new ApiResponse("Bunday user mavjud emas", false);
        }
        User user = optionalUser.get();
        Turnstile turnstile = new Turnstile(turnstileDto.getDate_time(), user);
        turnstileRepository.save(turnstile);
        return new ApiResponse("Success", true);
    }
    public ApiResponse exit(TurnstileDto turnstileDto) {
        Optional<User> optionalUser = userRepository.findById(turnstileDto.getUser_id());
        if (optionalUser.isEmpty()) {
            return new ApiResponse("Bunday user mavjud emas", false);
        }
        User user = optionalUser.get();
        Turnstile turnstile = new Turnstile(turnstileDto.getDate_time(), user);
        turnstileRepository.save(turnstile);
        return new ApiResponse("Success", true);
    }
}
