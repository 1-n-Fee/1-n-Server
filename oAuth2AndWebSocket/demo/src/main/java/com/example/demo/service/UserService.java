package com.example.demo.service;

import com.example.demo.domain.OAuth2;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.repository.OAuth2Repository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.dto.LoginDto;
import com.example.demo.service.dto.SignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OAuth2Repository oAuth2Repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User signup(SignupDto form) {
        Role role = Objects.equals(form.getRole(), "USER") ? Role.ROLE_USER : Role.ROLE_ADMIN;
        String password = passwordEncoder.encode(form.getPassword());
        User user = new User(form.getName(), form.getEmail(), password, role);
        userRepository.save(user);

        OAuth2 oAuth2 = new OAuth2(user);
        oAuth2Repository.save(oAuth2);

        return user;
    }
}
