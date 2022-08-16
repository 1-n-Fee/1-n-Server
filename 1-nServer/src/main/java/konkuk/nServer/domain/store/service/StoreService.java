package konkuk.nServer.domain.store.service;

import konkuk.nServer.domain.common.service.ConvertProvider;
import konkuk.nServer.domain.post.domain.Category;
import konkuk.nServer.domain.store.domain.Menu;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStoremanager;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStudent;
import konkuk.nServer.domain.store.dto.responseForm.StoreList;
import konkuk.nServer.domain.store.repository.MenuRepository;
import konkuk.nServer.domain.store.repository.StoreRepository;
import konkuk.nServer.domain.storemanager.domain.Storemanager;
import konkuk.nServer.domain.storemanager.repository.StoremanagerRepository;
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
    private final StoremanagerRepository storemanagerRepository;
    private final MenuRepository menuRepository;
    private final ConvertProvider convertProvider;

    @Value("${image.menu}")
    private String menuImagePath;

    @Value("${image.default_filename}")
    private String defaultMenuImage;

    public void registryStoreByStoremanager(Long storemanagerId, RegistryStoreByStoremanager form) {
        Storemanager storemanager = storemanagerRepository.findById(storemanagerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
        Category category = convertProvider.convertCategory(form.getCategory());

        Store store = form.toEntity(storemanager, category);
        storemanager.addStore(store);

        for (RegistryStoreByStoremanager.MenuDto menuDto : form.getMenus()) {
            String imageUrl = menuDto.getImageUrl();
            if (imageUrl == null) imageUrl = defaultMenuImage;

            Menu menu = new Menu(menuDto.getName(), menuDto.getPrice(), imageUrl);
            store.addMenu(menu);
            menuRepository.save(menu);
        }
        storeRepository.save(store);
    }

    public Long registryStoreByStudent(RegistryStoreByStudent form) {
        Category category = convertProvider.convertCategory(form.getCategory());
        Store store = form.toEntity(category);

        for (RegistryStoreByStudent.MenuDto menuDto : form.getMenus()) {
            String imageUrl = menuDto.getImageUrl();
            if (imageUrl == null) imageUrl = defaultMenuImage;

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

    public List<StoreList> getStoreListByCategory(String category) {
        /**
         * 원래는 findAll() 을 사용하는 경우가 적음 -> DB 부담 커짐, 비용 너무 큼
         * 페이징 처리를 하는게 맞다.
         */

        List<Store> stores;

        if (Objects.equals(category, "all")) stores = storeRepository.findAll();
        else stores = storeRepository.findByCategory(convertProvider.convertCategory(category));

        return stores.stream()
                .map(StoreList::of)
                .collect(Collectors.toList());
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
