package naga.core.orm.entity.resultset;

import naga.core.orm.entity.EntityId;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface EntityResultSet {

    Collection<EntityId> getEntityIds();

    Collection<Object> getFieldIds(EntityId id);

    Object getFieldValue(EntityId id, Object fieldId);
}
