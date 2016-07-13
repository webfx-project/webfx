package naga.framework.orm.entity.impl;


import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.UpdateStore;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class DynamicEntity implements Entity {

    private final EntityId id;
    private final EntityStore store;
    private final Map<Object, Object> fieldValues = new HashMap<>();

    protected DynamicEntity(EntityId id, EntityStore store) {
        this.id = id;
        this.store = store;
    }

    @Override
    public EntityId getId() {
        return id;
    }

    @Override
    public EntityStore getStore() {
        return store;
    }

    @Override
    public Object getFieldValue(Object domainFieldId) {
        return fieldValues.get(domainFieldId);
    }

    @Override
    public EntityId getForeignEntityId(Object foreignFieldId) {
        Object value = getFieldValue(foreignFieldId);
        if (value instanceof EntityId)
            return (EntityId) value;
        return null;
    }

    @Override
    public void setFieldValue(Object domainFieldId, Object value) {
        fieldValues.put(domainFieldId, value);
        if (store instanceof UpdateStore)
            ((UpdateStoreImpl) store).updateEntity(id, domainFieldId, value);
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }

    public StringBuilder toString(StringBuilder sb) {
        sb.append(id.getDomainClass()).append("(pk: ").append(id.getPrimaryKey());
        for (Map.Entry entry : fieldValues.entrySet())
            sb.append(", ").append(entry.getKey()).append(": ").append(entry.getValue());
        sb.append(')');
        return sb;
    }

}
