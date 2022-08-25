package konkuk.nServer.domain.proposal.dto.requestForm;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SaveProposal {

    @NotNull(message = "postId는 필수입니다.")
    private Long postId;

    private List<Menus> menus = new ArrayList<>();

    @Data
    @NoArgsConstructor
    public static class Menus {
        @NotNull(message = "menuId는 필수입니다.")
        private Long menuId;

        @NotNull(message = "quantity는 필수입니다.")
        private Integer quantity;

        public Menus(Long menuId, Integer quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }
    }

    public SaveProposal(Long postId, List<Menus> menus) {
        this.postId = postId;
        this.menus = menus;
    }
}
