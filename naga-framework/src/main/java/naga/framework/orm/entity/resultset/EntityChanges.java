package naga.framework.orm.entity.resultset;

import naga.framework.orm.entity.EntityId;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface EntityChanges {

    EntityResultSet getInsertedUpdatedEntityResultSet();

    Collection<EntityId> getDeletedEntityIds();

}
