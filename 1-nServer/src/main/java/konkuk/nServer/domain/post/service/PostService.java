package konkuk.nServer.domain.post.service;

import konkuk.nServer.domain.post.domain.Category;
import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.domain.PostProcess;
import konkuk.nServer.domain.post.domain.Spot;
import konkuk.nServer.domain.post.dto.requestForm.RegistryPost;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.store.repository.StoreRepository;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public void registryPost(Long userId, RegistryPost registryPost) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
        Store store = storeRepository.findById(registryPost.getStoreId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_STORE));

        Spot spot = convertSpot(registryPost.getSpotId());
        Category category = convertCategory(registryPost.getCategory());

        Post post = Post.builder()
                .registryTime(LocalDateTime.now())
                .closeTime(convertTime(registryPost.getCloseTime()))
                .category(category)
                .content(registryPost.getContent())
                .process(PostProcess.RECRUITING)
                .currentNumber(0)
                .limitNumber(registryPost.getLimitNumber())
                .user(user)
                .store(store)
                .spot(spot)
                .build();

        postRepository.save(post);
    }


    private LocalDateTime convertTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm"));
    }

    private Spot convertSpot(Long spotId) {
        for (Spot spot : Spot.values()) {
            if (spot.getId() == spotId) return spot;
        }
        throw new ApiException(ExceptionEnum.INCORRECT_SPOT);
    }

    private Category convertCategory(String categoryName) {
        for (Category category : Category.values()) {
            if (category.name().equals(categoryName)) return category;
        }
        throw new ApiException(ExceptionEnum.INCORRECT_CATEGORY);
    }
}
