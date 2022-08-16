package konkuk.nServer.domain.storemanager.controller;

import konkuk.nServer.domain.storemanager.dto.request.StoremanagerSignup;
import konkuk.nServer.domain.storemanager.dto.request.StoremanagerSignupForApp;
import konkuk.nServer.domain.storemanager.service.StoremanagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
public class StoremanagerController {

    private final StoremanagerService storemanagerService;


    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Valid StoremanagerSignup form) {
        storemanagerService.signup(form);
        //return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/signup/app")
    @ResponseStatus(HttpStatus.CREATED)
    public void signupForApp(@RequestBody @Valid StoremanagerSignupForApp form) {
        storemanagerService.signupForApp(form);
    }

    @GetMapping("/oauth/{oauth}")
    public void loginByOauth(@PathVariable String oauth, @RequestParam String code, HttpServletResponse response) {
        String jwtToken = storemanagerService.oAuthLogin(oauth, code);
        response.addHeader("Authorization", "Bearer " + jwtToken);
    }

    @GetMapping("/oauth/app/{oauth}")
    public void loginByOauthForApp(@PathVariable String oauth, @RequestParam String oauthId, HttpServletResponse response) {
        String jwtToken = storemanagerService.oAuthLoginForApp(oauth, oauthId);
        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
