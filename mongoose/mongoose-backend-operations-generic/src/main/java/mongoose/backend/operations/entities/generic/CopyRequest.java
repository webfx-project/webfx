package mongoose.backend.operations.entities.generic;

import webfx.framework.client.orm.reactive.mapping.entities_to_grid.EntityColumn;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.shared.operation.HasOperationExecutor;
import webfx.framework.shared.orm.entity.Entity;
import webfx.platform.shared.util.async.AsyncFunction;

import java.util.Collection;

abstract class CopyRequest implements HasOperationCode,
        HasOperationExecutor<CopyRequest, Void> {

    private final Collection<? extends Entity> entities;
    private final EntityColumn[] columns;

    CopyRequest(Collection<? extends Entity> entities, EntityColumn... columns) {
        this.entities = entities;
        this.columns = columns;
    }

    Collection<? extends Entity> getEntities() {
        return entities;
    }

    EntityColumn[] getColumns() {
        return columns;
    }

    @Override
    public AsyncFunction<CopyRequest, Void> getOperationExecutor() {
        return CopyExecutor::executeRequest;
    }
}
