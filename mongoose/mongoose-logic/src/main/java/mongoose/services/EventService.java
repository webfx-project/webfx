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

    // Entity lists ids used to store event options
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

    DataSourceModel getEventDataSourceModel();  // Note: simply calling it getDataSourceModel() would cause a mixin clash with DomainActivityContextMixin in BookingProcessActivity

    // Event options loading method

    Future<EntityList<Option>> onEventOptions();

    // Event options accessors (once loaded)

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
        return selectOptions(o -> o.isConcrete() && (o.isTeaching() || (o.isMeals() ? mealsAreIncludedByDefault() : o.isObligatory())) && !o.isDependant());
    }

    default boolean mealsAreIncludedByDefault() {
        String eventName = getEvent().getName();
        return !eventName.contains("Day Course") && !eventName.contains("Public Talk");
    }

    default Option findFirstOption(Predicate<? super Option> predicate) {
        return Collections.findFirst(getEventOptions(), predicate);
    }

    default Option getBreakfastOption() {
        return findFirstOption(Option::isBreakfast);
    }

    default List<Rate> selectRates(Predicate<? super Rate> predicate) {
        return selectEntities(getEventRates(), predicate);
    }

    default Rate findFirstRate(Predicate<? super Rate> predicate) {
        return Collections.findFirst(getEventRates(), predicate);
    }

    default boolean hasUnemployedRate() {
        return findFirstRate(rate -> rate.getUnemployedPrice() != null || rate.getUnemployedDiscount() != null) != null;
    }

    default boolean hasFacilityFeeRate() {
        return findFirstRate(rate -> rate.getFacilityFeePrice() != null || rate.getFacilityFeeDiscount() != null) != null;
    }

    // Event availability loading method

    Future<QueryResultSet> onEventAvailabilities();

    // Event availability accessor

    default boolean areEventAvailabilitiesLoaded() {
        return getEventAvailabilities() != null;
    }

    QueryResultSet getEventAvailabilities();

}
