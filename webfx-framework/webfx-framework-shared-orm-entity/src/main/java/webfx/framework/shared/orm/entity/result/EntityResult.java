package webfx.framework.shared.orm.entity.result;

import webfx.framework.shared.orm.entity.EntityId;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface EntityResult {

    Collection<EntityId> getEntityIds();

    Collection<Object> getFieldIds(EntityId id);

    Object getFieldValue(EntityId id, Object fieldId);
}
