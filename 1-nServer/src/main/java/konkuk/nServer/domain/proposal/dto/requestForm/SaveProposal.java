package konkuk.nServer.domain.proposal.dto.requestForm;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SaveProposal {

    @NotNull
    private Long postId;

    private List<Menus> menus = new ArrayList<>();

    @Data
    @NoArgsConstructor
    public static class Menus {
        @NotNull
        private Long menuId;

        @NotNull
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
