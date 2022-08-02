package konkuk.nServer.domain.store.service;

import konkuk.nServer.domain.store.domain.Menu;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStore;
import konkuk.nServer.domain.store.repository.MenuRepository;
import konkuk.nServer.domain.store.repository.StoreRepository;
import konkuk.nServer.domain.user.domain.Storemanager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    public void registryStore(Storemanager storemanager, RegistryStore form) {
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
    }
}
