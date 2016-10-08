package mongoose.services;

import mongoose.entities.*;
import naga.commons.util.async.Future;
import naga.commons.util.collection.Collections;
import naga.commons.util.function.Predicate;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;
import naga.platform.services.query.QueryResultSet;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface EventService {

    Object OPTIONS_LIST_ID = "options";
    Object SITES_LIST_ID = "sites";
    Object RATES_LIST_ID = "rates";
    Object DATE_INFOS_LIST_ID = "dateInfos";

    static EventService get(Object eventId) {
        return EventServiceImpl.get(eventId);
    }

    static EventService getOrCreate(Object eventId, DataSourceModel dataSourceModel) {
        return EventServiceImpl.getOrCreate(eventId, dataSourceModel);
    }

    Future<EntityList<Option>> onEventOptions();

    Future<QueryResultSet> onEventAvailabilities();

    QueryResultSet getEventAvailabilities();

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

    default <E extends Entity> List<E> selectEntities(Iterable<E> entities, Predicate<? super E> predicate) {
        return Collections.filter(entities, predicate);
    }

    default List<Option> selectOptions(Predicate<? super Option> predicate) {
        return selectEntities(getEventOptions(), predicate);
    }

    default List<Option> selectDefaultOptions() {
        return selectOptions(o -> o.isConcrete() && (o.isObligatory() || o.isTeaching() || o.isMeals()) && !o.isDependant());
    }

    default List<Rate> selectRates(Predicate<? super Rate> predicate) {
        return selectEntities(getEventRates(), predicate);
    }

    default Option findFirstOption(Predicate<? super Option> predicate) {
        return Collections.findFirst(getEventOptions(), predicate);
    }

    default Option getBreakfastOption() {
        return findFirstOption(Option::isBreakfast);
    }

}
