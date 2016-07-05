package naga.core.orm.entity;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class DynamicEntity implements Entity {

    private final EntityId id;
    private final EntityStore store;
    private final Map<Object, Object> fieldValues = new HashMap<>();

    DynamicEntity(EntityId id, EntityStore store) {
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
    public void setFieldValue(Object domainFieldId, Object value) {
        fieldValues.put(domainFieldId, value);
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }

    public StringBuilder toString(StringBuilder sb) {
        sb.append(id.getDomainClassId()).append("(pk: ").append(id.getPrimaryKey());
        for (Map.Entry entry : fieldValues.entrySet())
            sb.append(", ").append(entry.getKey()).append(": ").append(entry.getValue());
        sb.append(')');
        return sb;
    }

}
