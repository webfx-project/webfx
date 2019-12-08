package webfx.framework.shared.orm.entity.lciimpl;

import webfx.framework.shared.orm.entity.Entities;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.domainmodel.DomainField;
import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.extras.type.PrimType;

/**
 * @author Bruno Salmon
 */
public class EntityDomainReader<E extends Entity> implements DomainReader<E> {

    protected final EntityStore entityStore;

    public EntityDomainReader(EntityStore entityStore) {
        this.entityStore = entityStore;
    }

    @Override
    public E getDomainObjectFromId(Object id, Object src) {
        if (id instanceof Entity)
            return (E) id;
        E entity = entityStore.getEntity((EntityId) id);
        if (entity == null && src instanceof Entity)
            entity = ((Entity) src).getStore().getEntity((EntityId) id);
        return entity;
    }

    @Override
    public Object getDomainObjectId(Entity entity) {
        return entity == null ? null : entity.getId();
    }

    @Override
    public Object getDomainFieldValue(Entity entity, Object fieldId) {
        if (fieldId instanceof DomainField)
            fieldId = ((DomainField) fieldId).getId();
        return entity.getFieldValue(fieldId);
    }

    @Override
    public Object getParameterValue(String name) {
        return entityStore.getParameterValue(name);
    }

    @Override
    public Object prepareValueBeforeTypeConversion(Object value, PrimType type) {
        return Entities.getPrimaryKey(value);
    }
}
