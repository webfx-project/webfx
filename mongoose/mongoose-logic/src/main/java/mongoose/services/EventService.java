package mongoose.services;

import mongoose.entities.*;
import naga.commons.util.async.Batch;
import naga.commons.util.async.Future;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;

/**
 * @author Bruno Salmon
 */
public interface EventService {

    Object OPTIONS_LIST_ID = "options";
    Object SITES_LIST_ID = "sites";
    Object RATES_LIST_ID = "rates";
    Object DATE_INFOS_LIST_ID = "dateInfos";

    static EventService get(Integer eventId) {
        return EventServiceImpl.get(eventId);
    }

    static EventService getOrCreate(Integer eventId, DataSourceModel dataSourceModel) {
        return EventServiceImpl.getOrCreate(eventId, dataSourceModel);
    }

    Future<Batch<EntityList>> loadEventOptions();

    Event getEvent();

    <E extends Entity> EntityList<E> getEntityList(Object listId);

    default EntityList<Option> getEventOptions() {
        return getEntityList(OPTIONS_LIST_ID);
    }

    default EntityList<Site> getEventSites() {
        return getEntityList(SITES_LIST_ID);
    }

    default EntityList<Rate> getEventRates() {
        return getEntityList(RATES_LIST_ID);
    }

    default EntityList<DateInfo> getEventDateInfos() {
        return getEntityList(DATE_INFOS_LIST_ID);
    }
}
