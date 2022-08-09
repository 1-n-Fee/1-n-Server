package konkuk.nServer.domain.common.service;

import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    @Value("${image.menu}")
    private String menuImagePath;

    public UrlResource getImageFile(String type, String fileName) {
        log.info("{} 요청 filename={}", type + "Image", fileName);
        try {
            return new UrlResource("file:" + typeConvert(type) + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ExceptionEnum.FAIL_CALL_IMAGE);
        }
    }

    private String typeConvert(String type) {
        if (Objects.equals(type, "menu")) return menuImagePath;
        else throw new ApiException(ExceptionEnum.FAIL_CALL_IMAGE);
    }
}
