package mongoose.client.aggregates.event;

import mongoose.client.aggregates.person.PersonAggregate;
import mongoose.shared.entities.*;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.HasDataSourceModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.collection.Collections;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
public interface EventAggregate extends HasDataSourceModel {

    // Entity lists ids used to store event options
    Object EVENTS_LIST_ID = "events";
    Object OPTIONS_LIST_ID = "options";
    Object SITES_LIST_ID = "sites";
    Object RATES_LIST_ID = "rates";
    Object DATE_INFOS_LIST_ID = "dateInfos";

    static EventAggregate get(Object eventId) {
        return EventAggregateImpl.get(eventId);
    }

    static EventAggregate getOrCreate(Object eventId, DataSourceModel dataSourceModel) {
        return EventAggregateImpl.getOrCreate(eventId, dataSourceModel);
    }

    static EventAggregate getOrCreate(Object eventId, EntityStore store) {
        return EventAggregateImpl.getOrCreate(eventId, store);
    }

    static EventAggregate getOrCreateFromDocument(Document document) {
        return getOrCreate(document.getEventId(), document.getStore());
    }

    EntityStore getEventStore();

    PersonAggregate getPersonAggregate();

    // Event loading method

    Future<Event> onEvent();

    // Event accessor (once loaded)

    Event getEvent();
    // Event options loading method

    Future<EntityList<Option>> onEventOptions();

    // Event options accessors (once loaded)

    default EntityList<Option> getEventOptions() {
        return getEntityList(OPTIONS_LIST_ID);
    }

    void clearEventOptions();

    <E extends Entity> EntityList<E> getEntityList(Object listId);

    void clearEntityList(Object listId);

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

    List<Option> getChildrenOptions(Option parent);

    default Option findFirstOption(Predicate<? super Option> predicate) {
        return Collections.findFirst(getEventOptions(), predicate);
    }

    default Option findFirstConcreteOption(Predicate<? super Option> predicate) {
        return findFirstOption(option -> option.isConcrete() && predicate.test(option));
    }

    Option getBreakfastOption();

    void setBreakfastOption(Option breakfastOption);

    Option getDefaultDietOption();

    void setDefaultDietOption(Option defaultDietOption);

    default List<Rate> selectRates(Predicate<? super Rate> predicate) {
        return selectEntities(getEventRates(), predicate);
    }

    boolean hasUnemployedRate();

    boolean hasFacilityFeeRate();

    // Event availability loading method

    Future<QueryResult> onEventAvailabilities();

    // Event availability accessor

    default boolean areEventAvailabilitiesLoaded() {
        return getEventAvailabilities() != null;
    }

    QueryResult getEventAvailabilities();

    void setActiveCart(Cart activeCart);

    Cart getActiveCart();

}
