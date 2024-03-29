package konkuk.nServer.domain.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPassword is a Querydsl query type for Password
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPassword extends EntityPathBase<Password> {

    private static final long serialVersionUID = 1543783758L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPassword password1 = new QPassword("password1");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath password = createString("password");

    public final konkuk.nServer.domain.storemanager.domain.QStoremanager storemanager;

    public final konkuk.nServer.domain.user.domain.QUser user;

    public QPassword(String variable) {
        this(Password.class, forVariable(variable), INITS);
    }

    public QPassword(Path<? extends Password> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPassword(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPassword(PathMetadata metadata, PathInits inits) {
        this(Password.class, metadata, inits);
    }

    public QPassword(Class<? extends Password> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.storemanager = inits.isInitialized("storemanager") ? new konkuk.nServer.domain.storemanager.domain.QStoremanager(forProperty("storemanager"), inits.get("storemanager")) : null;
        this.user = inits.isInitialized("user") ? new konkuk.nServer.domain.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

