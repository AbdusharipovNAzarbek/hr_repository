package com.company.hr_manegment.service;

import com.company.hr_manegment.entity.User;
import com.company.hr_manegment.payload.ApiResponse;
import com.company.hr_manegment.payload.LoginDto;
import com.company.hr_manegment.payload.PasswordDto;
import com.company.hr_manegment.repository.UserRepository;
import com.company.hr_manegment.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        return optionalUser.orElseThrow(() -> new UsernameNotFoundException(username + " Bunaqa username li user topilmadi"));
    }

    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()));
            User user = (User) authenticate.getPrincipal();

            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
            return new ApiResponse("Welcome", true, token);
        } catch (BadCredentialsException e) {
            return new ApiResponse("Login yoki parol noto'g'ri", false);
        }

    }

    public ApiResponse setPassword(PasswordDto passwordDto, String username, String emailCode) {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isEmpty()) {
            return new ApiResponse("Bunday foydalanuuvchi mavjud emas", false);
        }
        User user = optionalUser.get();
        String userEmailCode = user.getEmailCode();
        if (userEmailCode == null) {
            return new ApiResponse("Siz parolingizni avvalroq o'rnatgansiz ", false);
        }
        if (!userEmailCode.equals(emailCode)) {
            return new ApiResponse("Foydalanuvchi tasdiqlanmadi", false);
        }
        if (!passwordDto.getPassword().equals(passwordDto.getRePassword())) {
            return new ApiResponse("Parollar mos emas", false);
        }

        user.setPassword(passwordDto.getPassword());
        userRepository.save(user);
        return new ApiResponse("Parolingiz o'rnatildi", true);
    }
}
