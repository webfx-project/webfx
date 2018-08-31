package webfx.framework.orm.entity.result;

import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.result.impl.EntityResultImpl;
import webfx.util.collection.HashList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class EntityResultBuilder {

    private final List<EntityId> entityIds = new HashList<>();
    private final List<Map> entityFieldsMaps = new ArrayList<>();

    private EntityResultBuilder() {
    }

    public boolean setFieldValue(EntityId id, Object fieldId, Object fieldValue) {
        Map fieldMap = entityFieldMap(id);
        boolean firstFieldValueSet = !fieldMap.containsKey(fieldId);
        fieldMap.put(fieldId, fieldValue);
        return firstFieldValueSet;
    }

    EntityResultBuilder unsetFieldValue(EntityId id, Object fieldId) {
        entityFieldMap(id).remove(fieldId);
        return this;
    }

    boolean hasEntityId(EntityId id) {
        return entityIds.contains(id);
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

    public EntityResult build() {
        return new EntityResultImpl(entityIds, entityFieldsMaps);
    }

    public static EntityResultBuilder create() {
        return new EntityResultBuilder();
    }
}
