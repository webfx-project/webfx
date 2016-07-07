package naga.core.orm.entity.resultset;

import naga.core.orm.entity.EntityId;
import naga.core.orm.entity.resultset.impl.EntityResultSetImpl;
import naga.core.util.collection.HashList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class EntityResultSetBuilder {

    private final List<EntityId> entityIds = new HashList<>();
    private final List<Map> entityFieldsMaps = new ArrayList<>();

    private EntityResultSetBuilder() {
    }

    public EntityResultSetBuilder setFieldValue(EntityId id, Object fieldId, Object fieldValue) {
        entityFieldMap(id).put(fieldId, fieldValue);
        return this;
    }

    public EntityResultSetBuilder unsetFieldValue(EntityId id, Object fieldId) {
        entityFieldMap(id).remove(fieldId);
        return this;
    }

    private Map entityFieldMap(EntityId id) {
        int entityIndex = entityIds.indexOf(id);
        Map entityFieldsMap;
        if (entityIndex != -1)
            entityFieldsMap = entityFieldsMaps.get(entityIndex);
        else {
            entityIds.add(id);
            entityFieldsMaps.add(entityFieldsMap = new HashMap());
        }
        return entityFieldsMap;
    }

    public EntityResultSet build() {
        return new EntityResultSetImpl(entityIds, entityFieldsMaps);
    }

    public static EntityResultSetBuilder create() {
        return new EntityResultSetBuilder();
    }
}
