package konkuk.nServer.domain.store.dto.responseForm;

import com.fasterxml.jackson.annotation.JsonInclude;
import konkuk.nServer.domain.store.domain.Store;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
public class FindStore {

    private Long storeId;
    private String name;
    private String phone;
    private Integer deliveryFee;
    private String address;
    private String businessHours;
    private String breakTime;
    private String state;
    private String category;

    @Builder
    public FindStore(Long storeId, String name, String phone, Integer deliveryFee, String address,
                     String businessHours, String breakTime, String state, String category) {
        this.storeId = storeId;
        this.name = name;
        this.phone = phone;
        this.deliveryFee = deliveryFee;
        this.address = address;
        this.businessHours = businessHours;
        this.breakTime = breakTime;
        this.state = state;
        this.category = category;
    }

    public static FindStore of(Store store) {
        return FindStore.builder()
                .storeId(store.getId())
                .name(store.getName())
                .phone(store.getPhone())
                .deliveryFee(store.getDeliveryFee())
                .address(store.getAddress())
                .businessHours(store.getBusinessHours())
                .breakTime(store.getBreakTime())
                .state(store.getState().name())
                .category(store.getCategory().name())
                .build();
    }

}
