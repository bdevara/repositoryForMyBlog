package com.org.coop.retail.entities;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QGlSubHeader is a Querydsl query type for GlSubHeader
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGlSubHeader extends EntityPathBase<GlSubHeader> {

    private static final long serialVersionUID = -221480516L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGlSubHeader glSubHeader = new QGlSubHeader("glSubHeader");

    public final DateTimePath<java.sql.Timestamp> createDate = createDateTime("createDate", java.sql.Timestamp.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath deleteInd = createString("deleteInd");

    public final StringPath deleteReason = createString("deleteReason");

    public final QGlHeader glHeader;

    public final ListPath<GlMaster, QGlMaster> glMasters = this.<GlMaster, QGlMaster>createList("glMasters", GlMaster.class, QGlMaster.class, PathInits.DIRECT2);

    public final NumberPath<Integer> glSubHeaderCode = createNumber("glSubHeaderCode", Integer.class);

    public final StringPath glSubHeaderDesc = createString("glSubHeaderDesc");

    public final StringPath passingAuthInd = createString("passingAuthInd");

    public final StringPath passingAuthRemark = createString("passingAuthRemark");

    public final DateTimePath<java.sql.Timestamp> updateDate = createDateTime("updateDate", java.sql.Timestamp.class);

    public final StringPath updateUser = createString("updateUser");

    public QGlSubHeader(String variable) {
        this(GlSubHeader.class, forVariable(variable), INITS);
    }

    public QGlSubHeader(Path<? extends GlSubHeader> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGlSubHeader(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGlSubHeader(PathMetadata<?> metadata, PathInits inits) {
        this(GlSubHeader.class, metadata, inits);
    }

    public QGlSubHeader(Class<? extends GlSubHeader> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.glHeader = inits.isInitialized("glHeader") ? new QGlHeader(forProperty("glHeader")) : null;
    }

}

