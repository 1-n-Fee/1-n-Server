package konkuk.nServer.domain.user.controller;

import konkuk.nServer.domain.user.dto.requestForm.StoremanagerSignup;
import konkuk.nServer.domain.user.service.StoremanagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
