package konkuk.nServer.domain.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QKakao is a Querydsl query type for Kakao
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKakao extends EntityPathBase<Kakao> {

    private static final long serialVersionUID = 1816120870L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QKakao kakao = new QKakao("kakao");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath kakaoId = createString("kakaoId");

    public final QUser user;

    public QKakao(String variable) {
        this(Kakao.class, forVariable(variable), INITS);
    }

    public QKakao(Path<? extends Kakao> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QKakao(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QKakao(PathMetadata metadata, PathInits inits) {
        this(Kakao.class, metadata, inits);
    }

    public QKakao(Class<? extends Kakao> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

