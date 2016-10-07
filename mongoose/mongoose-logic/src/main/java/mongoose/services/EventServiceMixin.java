package mongoose.services;

import mongoose.entities.*;
import naga.commons.util.async.Future;
import naga.commons.util.function.Predicate;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface EventServiceMixin extends EventService {

    EventService getEventService();

    default Future<EntityList<Option>> onEventOptions() {
        return getEventService().onEventOptions();
    }

    default Event getEvent() {
        return getEventService().getEvent();
    }

    default <E extends Entity> EntityList<E> getEntityList(Object listId) {
        return getEventService().getEntityList(listId);
    }

    @Override
    default EntityList<Option> getEventOptions() {
        return getEventService().getEventOptions();
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
    default List<Rate> selectRates(Predicate<? super Rate> predicate) {
        return getEventService().selectRates(predicate);
    }

    @Override
    default Option findFirstOption(Predicate<? super Option> predicate) {
        return getEventService().findFirstOption(predicate);
    }

    @Override
    default Option getBreakfastOption() {
        return getEventService().getBreakfastOption();
    }
}
