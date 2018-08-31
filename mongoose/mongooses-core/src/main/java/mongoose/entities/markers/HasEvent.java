package mongoose.entities.markers;

import mongoose.entities.Event;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasEvent {

    void setEvent(Object event);

    EntityId getEventId();

    Event getEvent();

}
