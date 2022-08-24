package konkuk.nServer.domain.websocket.service;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.domain.PostProcess;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.domain.websocket.domain.Message;
import konkuk.nServer.domain.websocket.domain.MessageType;
import konkuk.nServer.domain.websocket.dto.request.RequestMessage;
import konkuk.nServer.domain.websocket.dto.response.FindRoom;
import konkuk.nServer.domain.websocket.dto.response.ResponseMessage;
import konkuk.nServer.domain.websocket.repository.MessageRepository;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    //채팅방 불러오기
    public List<FindRoom> findUserRoom(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));

        List<FindRoom> findRooms = user.getProposal().stream()
                .filter(proposal -> proposal.getPost().getProcess() != PostProcess.CLOSE)
                .map(proposal -> FindRoom.builder()
                        .postState(proposal.getPost().getProcess().name())
                        .isOwner(false)
                        .postId(proposal.getPost().getId())
                        .storeName(proposal.getPost().getStore().getName())
                        .build())
                .collect(Collectors.toList());

        List<Post> posts = postRepository.findByUserId(userId);
        for (Post post : posts) {
            if (post.getProcess() != PostProcess.CLOSE) {
                FindRoom findRoom = FindRoom.builder()
                        .postState(post.getProcess().name())
                        .isOwner(true)
                        .postId(post.getId())
                        .storeName(post.getStore().getName())
                        .build();
                findRooms.add(findRoom);
            }
        }

        return findRooms;
    }

    public ResponseMessage sendMessage(Long userId, RequestMessage requestMessage) {
        Post post = postRepository.findById(requestMessage.getPostId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_POST));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));

        if (MessageType.ENTER.name().equals(requestMessage.getType())) {
            requestMessage.setContent(user.getNickname() + "님이 입장하였습니다.");
        }

        Message message = Message.builder()
                .type(convertMessageType(requestMessage.getType()))
                .content(requestMessage.getContent())
                .time(LocalDateTime.now())
                .post(post)
                .user(user)
                .build();
        messageRepository.save(message);

        return ResponseMessage.builder()
                .postId(requestMessage.getPostId())
                .sendTime(message.getTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm")))
                .type(requestMessage.getType())
                .content(requestMessage.getContent())
                .sender(requestMessage.getNickname())
                .build();
    }

    private MessageType convertMessageType(String type) {
        if (Objects.equals(type, "ENTER")) return MessageType.ENTER;
        else if (Objects.equals(type, "TALK")) return MessageType.TALK;
        else throw new ApiException(ExceptionEnum.INCORRECT_MESSAGE_TYPE);
    }

    public List<ResponseMessage> findByPostId(Long postId) {
        return messageRepository.findByPostId(postId).stream()
                .map(message -> ResponseMessage.of(message, postId))
                .toList();
    }
}