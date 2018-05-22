package mongoose.services;

import mongoose.activities.bothends.book.shared.FeesGroup;
import mongoose.activities.bothends.logic.preselection.OptionsPreselection;
import mongoose.activities.bothends.logic.work.WorkingDocument;
import mongoose.entities.*;
import naga.framework.orm.domainmodel.HasDataSourceModel;
import naga.platform.services.query.QueryResult;
import naga.util.async.Future;
import naga.util.collection.Collections;
import naga.util.function.Predicate;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface EventService extends HasDataSourceModel {

    // Entity lists ids used to store event options
    Object EVENTS_LIST_ID = "events";
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

    static EventService getOrCreate(Object eventId, EntityStore store) {
        return EventServiceImpl.getOrCreate(eventId, store);
    }

    static EventService getOrCreateFromDocument(Document document) {
        return getOrCreate(document.getEventId(), document.getStore());
    }

    EntityStore getEventStore();

    PersonService getPersonService();

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

    // Fees groups loading method

    Future<FeesGroup[]> onFeesGroups();

    FeesGroup[] getFeesGroups();

    // Event availability loading method

    Future<QueryResult> onEventAvailabilities();

    // Event availability accessor

    default boolean areEventAvailabilitiesLoaded() {
        return getEventAvailabilities() != null;
    }

    QueryResult getEventAvailabilities();

    // Setter and getter to store the booking process working document

    void setSelectedOptionsPreselection(OptionsPreselection selectedOptionsPreselection);

    OptionsPreselection getSelectedOptionsPreselection();

    void setWorkingDocument(WorkingDocument workingDocument);

    WorkingDocument getWorkingDocument();

    void setCurrentCart(Cart currentCart);

    Cart getCurrentCart();

}
