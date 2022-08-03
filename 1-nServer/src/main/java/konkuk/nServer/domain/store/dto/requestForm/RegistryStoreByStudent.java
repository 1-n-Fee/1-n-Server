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
// TODO
    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private Integer deliveryFee;

    @NotBlank
    private String address;

    private String businessHours; // hhmm-hhmm

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
    public RegistryStoreByStudent(String name, String phone, Integer deliveryFee, String address,
                                  String businessHours, String breakTime, List<MenuDto> menus) {
        this.name = name;
        this.phone = phone;
        this.deliveryFee = deliveryFee;
        this.address = address;
        this.businessHours = businessHours;
        this.breakTime = breakTime;
        this.menus = menus;
    }
}
