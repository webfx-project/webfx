package mongoose.backend.operations.entities.generic;

import webfx.framework.client.orm.entity.filter.table.EntityColumn;
import webfx.framework.shared.orm.entity.Entity;

import java.util.Collection;

public final class CopySelectionRequest extends CopyRequest {

    private final static String OPERATION_CODE = "CopySelection";

    public CopySelectionRequest(Collection<? extends Entity> entities, EntityColumn... columns) {
        super(entities, columns);
    }

    public Object getOperationCode() {
        return OPERATION_CODE;
    }

}
