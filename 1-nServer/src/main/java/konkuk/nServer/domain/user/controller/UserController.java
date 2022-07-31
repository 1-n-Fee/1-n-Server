package konkuk.nServer.domain.user.controller;

import konkuk.nServer.domain.user.domain.SexType;
import konkuk.nServer.domain.user.dto.requestForm.*;
import konkuk.nServer.domain.user.dto.responseForm.UserInfo;
import konkuk.nServer.domain.user.service.UserService;
import konkuk.nServer.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    public void signup(@RequestBody @Valid UserSignup form) {
        userService.signup(form);
        //return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/oauth/{oauth}")
    public void loginByOauth(@PathVariable String oauth, @RequestParam String code, HttpServletResponse response) {
        String jwtToken = userService.oAuthLogin(oauth, code);
        response.addHeader("Authorization", "Bearer " + jwtToken);
    }


    @GetMapping("/duplication/nickname/{nickname}")
    public Map<String, Boolean> isDuplicationEmail(@PathVariable String nickname) {
        // 가능하면 map 말고, 클래스 분리해서 리턴하기
        return Map.of("isDuplication", userService.isDuplicateNickname(nickname));
    }

    @PatchMapping("/change/password")
    public void changePassword(@AuthenticationPrincipal PrincipalDetails userDetail,
                               @RequestBody @Valid ChangePassword changePassword) {
        changePassword.validate();
        userService.changePassword(userDetail.getUserId(), changePassword.getNewPassword());
    }

    @PatchMapping("/change/nickname")
    public void changeNickname(@AuthenticationPrincipal PrincipalDetails userDetail,
                               @RequestBody @Valid ChangeNickname changeNickname) {
        changeNickname.validate();
        userService.changeNickname(userDetail.getUserId(), changeNickname.getNickname());
    }


    @PatchMapping("/change/sexType")
    public void changeSexType(@AuthenticationPrincipal PrincipalDetails userDetail,
                               @RequestBody @Valid ChangeSexType changeSexType) {
        userService.changeSexType(userDetail.getUserId(), changeSexType.getSexType());
    }


    @PostMapping("/find/password")
    public Map<String, Object> findPassword(@RequestBody @Valid FindPassword form) {
        String tempPassword = userService.findPassword(form.getEmail(), form.getName(), form.getPhone());
        return Map.of("tempPassword", tempPassword);
    }

    @PostMapping("/find/email")
    public Map<String, Object> findEmail(@RequestBody FindEmail form) {
        String email = userService.findEmail(form.getName(), form.getPhone());
        return Map.of("email", email);
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfo> findLoginMemberInfo(@AuthenticationPrincipal PrincipalDetails userDetail) {
        UserInfo result = userService.findInfoByUserId(userDetail.getUserId());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

/*
    @GetMapping("/point")
    public HashMap<String, Object> findPoint(@AuthenticationPrincipal Long userId) {
        Integer point = userService.findPointByMemberId(userId);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("point", point);
        return result;
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
