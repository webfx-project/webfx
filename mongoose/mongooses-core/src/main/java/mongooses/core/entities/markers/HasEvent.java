package mongooses.core.entities.markers;

import mongooses.core.entities.Event;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasEvent {

    void setEvent(Object event);

    EntityId getEventId();

    Event getEvent();

}
