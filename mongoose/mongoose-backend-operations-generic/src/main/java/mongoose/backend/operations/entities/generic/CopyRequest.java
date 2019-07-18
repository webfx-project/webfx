package mongoose.backend.operations.entities.generic;

import webfx.framework.client.ui.filter.ExpressionColumn;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.shared.operation.HasOperationExecutor;
import webfx.framework.shared.orm.entity.Entity;
import webfx.platform.shared.util.async.AsyncFunction;

import java.util.Collection;

abstract class CopyRequest implements HasOperationCode,
        HasOperationExecutor<CopyRequest, Void> {

    private final Collection<? extends Entity> entities;
    private final ExpressionColumn[] columns;

    CopyRequest(Collection<? extends Entity> entities, ExpressionColumn... columns) {
        this.entities = entities;
        this.columns = columns;
    }

    Collection<? extends Entity> getEntities() {
        return entities;
    }

    ExpressionColumn[] getColumns() {
        return columns;
    }

    @Override
    public AsyncFunction<CopyRequest, Void> getOperationExecutor() {
        return CopyExecutor::executeRequest;
    }
}
