package konkuk.nServer.domain.account.service;

import konkuk.nServer.domain.account.domain.EmailCode;
import konkuk.nServer.domain.account.repository.EmailCodeRepository;
import konkuk.nServer.domain.storemanager.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSenderImpl javaMailSender;
    private final UserRepository userRepository;
    private final StoremanagerRepository storemanagerRepository;
    private final EmailCodeRepository emailCodeRepository;

    @Value("${email.code.expiration_time}")
    private Long expirationTime;

    public void mailSend(String userEmail) {
        validateEmail(userEmail);
        try {
            MailHandler mailHandler = new MailHandler(javaMailSender);
            String code = Integer.toString(100000 + new Random(System.currentTimeMillis()).nextInt(900000));

            mailHandler.setTo(userEmail);
            mailHandler.setSubject("1/n : 인증번호입니다.");
            mailHandler.setText("<p>인증번호 : " + code + "<p>", true);
            mailHandler.send();

            emailCodeRepository.findByEmail(userEmail)
                    .ifPresent(emailCodeRepository::delete);

            emailCodeRepository.save(new EmailCode(userEmail, code, expirationTime));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ExceptionEnum.FAIL_EMAIL_SEND);
        }
    }

    public boolean emailCertification(String userEmail, String code) {
        validateEmail(userEmail);

        Optional<EmailCode> optionalEmailCode = emailCodeRepository.findByEmail(userEmail);
        if (optionalEmailCode.isPresent()) {
            EmailCode emailCode = optionalEmailCode.get();
            emailCodeRepository.delete(emailCode);

            if (emailCode.getExpireDateTime().isBefore(LocalDateTime.now())) return false;

            return Objects.equals(emailCode.getCode(), code);
        } else return false;

    }

    private void validateEmail(String email) {
        String regx = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regx);
        if (!pattern.matcher(email).matches())
            throw new ApiException(ExceptionEnum.INVALID_EMAIL_DOMAIN);

        if (userRepository.existsByEmail(email))
            throw new ApiException(ExceptionEnum.DUPLICATE_EMAIL);

        if (storemanagerRepository.existsByEmail(email))
            throw new ApiException(ExceptionEnum.DUPLICATE_EMAIL);
    }
}
