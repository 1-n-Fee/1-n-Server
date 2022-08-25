package konkuk.nServer.domain.storemanager.repository;

import konkuk.nServer.domain.account.domain.AccountType;
import konkuk.nServer.domain.account.domain.Google;
import konkuk.nServer.domain.account.domain.Kakao;
import konkuk.nServer.domain.account.domain.Naver;
import konkuk.nServer.domain.account.repository.GoogleRepository;
import konkuk.nServer.domain.account.repository.KakaoRepository;
import konkuk.nServer.domain.account.repository.NaverRepository;
import konkuk.nServer.domain.storemanager.domain.Storemanager;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoremanagerFindDao {

    private final StoremanagerRepository storemanagerRepository;
    private final KakaoRepository kakaoRepository;
    private final NaverRepository naverRepository;
    private final GoogleRepository googleRepository;

    public Storemanager findByEmail(final String email) {
        return storemanagerRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_STOREMANAGER));
    }

    public Storemanager findById(final Long userId) {
        return storemanagerRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_STOREMANAGER));
    }


    public Storemanager findStoremanagerByOauth(final AccountType accountType, final String oauthId) {
        if (accountType == AccountType.KAKAO) {
            Kakao kakao = kakaoRepository.findByKakaoId(oauthId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_STOREMANAGER));
            return kakao.getStoremanager();
        } else if (accountType == AccountType.NAVER) {
            Naver naver = naverRepository.findByNaverId(oauthId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_STOREMANAGER));
            return naver.getStoremanager();
        } else if (accountType == AccountType.GOOGLE) {
            Google google = googleRepository.findByGoogleId(oauthId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_STOREMANAGER));
            return google.getStoremanager();
        } else throw new ApiException(ExceptionEnum.NO_FOUND_STOREMANAGER);
    }

}
