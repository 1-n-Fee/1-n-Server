package konkuk.nServer.domain.store.dto.requestForm;

import konkuk.nServer.domain.post.domain.Category;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.store.domain.StoreState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class RegistryStoreByStudent {
    @NotBlank
    private String name;

    @NotNull
    private Integer deliveryFee;

    @NotBlank
    private String category;

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
    public RegistryStoreByStudent(String category, String name, Integer deliveryFee, List<MenuDto> menus) {
        this.category = category;
        this.name = name;
        this.deliveryFee = deliveryFee;
        this.menus = menus;
    }

    public Store toEntity(Category category) {
        return Store.builder()
                .name(name)
                .deliveryFee(deliveryFee)
                .phone("temp")
                .category(category)
                .address("temp")
                .businessHours("0000-0000")
                .state(StoreState.TEMPORARY)
                .build();
    }
}
