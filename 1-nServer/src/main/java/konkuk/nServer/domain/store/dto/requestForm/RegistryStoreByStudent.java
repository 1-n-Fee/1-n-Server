package konkuk.nServer.domain.store.dto.requestForm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
public class RegistryStoreByStudent {
    @NotBlank
    private String name;

    @NotBlank
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
}
