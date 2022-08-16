package konkuk.nServer.domain.store.dto.responseForm;

import konkuk.nServer.domain.store.domain.Menu;
import lombok.Builder;
import lombok.Data;

@Data
public class StoreMenu {

    private int price;
    private Long menuId;
    private String name;
    private String imageUrl;

    @Builder
    public StoreMenu(int price, String name, String imageUrl, Long menuId) {
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.menuId = menuId;
    }

    public static StoreMenu of(Menu menu) {
        return StoreMenu.builder()
                .price(menu.getPrice())
                .name(menu.getName())
                .imageUrl(menu.getImageUrl())
                .menuId(menu.getId())
                .build();
    }
}
