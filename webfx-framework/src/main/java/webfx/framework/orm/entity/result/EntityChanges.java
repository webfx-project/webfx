package webfx.framework.orm.entity.result;

import webfx.framework.orm.entity.EntityId;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface EntityChanges {

    EntityResult getInsertedUpdatedEntityResult();

    Collection<EntityId> getDeletedEntityIds();

}
