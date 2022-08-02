package konkuk.nServer.domain.store.service;

import konkuk.nServer.domain.store.domain.Menu;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStore;
import konkuk.nServer.domain.store.repository.MenuRepository;
import konkuk.nServer.domain.store.repository.StoreRepository;
import konkuk.nServer.domain.user.domain.Storemanager;
import konkuk.nServer.domain.user.repository.StoremanagerRepository;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoremanagerRepository storemanagerRepository;
    private final MenuRepository menuRepository;

    @Value("${image.menu}")
    private String menuImagePath;

    public void registryStore(Long storemanagerId, RegistryStore form) {
        Storemanager storemanager = storemanagerRepository.findById(storemanagerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));

        Store store = Store.builder()
                .name(form.getName())
                .phone(form.getPhone())
                .deliveryFee(form.getDeliveryFee())
                .address(form.getAddress())
                .businessHours(form.getBusinessHours()) // TODO 검증 필요
                .breakTime(form.getBreakTime()) // TODO 검증 필요
                .storemanager(storemanager)
                .build();

        form.getMenus().forEach(menuDto -> {
            Menu menu = new Menu(menuDto.getName(), menuDto.getPrice(), menuDto.getImageUrl());
            store.addMenu(menu);
            menuRepository.save(menu);
        });

        storeRepository.save(store);

        storemanager.addStore(store);
    }

    public List<String> registryImage(List<MultipartFile> menuImages) {
        List<String> imageStoreUrl = new ArrayList<>();

        try {
            for (MultipartFile reviewImage : menuImages) {
                String reviewImageFullName = createStoreFileName(reviewImage.getOriginalFilename());
                reviewImage.transferTo(new File(menuImagePath + reviewImageFullName));
                imageStoreUrl.add(reviewImageFullName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(ExceptionEnum.FAIL_STORE_IMAGE);
        }

        return imageStoreUrl;
    }

    private String createStoreFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }
}
