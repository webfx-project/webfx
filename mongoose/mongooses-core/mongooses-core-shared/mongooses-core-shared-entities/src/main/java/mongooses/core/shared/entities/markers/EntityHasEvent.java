package mongooses.core.shared.entities.markers;

import mongooses.core.shared.entities.Event;
import webfx.framework.orm.entity.Entity;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface EntityHasEvent extends Entity, HasEvent {

    @Override
    default void setEvent(Object event) {
        setForeignField("event", event);
    }

    @Override
    default EntityId getEventId() {
        return getForeignEntityId("event");
    }

    @Override
    default Event getEvent() {
        return getForeignEntity("event");
    }

}
