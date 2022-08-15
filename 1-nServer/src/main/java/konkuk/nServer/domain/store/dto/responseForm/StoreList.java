package konkuk.nServer.domain.store.dto.responseForm;

import konkuk.nServer.domain.store.domain.Store;
import lombok.Builder;
import lombok.Data;

@Data
public class StoreList {
    private Long id;
    private String name;
    private String category;
    private Integer deliveryFee;

    @Builder
    public StoreList(Long id, String name, String category, Integer deliveryFee) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.deliveryFee = deliveryFee;
    }

    public static StoreList of(Store store) {
        return StoreList.builder()
                .name(store.getName())
                .category(store.getCategory().toString())
                .deliveryFee(store.getDeliveryFee())
                .id(store.getId())
                .build();
    }
}
