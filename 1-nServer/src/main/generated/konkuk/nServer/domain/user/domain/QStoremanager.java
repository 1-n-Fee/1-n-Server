package konkuk.nServer.domain.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStoremanager is a Querydsl query type for Storemanager
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoremanager extends EntityPathBase<Storemanager> {

    private static final long serialVersionUID = -744620823L;

    public static final QStoremanager storemanager = new QStoremanager("storemanager");

    public final EnumPath<AccountType> accountType = createEnum("accountType", AccountType.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath phone = createString("phone");

    public final StringPath storeRegistrationNumber = createString("storeRegistrationNumber");

    public QStoremanager(String variable) {
        super(Storemanager.class, forVariable(variable));
    }

    public QStoremanager(Path<? extends Storemanager> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStoremanager(PathMetadata metadata) {
        super(Storemanager.class, metadata);
    }

}

