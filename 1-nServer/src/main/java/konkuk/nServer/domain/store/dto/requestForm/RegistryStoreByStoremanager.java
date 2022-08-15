package konkuk.nServer.domain.store.dto.requestForm;

import konkuk.nServer.domain.post.domain.Category;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.store.domain.StoreState;
import konkuk.nServer.domain.user.domain.Storemanager;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class RegistryStoreByStoremanager {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotNull
    private Integer deliveryFee;

    @NotBlank
    private String address;

    @NotBlank
    private String businessHours; // hhmm-hhmm

    @NotBlank
    private String category;

    private String breakTime; // hhmm-hhmm

    private List<MenuDto> menus;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MenuDto {
        Integer price;
        String name;
        String imageUrl;
    }

    @Builder
    public RegistryStoreByStoremanager(String category, String name, String phone, Integer deliveryFee, String address,
                                       String businessHours, String breakTime, List<MenuDto> menus) {
        this.category = category;
        this.name = name;
        this.phone = phone;
        this.deliveryFee = deliveryFee;
        this.address = address;
        this.businessHours = businessHours;
        this.breakTime = breakTime;
        this.menus = menus;

    }

    public void validate() {
        if (businessHours.length() != 9)
            throw new ApiException(ExceptionEnum.INCORRECT_HOUR);

        for (int i = 0; i < 4; i++) {
            if (businessHours.charAt(i) < '0' || businessHours.charAt(i) > '9')
                throw new ApiException(ExceptionEnum.INCORRECT_HOUR);
        }

        if (businessHours.charAt(4) != '-') throw new ApiException(ExceptionEnum.INCORRECT_HOUR);

        for (int i = 5; i < 9; i++) {
            if (businessHours.charAt(i) < '0' || businessHours.charAt(i) > '9')
                throw new ApiException(ExceptionEnum.INCORRECT_HOUR);
        }

        if (breakTime != null) {
            for (int i = 0; i < 4; i++) {
                if (breakTime.charAt(i) < '0' || breakTime.charAt(i) > '9')
                    throw new ApiException(ExceptionEnum.INCORRECT_HOUR);
            }

            if (breakTime.charAt(4) != '-') throw new ApiException(ExceptionEnum.INCORRECT_HOUR);

            for (int i = 5; i < 9; i++) {
                if (breakTime.charAt(i) < '0' || breakTime.charAt(i) > '9')
                    throw new ApiException(ExceptionEnum.INCORRECT_HOUR);
            }
        }
    }

    public Store toEntity(Storemanager storemanager, Category category) {
        return Store.builder()
                .name(name)
                .phone(phone)
                .deliveryFee(deliveryFee)
                .address(address)
                .businessHours(businessHours)
                .breakTime(breakTime)
                .storemanager(storemanager)
                .category(category)
                .state(StoreState.ACTIVE)
                .build();
    }
}
