package naga.framework.orm.entity.resultset;

import naga.framework.orm.entity.EntityId;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface EntityResultSet {

    Collection<EntityId> getEntityIds();

    Collection<Object> getFieldIds(EntityId id);

    Object getFieldValue(EntityId id, Object fieldId);
}
