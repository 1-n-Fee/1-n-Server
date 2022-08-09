package konkuk.nServer.domain.common.controller;

import konkuk.nServer.domain.common.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/{type}/{filename}")
    public Resource image(@PathVariable String type, @PathVariable String filename) {
        return imageService.getImageFile(type, filename);
    }
}
