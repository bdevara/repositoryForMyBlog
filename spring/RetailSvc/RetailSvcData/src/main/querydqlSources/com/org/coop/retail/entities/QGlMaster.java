package com.org.coop.retail.entities;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QGlMaster is a Querydsl query type for GlMaster
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGlMaster extends EntityPathBase<GlMaster> {

    private static final long serialVersionUID = -1004840973L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGlMaster glMaster = new QGlMaster("glMaster");

    public final NumberPath<Integer> annexureId = createNumber("annexureId", Integer.class);

    public final ListPath<BankMaster, QBankMaster> bankMasters = this.<BankMaster, QBankMaster>createList("bankMasters", BankMaster.class, QBankMaster.class, PathInits.DIRECT2);

    public final DateTimePath<java.sql.Timestamp> createDate = createDateTime("createDate", java.sql.Timestamp.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath deleteInd = createString("deleteInd");

    public final StringPath deleteReason = createString("deleteReason");

    public final ListPath<GlLedgerDtl, QGlLedgerDtl> glLedgerDtls = this.<GlLedgerDtl, QGlLedgerDtl>createList("glLedgerDtls", GlLedgerDtl.class, QGlLedgerDtl.class, PathInits.DIRECT2);

    public final NumberPath<Integer> glMasCode = createNumber("glMasCode", Integer.class);

    public final StringPath glMasDesc = createString("glMasDesc");

    public final QGlSubHeader glSubHeader;

    public final StringPath passingAuthInd = createString("passingAuthInd");

    public final StringPath passingAuthRemark = createString("passingAuthRemark");

    public final DateTimePath<java.sql.Timestamp> updateDate = createDateTime("updateDate", java.sql.Timestamp.class);

    public final StringPath updateUser = createString("updateUser");

    public QGlMaster(String variable) {
        this(GlMaster.class, forVariable(variable), INITS);
    }

    public QGlMaster(Path<? extends GlMaster> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGlMaster(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGlMaster(PathMetadata<?> metadata, PathInits inits) {
        this(GlMaster.class, metadata, inits);
    }

    public QGlMaster(Class<? extends GlMaster> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.glSubHeader = inits.isInitialized("glSubHeader") ? new QGlSubHeader(forProperty("glSubHeader"), inits.get("glSubHeader")) : null;
    }

}

