package naga.core.orm.entity.resultset.impl;

import naga.core.orm.entity.EntityId;
import naga.core.orm.entity.resultset.EntityResultSet;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class EntityResultSetImpl implements EntityResultSet {

    private final List<EntityId> entityIds;
    private final List<Map> entityFieldsMaps;

    public EntityResultSetImpl(List<EntityId> entityIds, List<Map> entityFieldsMaps) {
        if (entityIds.size() != entityFieldsMaps.size())
            throw new IllegalArgumentException("entityIds and entityFieldsMaps must have the same size");
        this.entityIds = entityIds;
        this.entityFieldsMaps = entityFieldsMaps;
    }

    @Override
    public Collection<EntityId> getEntityIds() {
        return entityIds;
    }

    @Override
    public Collection<Object> getFieldIds(EntityId id) {
        int entityIndex = entityIds.indexOf(id);
        return entityIndex == -1 ? Collections.EMPTY_LIST : entityFieldsMaps.get(entityIndex).keySet();
    }

    @Override
    public Object getFieldValue(EntityId id, Object fieldId) {
        int entityIndex = entityIds.indexOf(id);
        return entityIndex == -1 ? null : entityFieldsMaps.get(entityIndex).get(fieldId);
    }
}
