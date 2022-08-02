package konkuk.nServer.security;

import konkuk.nServer.domain.user.domain.Password;
import konkuk.nServer.domain.user.domain.Storemanager;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final StoremanagerRepository storemanagerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByUsername 실행. email={}", email);

        Optional<User> user = userRepository.findByEmail(email);
        Optional<Storemanager> storemanager = storemanagerRepository.findByEmail(email);

        if (user.isPresent()) {
            log.info("student 로그인 입니다.");
            Password password = user.get().getPassword();
            return new PrincipalDetails(user.get(), password);
        } else if (storemanager.isPresent()) {
            log.info("storemanager 로그인 입니다.");
            String password = storemanager.get().getPassword();
            return new PrincipalDetails(storemanager.get(), new Password(password));
        } else throw new UsernameNotFoundException("해당 사용자를 찾을수 없습니다.:" + email);
    }

}
