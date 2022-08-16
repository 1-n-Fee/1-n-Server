package konkuk.nServer.domain.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGoogle is a Querydsl query type for Google
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGoogle extends EntityPathBase<Google> {

    private static final long serialVersionUID = 2013723052L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGoogle google = new QGoogle("google");

    public final StringPath googleId = createString("googleId");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final konkuk.nServer.domain.storemanager.domain.QStoremanager storemanager;

    public final konkuk.nServer.domain.user.domain.QUser user;

    public QGoogle(String variable) {
        this(Google.class, forVariable(variable), INITS);
    }

    public QGoogle(Path<? extends Google> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGoogle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGoogle(PathMetadata metadata, PathInits inits) {
        this(Google.class, metadata, inits);
    }

    public QGoogle(Class<? extends Google> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.storemanager = inits.isInitialized("storemanager") ? new konkuk.nServer.domain.storemanager.domain.QStoremanager(forProperty("storemanager"), inits.get("storemanager")) : null;
        this.user = inits.isInitialized("user") ? new konkuk.nServer.domain.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

