package mongoose.backend.operations.entities.generic;

import webfx.framework.client.orm.reactive.mapping.entities_to_grid.EntityColumn;
import webfx.framework.shared.orm.entity.Entity;

import java.util.Collection;

public final class CopyAllRequest extends CopyRequest {

    private final static String OPERATION_CODE = "CopyAll";

    public CopyAllRequest(Collection<? extends Entity> entities, EntityColumn... columns) {
        super(entities, columns);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
