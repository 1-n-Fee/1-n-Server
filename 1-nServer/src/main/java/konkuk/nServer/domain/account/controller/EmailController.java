package konkuk.nServer.domain.account.controller;

import konkuk.nServer.domain.account.dto.email.EmailCertification;
import konkuk.nServer.domain.account.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email")
    public void sendEmail(HttpServletRequest request, @RequestBody Map<String, String> map) {
        log.info("이메일 인증 요청. userEmail={}", map.get("userEmail"));
        HttpSession session = request.getSession();
        emailService.mailSend(session, map.get("userEmail"));
    }

    @PostMapping("/email/certification")
    public Map<String, Boolean> emailCertification(HttpServletRequest request, @RequestBody EmailCertification form) {
        log.info("인증 코드. userEmail={}, code={}", form.getUserEmail(), form.getCode());
        HttpSession session = request.getSession();
        boolean result = emailService.emailCertification(session, form.getUserEmail(), Integer.parseInt(form.getCode()));
        return Map.of("result", result);
    }
}
