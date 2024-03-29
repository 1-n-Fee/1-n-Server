package konkuk.nServer.domain.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 474541576L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final EnumPath<konkuk.nServer.domain.account.domain.AccountType> accountType = createEnum("accountType", konkuk.nServer.domain.account.domain.AccountType.class);

    public final StringPath email = createString("email");

    public final konkuk.nServer.domain.account.domain.QGoogle google;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final konkuk.nServer.domain.account.domain.QKakao kakao;

    public final StringPath major = createString("major");

    public final StringPath name = createString("name");

    public final konkuk.nServer.domain.account.domain.QNaver naver;

    public final StringPath nickname = createString("nickname");

    public final konkuk.nServer.domain.account.domain.QPassword password;

    public final StringPath phone = createString("phone");

    public final ListPath<konkuk.nServer.domain.proposal.domain.Proposal, konkuk.nServer.domain.proposal.domain.QProposal> proposal = this.<konkuk.nServer.domain.proposal.domain.Proposal, konkuk.nServer.domain.proposal.domain.QProposal>createList("proposal", konkuk.nServer.domain.proposal.domain.Proposal.class, konkuk.nServer.domain.proposal.domain.QProposal.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final EnumPath<SexType> sexType = createEnum("sexType", SexType.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.google = inits.isInitialized("google") ? new konkuk.nServer.domain.account.domain.QGoogle(forProperty("google"), inits.get("google")) : null;
        this.kakao = inits.isInitialized("kakao") ? new konkuk.nServer.domain.account.domain.QKakao(forProperty("kakao"), inits.get("kakao")) : null;
        this.naver = inits.isInitialized("naver") ? new konkuk.nServer.domain.account.domain.QNaver(forProperty("naver"), inits.get("naver")) : null;
        this.password = inits.isInitialized("password") ? new konkuk.nServer.domain.account.domain.QPassword(forProperty("password"), inits.get("password")) : null;
    }

}

