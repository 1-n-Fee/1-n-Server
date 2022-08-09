package konkuk.nServer.domain.proposal.dto.responseForm;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class FindProposal {

    @NotNull
    private Long proposalId;
    private String userNickname;
    private List<Menus> menus = new ArrayList<>();

    @Data
    @AllArgsConstructor
    public static class Menus {
        @NotNull
        private Long menuId;

        @NotNull
        private Integer quantity;
    }
}
