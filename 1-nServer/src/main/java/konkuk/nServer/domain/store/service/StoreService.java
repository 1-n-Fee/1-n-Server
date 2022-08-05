package konkuk.nServer.domain.store.service;

import konkuk.nServer.domain.post.domain.Category;
import konkuk.nServer.domain.store.domain.Menu;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.store.domain.StoreState;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStoremanager;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStudent;
import konkuk.nServer.domain.store.dto.responseForm.StoreList;
import konkuk.nServer.domain.store.repository.MenuRepository;
import konkuk.nServer.domain.store.repository.StoreRepository;
import konkuk.nServer.domain.user.domain.Storemanager;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.repository.UserRepository;
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
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final StoremanagerRepository storemanagerRepository;
    private final MenuRepository menuRepository;

    @Value("${image.menu}")
    private String menuImagePath;

    public void registryStoreByStoremanager(Long storemanagerId, RegistryStoreByStoremanager form) {
        Storemanager storemanager = storemanagerRepository.findById(storemanagerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));

        Category category = convertCategory(form.getCategory());

        Store store = Store.builder()
                .name(form.getName())
                .phone(form.getPhone())
                .deliveryFee(form.getDeliveryFee())
                .address(form.getAddress())
                .businessHours(form.getBusinessHours())
                .breakTime(form.getBreakTime())
                .storemanager(storemanager)
                .category(category)
                .state(StoreState.ACTIVE)
                .build();

        storemanager.addStore(store);

        for (RegistryStoreByStoremanager.MenuDto menuDto : form.getMenus()) {
            String imageUrl = menuDto.getImageUrl();
            if (imageUrl == null) imageUrl = "default";

            Menu menu = new Menu(menuDto.getName(), menuDto.getPrice(), imageUrl);
            store.addMenu(menu);
            menuRepository.save(menu);
        }
        storeRepository.save(store);
    }

    public Long registryStoreByStudent(Long studentId, RegistryStoreByStudent form) {
        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));

        Category category = convertCategory(form.getCategory());

        Store store = Store.builder()
                .name(form.getName())
                .deliveryFee(form.getDeliveryFee())
                .phone("temp")
                .category(category)
                .address("temp")
                .businessHours("0000-0000")
                .state(StoreState.TEMPORARY)
                .build();

        for (RegistryStoreByStudent.MenuDto menuDto : form.getMenus()) {
            String imageUrl = menuDto.getImageUrl();
            if (imageUrl == null) imageUrl = "default";

            Menu menu = new Menu(menuDto.getName(), menuDto.getPrice(), imageUrl);
            store.addMenu(menu);
            menuRepository.save(menu);
        }
        return storeRepository.save(store).getId();
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


    public List<StoreList> getStoreListByCategory(String category) {
        /**
         * 원래는 findAll() 을 사용하는 경우가 적음 -> DB 부담 커짐, 비용 너무 큼
         * 페이징 처리를 하는게 맞다.
         */
        if (Objects.equals(category, "all"))
            return storeRepository.findAll().stream()
                    .map(store -> StoreList.builder()
                            .name(store.getName())
                            .category(store.getCategory().toString())
                            .deliveryFee(store.getDeliveryFee())
                            .id(store.getId())
                            .build())
                    .collect(Collectors.toList());

        else return storeRepository.findByCategory(convertCategory(category)).stream()
                .map(store -> StoreList.builder()
                        .name(store.getName())
                        .category(store.getCategory().toString())
                        .deliveryFee(store.getDeliveryFee())
                        .id(store.getId())
                        .build())
                .collect(Collectors.toList());
    }

    private Category convertCategory(String category) {
        //KOREAN, CHINESE, WESTERN, JAPANESE, MIDNIGHT, ETC
        if (Objects.equals(category, "korean")) return Category.KOREAN;
        else if (Objects.equals(category, "chinese")) return Category.CHINESE;
        else if (Objects.equals(category, "western")) return Category.WESTERN;
        else if (Objects.equals(category, "japanese")) return Category.JAPANESE;
        else if (Objects.equals(category, "midnight")) return Category.MIDNIGHT;
        else if (Objects.equals(category, "etc")) return Category.ETC;
        else throw new ApiException(ExceptionEnum.INCORRECT_CATEGORY);
    }
}
