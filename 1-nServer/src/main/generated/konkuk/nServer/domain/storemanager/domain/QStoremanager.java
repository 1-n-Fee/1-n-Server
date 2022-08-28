package konkuk.nServer.domain.storemanager.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoremanager is a Querydsl query type for Storemanager
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoremanager extends EntityPathBase<Storemanager> {

    private static final long serialVersionUID = 1407297610L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoremanager storemanager = new QStoremanager("storemanager");

    public final EnumPath<konkuk.nServer.domain.account.domain.AccountType> accountType = createEnum("accountType", konkuk.nServer.domain.account.domain.AccountType.class);

    public final StringPath email = createString("email");

    public final konkuk.nServer.domain.account.domain.QGoogle google;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final konkuk.nServer.domain.account.domain.QKakao kakao;

    public final StringPath name = createString("name");

    public final konkuk.nServer.domain.account.domain.QNaver naver;

    public final konkuk.nServer.domain.account.domain.QPassword password;

    public final StringPath phone = createString("phone");

    public final EnumPath<konkuk.nServer.domain.user.domain.Role> role = createEnum("role", konkuk.nServer.domain.user.domain.Role.class);

    public final StringPath storeRegistrationNumber = createString("storeRegistrationNumber");

    public final ListPath<konkuk.nServer.domain.store.domain.Store, konkuk.nServer.domain.store.domain.QStore> stores = this.<konkuk.nServer.domain.store.domain.Store, konkuk.nServer.domain.store.domain.QStore>createList("stores", konkuk.nServer.domain.store.domain.Store.class, konkuk.nServer.domain.store.domain.QStore.class, PathInits.DIRECT2);

    public QStoremanager(String variable) {
        this(Storemanager.class, forVariable(variable), INITS);
    }

    public QStoremanager(Path<? extends Storemanager> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoremanager(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoremanager(PathMetadata metadata, PathInits inits) {
        this(Storemanager.class, metadata, inits);
    }

    public QStoremanager(Class<? extends Storemanager> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.google = inits.isInitialized("google") ? new konkuk.nServer.domain.account.domain.QGoogle(forProperty("google"), inits.get("google")) : null;
        this.kakao = inits.isInitialized("kakao") ? new konkuk.nServer.domain.account.domain.QKakao(forProperty("kakao"), inits.get("kakao")) : null;
        this.naver = inits.isInitialized("naver") ? new konkuk.nServer.domain.account.domain.QNaver(forProperty("naver"), inits.get("naver")) : null;
        this.password = inits.isInitialized("password") ? new konkuk.nServer.domain.account.domain.QPassword(forProperty("password"), inits.get("password")) : null;
    }

}

