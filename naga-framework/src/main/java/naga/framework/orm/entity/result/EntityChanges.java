package naga.framework.orm.entity.result;

import naga.framework.orm.entity.EntityId;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface EntityChanges {

    EntityResult getInsertedUpdatedEntityResult();

    Collection<EntityId> getDeletedEntityIds();

}
