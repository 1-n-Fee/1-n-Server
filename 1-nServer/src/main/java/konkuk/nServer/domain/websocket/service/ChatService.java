package konkuk.nServer.domain.websocket.service;

import konkuk.nServer.domain.common.service.ConvertProvider;
import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.proposal.domain.Proposal;
import konkuk.nServer.domain.proposal.domain.ProposalState;
import konkuk.nServer.domain.proposal.repository.ProposalRepository;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.repository.UserFindDao;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final PostRepository postRepository;
    private final UserFindDao userFindDao;
    private final MessageRepository messageRepository;
    private final ConvertProvider convertProvider;
    private final ProposalRepository proposalRepository;

    @Transactional(readOnly = true)
    public FindRoom findUserRoom(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_POST));
        List<Proposal> proposals = proposalRepository.findByPostIdAndProposalState(postId, ProposalState.ACCEPTED);

        List<String> members = proposals.stream().map(proposal -> proposal.getUser().getNickname()).toList();

        Proposal proposal = proposalRepository.findByUserIdAndPostIdAndProposalState(userId, postId, ProposalState.ACCEPTED)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_PROPOSAL));

        int totalPrice = proposal.getProposalDetails()
                .stream()
                .mapToInt(proposalDetail -> proposalDetail.getMenu().getPrice() * proposalDetail.getQuantity())
                .sum();
        int deliveryFeePerPerson = post.getStore().getDeliveryFee() / members.size();

        return FindRoom.builder()
                .postId(postId)
                .storeName(post.getStore().getName())
                .isOwner(Objects.equals(post.getUser().getId(), userId))
                .postState(post.getProcess().name())
                .userTotalPrice(totalPrice + deliveryFeePerPerson)
                .deliveryFeePerPerson(deliveryFeePerPerson)
                .spotId(post.getSpot().getId())
                .limitNumber(post.getLimitNumber())
                .currentNumber(post.getCurrentNumber())
                .members(members)
                .build();
    }

    public ResponseMessage sendMessage(Long userId, RequestMessage requestMessage) {
        Post post = postRepository.findById(requestMessage.getPostId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_POST));

        User user = userFindDao.findById(userId);

        if (MessageType.ENTER.name().equals(requestMessage.getType())) {
            requestMessage.setContent(user.getNickname() + "님이 입장하였습니다.");
        }

        Message message = Message.builder()
                .type(convertProvider.convertMessageType(requestMessage.getType()))
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

    public List<ResponseMessage> findByPostId(Long postId) {
        return messageRepository.findByPostIdOrderByTimeAsc(postId).stream()
                .map(message -> ResponseMessage.of(message, postId))
                .toList();
    }
}