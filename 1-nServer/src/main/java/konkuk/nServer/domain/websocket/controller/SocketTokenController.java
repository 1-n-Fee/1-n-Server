package konkuk.nServer.domain.websocket.controller;

import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.repository.UserFindDao;
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
    private final UserFindDao userFindDao;

    @GetMapping("/socketToken")
    @ResponseBody
    public Map<String, String> provideToken(@AuthenticationPrincipal PrincipalDetails userDetail) {
        String socketJwt = tokenProvider.createSocketJwt(userDetail.getId());
        User user = userFindDao.findById(userDetail.getId());
        return Map.of("socketToken", socketJwt, "nickname", user.getNickname());
    }

}
