package konkuk.nServer.domain.user.domain;

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

    private static final long serialVersionUID = -744620823L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoremanager storemanager = new QStoremanager("storemanager");

    public final EnumPath<AccountType> accountType = createEnum("accountType", AccountType.class);

    public final StringPath email = createString("email");

    public final QGoogle google;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QKakao kakao;

    public final StringPath name = createString("name");

    public final QNaver naver;

    public final QPassword password;

    public final StringPath phone = createString("phone");

    public final EnumPath<Role> role = createEnum("role", Role.class);

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
        this.google = inits.isInitialized("google") ? new QGoogle(forProperty("google"), inits.get("google")) : null;
        this.kakao = inits.isInitialized("kakao") ? new QKakao(forProperty("kakao"), inits.get("kakao")) : null;
        this.naver = inits.isInitialized("naver") ? new QNaver(forProperty("naver"), inits.get("naver")) : null;
        this.password = inits.isInitialized("password") ? new QPassword(forProperty("password"), inits.get("password")) : null;
    }

}

