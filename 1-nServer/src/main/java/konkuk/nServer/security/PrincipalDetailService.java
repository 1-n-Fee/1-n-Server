package konkuk.nServer.security;

import konkuk.nServer.domain.account.domain.Password;
import konkuk.nServer.domain.storemanager.domain.Storemanager;
import konkuk.nServer.domain.storemanager.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.User;
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
            log.info("student 로그인 시도");
            Password password = user.get().getPassword();
            return new PrincipalDetails(user.get().getId(), password, Role.ROLE_STUDENT);
        }
        else if (storemanager.isPresent()) {
            log.info("storemanager 로그인 시도");
            Password password = storemanager.get().getPassword();
            return new PrincipalDetails(storemanager.get().getId(), password, Role.ROLE_STOREMANAGER);
        }
        else throw new UsernameNotFoundException("해당 사용자를 찾을수 없습니다.:" + email);
    }

}
