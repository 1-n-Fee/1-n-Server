package konkuk.nServer.security;

import com.example.demo.domain.EmailPassword;
import com.example.demo.domain.User;
import com.example.demo.repository.EmailPasswordRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailService implements UserDetailsService {

    private final EmailPasswordRepository emailPasswordRepository;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByUsername 실행. email={}", email);

        EmailPassword emailPassword = emailPasswordRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을수 없습니다.:" + email));
        User user = userRepository.findById(emailPassword.getId())
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을수 없습니다.:" + email));

        return new PrincipalDetails(emailPassword, user);
    }


}
