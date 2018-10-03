package mongoose.client.aggregates;

import mongoose.client.activities.shared.FeesGroup;
import mongoose.client.businesslogic.preselection.OptionsPreselection;
import mongoose.client.businesslogic.workingdocument.WorkingDocument;
import mongoose.shared.entities.*;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.util.async.Future;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
public interface EventAggregateMixin extends EventAggregate {

    EventAggregate getEventService();

/* Commented to avoid clash with DomainActivityContextMixin
    @Override
    default DataSourceModel getDataSourceModel() {
        return getEventAggregate().getDataSourceModel();
    }
*/

    @Override
    default EntityStore getEventStore() {
        return getEventService().getEventStore();
    }

    @Override
    default PersonAggregate getPersonAggregate() {
        return getEventService().getPersonAggregate();
    }

    @Override
    default Future<Event> onEvent() {
        return getEventService().onEvent();
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
    default void setBreakfastOption(Option breakfastOption) {
        getEventService().setBreakfastOption(breakfastOption);
    }

    @Override
    default Option getDefaultDietOption() {
        return getEventService().getDefaultDietOption();
    }

    @Override
    default void setDefaultDietOption(Option defaultDietOption) {
        getEventService().setDefaultDietOption(defaultDietOption);
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
    default Future<QueryResult> onEventAvailabilities() {
        return getEventService().onEventAvailabilities();
    }

    @Override
    default boolean areEventAvailabilitiesLoaded() {
        return getEventService().areEventAvailabilitiesLoaded();
    }

    @Override
    default QueryResult getEventAvailabilities() {
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
