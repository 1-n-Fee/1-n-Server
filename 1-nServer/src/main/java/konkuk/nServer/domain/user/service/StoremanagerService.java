package konkuk.nServer.domain.user.service;

import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.Storemanager;
import konkuk.nServer.domain.user.dto.requestForm.StoremanagerSignup;
import konkuk.nServer.domain.user.repository.StoremanagerRepository;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StoremanagerService {

    private final StoremanagerRepository storemanagerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void signup(StoremanagerSignup form) {
        Role role = convertRole(form.getRole());

        Storemanager storemanager = Storemanager.builder()
                .password(passwordEncoder.encode(form.getPassword()))
                .storeRegistrationNumber(form.getStoreRegistrationNumber())
                .name(form.getName())
                .phone(form.getPhone())
                .email(form.getEmail())
                .role(role)
                .build();

        storemanagerRepository.save(storemanager);
    }

    private Role convertRole(String role) {
        if (Objects.equals(role, "student")) return Role.ROLE_STUDENT;
        else if (Objects.equals(role, "storemanager")) return Role.ROLE_STOREMANAGER;
        else throw new ApiException(ExceptionEnum.INCORRECT_ROLE);
    }

}
