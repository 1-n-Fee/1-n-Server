package konkuk.nServer.domain.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNaver is a Querydsl query type for Naver
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNaver extends EntityPathBase<Naver> {

    private static final long serialVersionUID = 209560381L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNaver naver = new QNaver("naver");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath naverId = createString("naverId");

    public final konkuk.nServer.domain.storemanager.domain.QStoremanager storemanager;

    public final konkuk.nServer.domain.user.domain.QUser user;

    public QNaver(String variable) {
        this(Naver.class, forVariable(variable), INITS);
    }

    public QNaver(Path<? extends Naver> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNaver(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNaver(PathMetadata metadata, PathInits inits) {
        this(Naver.class, metadata, inits);
    }

    public QNaver(Class<? extends Naver> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.storemanager = inits.isInitialized("storemanager") ? new konkuk.nServer.domain.storemanager.domain.QStoremanager(forProperty("storemanager"), inits.get("storemanager")) : null;
        this.user = inits.isInitialized("user") ? new konkuk.nServer.domain.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

