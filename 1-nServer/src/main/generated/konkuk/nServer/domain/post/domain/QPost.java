package konkuk.nServer.domain.post.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -2077839310L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final EnumPath<Category> category = createEnum("category", Category.class);

    public final DateTimePath<java.time.LocalDateTime> closeTime = createDateTime("closeTime", java.time.LocalDateTime.class);

    public final StringPath content = createString("content");

    public final NumberPath<Integer> currentNumber = createNumber("currentNumber", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> limitNumber = createNumber("limitNumber", Integer.class);

    public final EnumPath<PostProcess> process = createEnum("process", PostProcess.class);

    public final DateTimePath<java.time.LocalDateTime> registryTime = createDateTime("registryTime", java.time.LocalDateTime.class);

    public final EnumPath<Spot> spot = createEnum("spot", Spot.class);

    public final konkuk.nServer.domain.store.domain.QStore store;

    public final konkuk.nServer.domain.user.domain.QUser user;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new konkuk.nServer.domain.store.domain.QStore(forProperty("store")) : null;
        this.user = inits.isInitialized("user") ? new konkuk.nServer.domain.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

