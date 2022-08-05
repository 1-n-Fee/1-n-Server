package konkuk.nServer.domain.post.service;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.domain.PostProcess;
import konkuk.nServer.domain.post.domain.Spot;
import konkuk.nServer.domain.post.dto.requestForm.RegistryPost;
import konkuk.nServer.domain.post.dto.responseForm.FindPost;
import konkuk.nServer.domain.post.dto.responseForm.FindPostDetail;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.proposal.domain.Proposal;
import konkuk.nServer.domain.proposal.repository.ProposalRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final ProposalRepository proposalRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public void registryPost(Long userId, RegistryPost registryPost) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
        Store store = storeRepository.findById(registryPost.getStoreId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_STORE));

        Spot spot = convertSpot(registryPost.getSpotId());

        Post post = Post.builder()
                .registryTime(LocalDateTime.now())
                .closeTime(convertTime(registryPost.getCloseTime()))
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


    public List<FindPost> findPostBySpot(Long userId, Long spotId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));

        Spot spot = getSpotById(spotId);
        if (spot == null) throw new ApiException(ExceptionEnum.INCORRECT_SPOT);

        List<Post> posts = postRepository.findBySpot(spot);

        return posts.stream()
                .map(post -> {
                    Store store = post.getStore();
                    FindPost res = FindPost.builder()
                            .postId(post.getId())
                            .deliveryFee(store.getDeliveryFee())
                            .currentNumber(post.getCurrentNumber())
                            .limitNumber(post.getLimitNumber())
                            .storeName(store.getName())
                            .category(store.getCategory().name())
                            .closeTime(post.getCloseTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm")))
                            .build();

                    Optional<Proposal> proposal = proposalRepository.findByUserAndPost(user, post);
                    proposal.ifPresent(value -> res.setState(value.getProposalState()));

                    return res;
                })
                .collect(Collectors.toList());
    }

    public Spot getSpotById(Long id) {
        Spot[] values = Spot.values();
        for (Spot spot : values) {
            if (spot.getId() == id) return spot;
        }
        return null;
    }

    public FindPostDetail findPostDetailById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_POST));

        List<FindPostDetail.MenuDetail> menus = post.getStore().getMenus().stream()
                .map(menu ->
                        new FindPostDetail.MenuDetail(menu.getName(), menu.getPrice(), menu.getImageUrl())
                ).toList();


        return FindPostDetail.builder()
                .currentNumber(post.getCurrentNumber())
                .limitNumber(post.getLimitNumber())
                .spotId(post.getSpot().getId())
                .storeName(post.getStore().getName())
                .closeTime(post.getCloseTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm")))
                .deliveryFee(post.getStore().getDeliveryFee())
                .menus(menus)
                .category(post.getStore().getCategory().name())
                .content(post.getContent())
                .build();
    }
}
