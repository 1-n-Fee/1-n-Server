package konkuk.nServer.domain.proposal.dto.requestForm;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ApproveProposal {

    @NotNull
    private Long proposalId;

    @NotNull
    private Boolean isApprove;
}
