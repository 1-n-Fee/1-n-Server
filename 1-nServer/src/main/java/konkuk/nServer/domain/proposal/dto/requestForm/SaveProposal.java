package konkuk.nServer.domain.proposal.dto.requestForm;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class SaveProposal {

    @NotNull
    private Long postId;

    private List<Menus> menus = new ArrayList<>();

    @Data
    public static class Menus {
        @NotNull
        private Long menuId;

        @NotNull
        private Integer quantity;
    }
}
