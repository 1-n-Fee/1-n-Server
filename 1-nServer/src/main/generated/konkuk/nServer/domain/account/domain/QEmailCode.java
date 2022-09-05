package konkuk.nServer.domain.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmailCode is a Querydsl query type for EmailCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailCode extends EntityPathBase<EmailCode> {

    private static final long serialVersionUID = -634254506L;

    public static final QEmailCode emailCode = new QEmailCode("emailCode");

    public final StringPath code = createString("code");

    public final DateTimePath<java.time.LocalDateTime> createDateTime = createDateTime("createDateTime", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> expireDateTime = createDateTime("expireDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QEmailCode(String variable) {
        super(EmailCode.class, forVariable(variable));
    }

    public QEmailCode(Path<? extends EmailCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmailCode(PathMetadata metadata) {
        super(EmailCode.class, metadata);
    }

}

