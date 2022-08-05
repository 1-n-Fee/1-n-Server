package konkuk.nServer.domain.proposal.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProposalDetail is a Querydsl query type for ProposalDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProposalDetail extends EntityPathBase<ProposalDetail> {

    private static final long serialVersionUID = 1141901511L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProposalDetail proposalDetail = new QProposalDetail("proposalDetail");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final konkuk.nServer.domain.store.domain.QMenu menu;

    public final QProposal proposal;

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public QProposalDetail(String variable) {
        this(ProposalDetail.class, forVariable(variable), INITS);
    }

    public QProposalDetail(Path<? extends ProposalDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProposalDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProposalDetail(PathMetadata metadata, PathInits inits) {
        this(ProposalDetail.class, metadata, inits);
    }

    public QProposalDetail(Class<? extends ProposalDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.menu = inits.isInitialized("menu") ? new konkuk.nServer.domain.store.domain.QMenu(forProperty("menu"), inits.get("menu")) : null;
        this.proposal = inits.isInitialized("proposal") ? new QProposal(forProperty("proposal"), inits.get("proposal")) : null;
    }

}

