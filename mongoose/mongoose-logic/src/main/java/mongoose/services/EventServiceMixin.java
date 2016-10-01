package mongoose.services;

import mongoose.entities.*;
import naga.commons.util.async.Batch;
import naga.commons.util.async.Future;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;

/**
 * @author Bruno Salmon
 */
public interface EventServiceMixin extends EventService {

    EventService getEventService();

    default Future<Batch<EntityList>> loadEventOptions() {
        return getEventService().loadEventOptions();
    }

    default Event getEvent() {
        return getEventService().getEvent();
    }

    default <E extends Entity> EntityList<E> getEntityList(Object listId) {
        return getEventService().getEntityList(listId);
    }

}
