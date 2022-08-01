package konkuk.nServer.domain.store.dto.requestForm;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class RegistryStore {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private Integer deliveryFee;

    @NotBlank
    private String address;

    @NotBlank
    private String businessHours;

    private String breakTime;

    private List<MenuDto> menus;

    @Data
    static class MenuDto {
        Integer price;
        String name;
        String imageUrl;
    }
}
