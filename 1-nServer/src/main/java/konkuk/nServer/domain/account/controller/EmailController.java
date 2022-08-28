package konkuk.nServer.domain.account.controller;

import konkuk.nServer.domain.account.dto.email.EmailCertification;
import konkuk.nServer.domain.account.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email")
    public void sendEmail(@RequestBody Map<String, String> map) {
        log.info("이메일 인증 요청. userEmail={}", map.get("userEmail"));
        emailService.mailSend(map.get("userEmail"));
    }

    @PostMapping("/email/certification")
    public Map<String, Boolean> emailCertification(@RequestBody EmailCertification form) {
        log.info("인증 코드. userEmail={}, code={}", form.getUserEmail(), form.getCode());
        boolean result = emailService.emailCertification(form.getUserEmail(), form.getCode());
        return Map.of("result", result);
    }
}
