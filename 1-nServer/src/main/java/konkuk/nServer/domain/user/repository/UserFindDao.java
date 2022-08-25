package konkuk.nServer.domain.user.repository;

import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserFindDao {

    private final UserRepository userRepository;

    public User findByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
    }

    public User findById(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
    }

    public User findByNameAndPhone(final String name, final String phone) {
        return userRepository.findByNameAndPhone(name, phone)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
    }

}
