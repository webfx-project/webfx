package webfx.framework.orm.entity.lciimpl;

import webfx.framework.orm.domainmodel.DomainField;
import webfx.framework.orm.entity.Entity;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.expression.lci.DataWriter;

/**
 * @author Bruno Salmon
 */
public class EntityDataWriter<E extends Entity> extends EntityDataReader<E> implements DataWriter<E> {

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
