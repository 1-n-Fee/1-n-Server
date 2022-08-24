package konkuk.nServer.domain.comment.service;

import konkuk.nServer.domain.comment.domain.Comment;
import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.comment.dto.requestForm.RegistryComment;
import konkuk.nServer.domain.comment.repository.CommentRepository;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public void registryComment(Long userId, RegistryComment registryComment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));

        Post post = postRepository.findById(registryComment.getPostId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_POST));

        Comment comment = Comment.builder()
                .createDateTime(LocalDateTime.now())
                .content(registryComment.getContent())
                .user(user)
                .post(post)
                .build();
        post.addComment(comment);
        commentRepository.save(comment);
    }
}
