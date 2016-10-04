package mongoose.services;

import mongoose.entities.Event;
import naga.commons.util.async.Future;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;

/**
 * @author Bruno Salmon
 */
public interface EventServiceMixin extends EventService {

    EventService getEventService();

    default Future<EntityList> onEventOptions() {
        return getEventService().onEventOptions();
    }

    default Event getEvent() {
        return getEventService().getEvent();
    }

    default <E extends Entity> EntityList<E> getEntityList(Object listId) {
        return getEventService().getEntityList(listId);
    }

}
