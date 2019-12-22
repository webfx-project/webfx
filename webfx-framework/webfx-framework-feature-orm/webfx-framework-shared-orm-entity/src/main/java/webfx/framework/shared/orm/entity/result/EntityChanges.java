package webfx.framework.shared.orm.entity.result;

import webfx.framework.shared.orm.entity.EntityId;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface EntityChanges {

    EntityResult getInsertedUpdatedEntityResult();

    Collection<EntityId> getDeletedEntityIds();

}
