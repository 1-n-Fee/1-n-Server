package konkuk.nServer.domain.proposal.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProposal is a Querydsl query type for Proposal
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProposal extends EntityPathBase<Proposal> {

    private static final long serialVersionUID = -1088914218L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProposal proposal = new QProposal("proposal");

    public final DateTimePath<java.time.LocalDateTime> createDateTime = createDateTime("createDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final konkuk.nServer.domain.post.domain.QPost post;

    public final ListPath<ProposalDetail, QProposalDetail> proposalDetails = this.<ProposalDetail, QProposalDetail>createList("proposalDetails", ProposalDetail.class, QProposalDetail.class, PathInits.DIRECT2);

    public final EnumPath<ProposalState> proposalState = createEnum("proposalState", ProposalState.class);

    public final konkuk.nServer.domain.user.domain.QUser user;

    public QProposal(String variable) {
        this(Proposal.class, forVariable(variable), INITS);
    }

    public QProposal(Path<? extends Proposal> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProposal(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProposal(PathMetadata metadata, PathInits inits) {
        this(Proposal.class, metadata, inits);
    }

    public QProposal(Class<? extends Proposal> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new konkuk.nServer.domain.post.domain.QPost(forProperty("post"), inits.get("post")) : null;
        this.user = inits.isInitialized("user") ? new konkuk.nServer.domain.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

