package konkuk.nServer.domain.user.service;

import konkuk.nServer.domain.user.domain.Storemanager;
import konkuk.nServer.domain.user.dto.requestForm.StoremanagerSignup;
import konkuk.nServer.domain.user.repository.*;
import konkuk.nServer.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StoremanagerService {

    private final StoremanagerRepository storemanagerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void signup(StoremanagerSignup form) {

        Storemanager storemanager = Storemanager.builder()
                .password(passwordEncoder.encode(form.getPassword()))
                .storeRegistrationNumber(form.getStoreRegistrationNumber())
                .name(form.getName())
                .phone(form.getPhone())
                .email(form.getEmail())
                .build();

        storemanagerRepository.save(storemanager);
    }

}
