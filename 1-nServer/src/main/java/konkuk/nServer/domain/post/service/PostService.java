package konkuk.nServer.domain.post.service;

import konkuk.nServer.domain.common.service.ConvertProvider;
import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.domain.PostProcess;
import konkuk.nServer.domain.post.domain.Spot;
import konkuk.nServer.domain.post.dto.requestForm.RegistryPost;
import konkuk.nServer.domain.post.dto.responseForm.FindPost;
import konkuk.nServer.domain.post.dto.responseForm.FindPostDetail;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.proposal.repository.ProposalRepository;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.store.repository.StoreRepository;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Order.asc;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final ProposalRepository proposalRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ConvertProvider convertProvider;

    public void registryPost(Long userId, RegistryPost registryPost) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
        Store store = storeRepository.findById(registryPost.getStoreId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_STORE));
        Spot spot = convertProvider.convertSpot(registryPost.getSpotId());
        LocalDateTime closeTime = convertProvider.convertTime(registryPost.getCloseTime());

        Post post = registryPost.toEntity(user, store, spot, closeTime);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<FindPost> findPostBySpot(Long userId, Long spotId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
        Spot spot = convertProvider.convertSpot(spotId);
        List<Post> posts = postRepository.findBySpot(spot);

        return posts.stream()
                .map(post -> {
                    FindPost res = FindPost.of(post);

                    proposalRepository.findByUserAndPost(user, post).
                            ifPresent(value -> res.setState(value.getProposalState().name()));

                    if (Objects.equals(post.getUser().getId(), userId)) res.setState("OWNER");

                    return res;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FindPostDetail findPostDetailById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_POST));

        List<FindPostDetail.MenuDetail> menus = post.getStore().getMenus().stream()
                .map(FindPostDetail.MenuDetail::of)
                .toList();

        List<FindPostDetail.CommentDto> comments = post.getComments().stream()
                .map(FindPostDetail.CommentDto::of)
                .toList();

        return FindPostDetail.of(post, menus, comments);
    }

    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_POST));

        if (!Objects.equals(post.getUser().getId(), userId))
            throw new ApiException(ExceptionEnum.NOT_OWNER_POST);

        if (post.getProcess() != PostProcess.RECRUITING)
            throw new ApiException(ExceptionEnum.NOT_DELETE_POST);

        postRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public List<FindPost> findPostByStoreName(Long userId, String storeName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
        List<Store> stores = storeRepository.findByNameContains(storeName);

        List<FindPost> res = new ArrayList<>();
        for (Store store : stores) {
            List<Post> posts = postRepository.findByStore(store);

            for (Post post : posts) {
                FindPost findPost = FindPost.of(post);

                proposalRepository.findByUserAndPost(user, post).
                        ifPresent(value -> findPost.setState(value.getProposalState().name()));

                if (Objects.equals(post.getUser().getId(), userId)) findPost.setState("OWNER");

                res.add(findPost);
            }
        }
        return res;
    }

    @Transactional(readOnly = true)
    public List<FindPost> findPostByDate(Long userId, String dateStr) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));

        LocalDateTime start = LocalDateTime.parse(dateStr + ".00:00:00", DateTimeFormatter.ofPattern("yyyyMMdd.HH:mm:ss"));
        LocalDateTime end = LocalDateTime.parse(dateStr + ".23:59:59", DateTimeFormatter.ofPattern("yyyyMMdd.HH:mm:ss"));

        List<Post> posts = postRepository.findByCloseTimeBetween(start, end, Sort.by(asc("closeTime")));
        return posts.stream()
                .map(post -> {
                    FindPost res = FindPost.of(post);

                    proposalRepository.findByUserAndPost(user, post).
                            ifPresent(value -> res.setState(value.getProposalState().name()));

                    if (Objects.equals(post.getUser().getId(), userId)) res.setState("OWNER");

                    return res;
                })
                .collect(Collectors.toList());
    }
}
