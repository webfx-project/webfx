package mongoose.shared.entities.markers;

import mongoose.shared.entities.Event;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;

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
