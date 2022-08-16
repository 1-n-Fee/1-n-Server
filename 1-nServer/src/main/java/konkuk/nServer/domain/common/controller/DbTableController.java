package konkuk.nServer.domain.common.controller;

import konkuk.nServer.domain.account.domain.Google;
import konkuk.nServer.domain.account.domain.Kakao;
import konkuk.nServer.domain.account.domain.Naver;
import konkuk.nServer.domain.account.domain.Password;
import konkuk.nServer.domain.account.repository.GoogleRepository;
import konkuk.nServer.domain.account.repository.KakaoRepository;
import konkuk.nServer.domain.account.repository.NaverRepository;
import konkuk.nServer.domain.account.repository.PasswordRepository;
import konkuk.nServer.domain.post.domain.Comment;
import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.repository.CommentRepository;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.proposal.domain.Proposal;
import konkuk.nServer.domain.proposal.domain.ProposalDetail;
import konkuk.nServer.domain.proposal.repository.ProposalDetailRepository;
import konkuk.nServer.domain.proposal.repository.ProposalRepository;
import konkuk.nServer.domain.store.domain.Menu;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.store.repository.MenuRepository;
import konkuk.nServer.domain.store.repository.StoreRepository;
import konkuk.nServer.domain.storemanager.domain.Storemanager;
import konkuk.nServer.domain.storemanager.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.domain.*;
import konkuk.nServer.domain.user.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DbTableController {
    private final KakaoRepository kakaoRepository;
    private final NaverRepository naverRepository;
    private final GoogleRepository googleRepository;
    private final PasswordRepository passwordRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final StoremanagerRepository storemanagerRepository;
    private final ProposalRepository proposalRepository;
    private final ProposalDetailRepository proposalDetailRepository;
    private final StoreRepository storeRepository;
    private final CommentRepository commentRepository;
    private final MenuRepository menuRepository;

    @GetMapping("/table")
    public String showDbTable(Model model) {
        log.info("DB 테이블 보기");
        List<Kakao> kakaos = kakaoRepository.findAll();
        List<Naver> navers = naverRepository.findAll();
        List<Google> googles = googleRepository.findAll();
        List<Password> passwords = passwordRepository.findAll();
        List<User> users = userRepository.findAll();
        List<Post> posts = postRepository.findAll();
        List<Storemanager> storemanagers = storemanagerRepository.findAll();
        List<Proposal> proposals = proposalRepository.findAll();
        List<ProposalDetail> proposalDetails = proposalDetailRepository.findAll();
        List<Store> stores = storeRepository.findAll();
        List<Comment> comments = commentRepository.findAll();
        List<Menu> menus = menuRepository.findAll();


        model.addAttribute("kakaos", kakaos);
        model.addAttribute("navers", navers);
        model.addAttribute("googles", googles);
        model.addAttribute("passwords", passwords);
        model.addAttribute("users", users);
        model.addAttribute("posts", posts);
        model.addAttribute("storemanagers", storemanagers);
        model.addAttribute("proposals", proposals);
        model.addAttribute("proposalDetails", proposalDetails);
        model.addAttribute("stores", stores);
        model.addAttribute("comments", comments);
        model.addAttribute("menus", menus);

        List<String> table = Arrays.asList("kakao", "naver", "google", "password", "user", "storemanager",
                "post", "proposal", "proposalDetail", "store", "comment", "menu");
        model.addAttribute("tableName", table);

        return "showDbTable";
    }
}
