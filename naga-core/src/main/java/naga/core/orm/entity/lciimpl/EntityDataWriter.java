package naga.core.orm.entity.lciimpl;

import naga.core.orm.domainmodel.DomainField;
import naga.core.orm.entity.Entity;
import naga.core.orm.entity.EntityStore;
import naga.core.orm.expression.lci.DataWriter;

/**
 * @author Bruno Salmon
 */
public class EntityDataWriter extends EntityDataReader implements DataWriter<Entity> {

    public EntityDataWriter(EntityStore entityStore) {
        super(entityStore);
    }

    @Override
    public void setDomainFieldValue(Entity entity, Object fieldId, Object fieldValue) {
        if (fieldId instanceof DomainField)
            fieldId = ((DomainField) fieldId).getId();
        entity.setFieldValue(fieldId, fieldValue);
    }

    @Override
    public void setParameterValue(String name, Object value) {

    }
}
