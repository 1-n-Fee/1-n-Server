package konkuk.nServer.domain.account.service;

import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSenderImpl javaMailSender;

    public void mailSend(HttpSession session, String userEmail) {
        try {
            MailHandler mailHandler = new MailHandler(javaMailSender);
            int result = 100000 + new Random(System.currentTimeMillis()).nextInt(900000);

            mailHandler.setTo(userEmail + "@konkuk.ac.kr");
            mailHandler.setSubject("1/n : 인증번호입니다.");
            mailHandler.setText("<p>인증번호 : " + result + "<p>", true);
            mailHandler.send();

            session.setMaxInactiveInterval(3 * 60); // 초 단위 - 3분
            session.setAttribute(userEmail, result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ExceptionEnum.FAIL_EMAIL_SEND);
        }
    }

    public boolean emailCertification(HttpSession session, String userEmail, int inputCode) {
        Object generationCode = session.getAttribute(userEmail);
        if (generationCode == null) {
            throw new ApiException(ExceptionEnum.NO_SESSION_CODE);
        }

        if ((int) generationCode == inputCode) {
            session.removeAttribute(userEmail);
            return true;
        }
        return false;
    }
}
