package konkuk.nServer.domain.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthCheckController {


    @GetMapping("/health")
    public String controllerTest() {
        return "Spring server is running...";
    }



}
