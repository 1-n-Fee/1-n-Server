package konkuk.nServer.domain.user.controller;

import konkuk.nServer.domain.storemanager.service.StoremanagerService;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.dto.requestForm.*;
import konkuk.nServer.domain.user.dto.responseForm.UserInfo;
import konkuk.nServer.domain.user.service.UserService;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
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
    private final StoremanagerService storemanagerService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Valid UserSignup form) {
        userService.signup(form);
    }

    @PostMapping("/signup/app")
    @ResponseStatus(HttpStatus.CREATED)
    public void signupForApp(@RequestBody @Valid UserSignupForApp form) {
        userService.signupForApp(form);
    }

    @GetMapping("/oauth/{oauth}")
    public void loginByOauth(@PathVariable String oauth, @RequestParam String code, HttpServletResponse response) {
        String jwtToken = userService.oAuthLogin(oauth, code);
        response.addHeader("Authorization", "Bearer " + jwtToken);
    }

    @GetMapping("/oauth/app/{oauth}")
    public void loginByOauthForApp(@PathVariable String oauth, @RequestParam String oauthId, HttpServletResponse response) {
        String jwtToken = userService.oAuthLoginForApp(oauth, oauthId);
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
        userService.changePassword(userDetail.getId(), changePassword.getNewPassword());
    }

    @PatchMapping("/change/nickname")
    public void changeNickname(@AuthenticationPrincipal PrincipalDetails userDetail,
                               @RequestBody @Valid ChangeNickname changeNickname) {
        changeNickname.validate();
        userService.changeNickname(userDetail.getId(), changeNickname.getNickname());
    }


    @PatchMapping("/change/sexType")
    public void changeSexType(@AuthenticationPrincipal PrincipalDetails userDetail,
                              @RequestBody @Valid ChangeSexType changeSexType) {
        userService.changeSexType(userDetail.getId(), changeSexType.getSexType());
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
    public UserInfo findLoginMemberInfo(@AuthenticationPrincipal PrincipalDetails userDetail) {
        if(userDetail.getRole()== Role.ROLE_STUDENT) return userService.findInfoByUserId(userDetail.getId());
        else if(userDetail.getRole()== Role.ROLE_STOREMANAGER) return storemanagerService.findInfoByUserId(userDetail.getId());
        else throw new ApiException(ExceptionEnum.NO_FOUND_USER);
    }

    @GetMapping("/isLogin")
    public Map<String, Object> userIsLogin(@AuthenticationPrincipal PrincipalDetails userDetail) {
        if (userDetail != null) {
            return Map.of("isLogin", true, "role", userDetail.getRole().name());
        } else return Map.of("isLogin", false);
    }
}
