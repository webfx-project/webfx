package mongoose.services;

import mongoose.activities.shared.book.event.shared.FeesGroup;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.*;
import naga.util.async.Future;
import naga.util.function.Predicate;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;
import naga.platform.services.query.QueryResultSet;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface EventServiceMixin extends EventService {

    EventService getEventService();

    @Override
    default DataSourceModel getEventDataSourceModel() {
        return getEventService().getEventDataSourceModel();
    }

    @Override
    default EntityStore getEventStore() {
        return getEventService().getEventStore();
    }

    @Override
    default PersonService getPersonService() {
        return getEventService().getPersonService();
    }

    @Override
    default Future<EntityList<Option>> onEventOptions() {
        return getEventService().onEventOptions();
    }

    @Override
    default Event getEvent() {
        return getEventService().getEvent();
    }

    @Override
    default <E extends Entity> EntityList<E> getEntityList(Object listId) {
        return getEventService().getEntityList(listId);
    }

    @Override
    default void clearEntityList(Object listId) {
        getEventService().clearEntityList(listId);
    }

    @Override
    default EntityList<Option> getEventOptions() {
        return getEventService().getEventOptions();
    }

    @Override
    default void clearEventOptions() {
        getEventService().clearEventOptions();
    }

    @Override
    default EntityList<Site> getEventSites() {
        return getEventService().getEventSites();
    }

    @Override
    default EntityList<Rate> getEventRates() {
        return getEventService().getEventRates();
    }

    @Override
    default EntityList<DateInfo> getEventDateInfos() {
        return getEventService().getEventDateInfos();
    }

    @Override
    default <E extends Entity> List<E> selectEntities(Iterable<E> entities, Predicate<? super E> predicate) {
        return getEventService().selectEntities(entities, predicate);
    }

    @Override
    default List<Option> selectOptions(Predicate<? super Option> predicate) {
        return getEventService().selectOptions(predicate);
    }

    @Override
    default List<Option> selectDefaultOptions() {
        return getEventService().selectDefaultOptions();
    }

    @Override
    default List<Option> getChildrenOptions(Option parent) {
        return getEventService().getChildrenOptions(parent);
    }

    @Override
    default Option findFirstOption(Predicate<? super Option> predicate) {
        return getEventService().findFirstOption(predicate);
    }

    @Override
    default Option findFirstConcreteOption(Predicate<? super Option> predicate) {
        return getEventService().findFirstConcreteOption(predicate);
    }


    @Override
    default Option getBreakfastOption() {
        return getEventService().getBreakfastOption();
    }

    @Override
    default Option getDefaultDietOption() {
        return getEventService().getDefaultDietOption();
    }

    @Override
    default List<Rate> selectRates(Predicate<? super Rate> predicate) {
        return getEventService().selectRates(predicate);
    }

    default boolean hasUnemployedRate() {
        return getEventService().hasUnemployedRate();
    }

    default boolean hasFacilityFeeRate() {
        return getEventService().hasFacilityFeeRate();
    }

    default Future<FeesGroup[]> onFeesGroups() {
        return getEventService().onFeesGroups();
    }

    default FeesGroup[] getFeesGroups() {
        return getEventService().getFeesGroups();
    }

    @Override
    default Future<QueryResultSet> onEventAvailabilities() {
        return getEventService().onEventAvailabilities();
    }

    @Override
    default boolean areEventAvailabilitiesLoaded() {
        return getEventService().areEventAvailabilitiesLoaded();
    }

    @Override
    default QueryResultSet getEventAvailabilities() {
        return getEventService().getEventAvailabilities();
    }


    @Override
    default void setSelectedOptionsPreselection(OptionsPreselection selectedOptionsPreselection) {
        getEventService().setSelectedOptionsPreselection(selectedOptionsPreselection);
    }

    @Override
    default OptionsPreselection getSelectedOptionsPreselection() {
        return getEventService().getSelectedOptionsPreselection();
    }

    @Override
    default void setWorkingDocument(WorkingDocument workingDocument) {
        getEventService().setWorkingDocument(workingDocument);
    }

    @Override
    default WorkingDocument getWorkingDocument() {
        return getEventService().getWorkingDocument();
    }

    @Override
    default void setCurrentCart(Cart currentCart) {
        getEventService().setCurrentCart(currentCart);
    }

    @Override
    default Cart getCurrentCart() {
        return getEventService().getCurrentCart();
    }
}
