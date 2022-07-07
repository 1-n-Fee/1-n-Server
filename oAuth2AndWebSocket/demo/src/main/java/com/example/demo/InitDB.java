package com.example.demo;

import com.example.demo.service.UserService;
import com.example.demo.service.dto.SignupDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitDB {

    private final UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() {
        log.info("initialize database");
        userService.signup(new SignupDto("홍길동", "asdf@asdf.com", "asdf@", "USER"));
    }


}