package konkuk.nServer.domain.websocket.controller;

import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import konkuk.nServer.security.PrincipalDetails;
import konkuk.nServer.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SocketTokenController {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository; // TODO

    @GetMapping("/socketToken")
    @ResponseBody
    public Map<String, String> provideToken(@AuthenticationPrincipal PrincipalDetails userDetail) {
        String socketJwt = tokenProvider.createSocketJwt(userDetail.getId());
        User user = userRepository.findById(userDetail.getId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));// TODO
        return Map.of("socketToken", socketJwt, "nickname", user.getNickname());
    }

}
