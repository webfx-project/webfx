package naga.core.orm.entity.resultset;

import naga.core.orm.entity.EntityId;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface EntityChanges {

    EntityResultSet getInsertedUpdatedEntityResultSet();

    Collection<EntityId> getDeletedEntityIds();

}
