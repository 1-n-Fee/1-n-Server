package com.example.demo.repository;

import com.example.demo.domain.OAuth2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuth2Repository extends JpaRepository<OAuth2, Long> {
    Optional<OAuth2> findByKakao(String kakao);

    Optional<OAuth2> findByUserId(Long userId);
}
