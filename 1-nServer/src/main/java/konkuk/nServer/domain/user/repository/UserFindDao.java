package konkuk.nServer.domain.user.repository;

import konkuk.nServer.domain.account.domain.AccountType;
import konkuk.nServer.domain.account.domain.Google;
import konkuk.nServer.domain.account.domain.Kakao;
import konkuk.nServer.domain.account.domain.Naver;
import konkuk.nServer.domain.account.repository.GoogleRepository;
import konkuk.nServer.domain.account.repository.KakaoRepository;
import konkuk.nServer.domain.account.repository.NaverRepository;
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
    private final KakaoRepository kakaoRepository;
    private final NaverRepository naverRepository;
    private final GoogleRepository googleRepository;

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

    public User findUserByOauth(final AccountType accountType, final String oauthId) {
        if (accountType == AccountType.KAKAO) {
            Kakao kakao = kakaoRepository.findByKakaoId(oauthId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
            return kakao.getUser();
        } else if (accountType == AccountType.NAVER) {
            Naver naver = naverRepository.findByNaverId(oauthId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
            return naver.getUser();
        } else if (accountType == AccountType.GOOGLE) {
            Google google = googleRepository.findByGoogleId(oauthId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
            return google.getUser();
        } else throw new ApiException(ExceptionEnum.NO_FOUND_USER);
    }

}
