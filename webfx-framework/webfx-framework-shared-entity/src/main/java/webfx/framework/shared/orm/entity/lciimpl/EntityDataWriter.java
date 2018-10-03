package webfx.framework.shared.orm.entity.lciimpl;

import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.domainmodel.DomainField;
import webfx.framework.shared.expression.lci.DataWriter;

/**
 * @author Bruno Salmon
 */
public final class EntityDataWriter<E extends Entity> extends EntityDataReader<E> implements DataWriter<E> {

    public EntityDataWriter(EntityStore entityStore) {
        super(entityStore);
    }

    @Override
    public void setDomainFieldValue(E entity, Object fieldId, Object fieldValue) {
        if (fieldId instanceof DomainField)
            fieldId = ((DomainField) fieldId).getId();
        entity.setFieldValue(fieldId, fieldValue);
    }

    @Override
    public void setParameterValue(String name, Object value) {
    }
}
