package konkuk.nServer.domain.user.controller;

import konkuk.nServer.domain.user.dto.requestForm.RequestUserSignup;
import konkuk.nServer.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/health")
    public String controllerTest() {
        return "Spring server is running...";
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Valid RequestUserSignup form) {
        /**
         * 인가 코드 사용해서 oAuth 로그인 해줘야?
         */
        userService.signup(form);
        //return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @GetMapping("/duplication/nickname/{nickname}")
    public Map<String, Boolean> isDuplicationEmail(@PathVariable String nickname) {
        // 가능하면 map 말고, 클래스 분리해서 리턴하기
        return Map.of("isDuplication", userService.isDuplicateNickname(nickname));
    }

    /*

    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@RequestBody RequestLoginForm form) {
        LoginDto result = userService.login(form.getEmail(), form.getPassword());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/find/email")
    public HashMap<String, Object> findEmail(@RequestBody RequestFindEmailForm form) {
        String email = userService.findEmail(form.getName(), form.getPhone());

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("email", email);
        return result;
    }

    @PostMapping("/find/password")
    public HashMap<String, Object> findPassword(@RequestBody RequestFindPasswordForm form) {
        String tempPassword = userService.findPassword(form.getEmail(), form.getName(), form.getPhone());

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("tempPassword", tempPassword);
        return result;
    }

    @PostMapping("/change/password")
    public void changePassword(@AuthenticationPrincipal Long userId,
                               @RequestBody RequestChangePasswordForm form) {
        userService.changePassword(userId, form.getNewPassword());
    }

    @GetMapping("/point")
    public HashMap<String, Object> findPoint(@AuthenticationPrincipal Long userId) {
        Integer point = userService.findPointByMemberId(userId);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("point", point);
        return result;
    }

    @GetMapping("/info")
    public ResponseEntity<FindMemberInfoByUserIdDto> findLoginMemberInfo(@AuthenticationPrincipal Long userId) {
        FindMemberInfoByUserIdDto result = userService.findInfoByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/isLogin")
    public HashMap<String, Object> isLogin(@AuthenticationPrincipal Long userId) {
        log.info("로그인 여부 요청.");
        HashMap<String, Object> result = new HashMap<String, Object>();

        if (userId == null) result.put("isLogin", false);
        else {
            boolean isLogin = userService.existsMemberById(userId);
            result.put("isLogin", isLogin);
        }
        return result;
    }
     */
}
