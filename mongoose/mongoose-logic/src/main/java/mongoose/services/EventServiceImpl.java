package mongoose.services;

import mongoose.entities.*;
import naga.commons.util.async.Batch;
import naga.commons.util.async.Future;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.mapping.QueryResultSetToEntityListGenerator;
import naga.platform.client.bus.WebSocketBusOptions;
import naga.platform.services.query.QueryArgument;
import naga.platform.spi.Platform;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
class EventServiceImpl implements EventService {

    private static Map<Integer, EventService> services = new HashMap<>();

    static EventService get(Integer eventId) {
        return EventServiceImpl.services.get(eventId);
    }

    static EventService getOrCreate(Integer eventId, DataSourceModel dataSourceModel) {
        EventService service = get(eventId);
        if (service == null)
            EventServiceImpl.services.put(eventId, service = new EventServiceImpl(eventId, dataSourceModel));
        return service;
    }

    private final Integer eventId;
    private final DataSourceModel dataSourceModel;
    private final EntityStore eventStore;

    public EventServiceImpl(Integer eventId, DataSourceModel dataSourceModel) {
        this.eventId = eventId;
        this.dataSourceModel = dataSourceModel;
        eventStore = EntityStore.create(dataSourceModel);
    }

    @Override
    public Future<EntityList<Option>> onEventOptions() {
        EntityList<Option> options = getEventOptions();
        if (options != null)
            return Future.succeededFuture(options);
        String host = getHost();
        Object[] parameters = {eventId, host, host, false /* isDeveloper */};
        // Loading event options
        String optionCondition = "event.(id=? and (host=null or host=? or ?='localhost')) and online and (!dev or ?=true)";
        String siteIds = "(select site.id from Option where " + optionCondition + ")";
        String rateCondition = "site.id in " + siteIds + " and (startDate is null or startDate <= site.event.endDate) and (endDate is null or endDate >= site.event.startDate) and (onDate is null or onDate <= now()) and (offDate is null or offDate > now())";
        return executeParallelEventQueries(
                new EventQuery(OPTIONS_LIST_ID,    "select <frontend_loadEvent> from Option where " + optionCondition + " order by ord", parameters),
                new EventQuery(SITES_LIST_ID,      "select <frontend_loadEvent> from Site where id in " + siteIds, parameters),
                new EventQuery(RATES_LIST_ID,      "select <frontend_loadEvent> from Rate where " + rateCondition, parameters),
                new EventQuery(DATE_INFOS_LIST_ID, "select <frontend_loadEvent> from DateInfo where event=? order by id", eventId)
        ).map(this::getEventOptions);
    }

    @Override
    public Event getEvent() {
        return eventStore.getEntity("Event", eventId);
    }

    @Override
    public <E extends Entity> EntityList<E> getEntityList(Object listId) {
        return eventStore.getEntityList(listId);
    }

    private static String getHost() {
        return ((WebSocketBusOptions) Platform.getBusOptions()).getServerHost();
    }

    private Future<Batch<EntityList>> executeParallelEventQueries(EventQuery... eventQueries) {
        return executeParallelEventQueries(new Batch<>(eventQueries));
    }

    private Future<Batch<EntityList>> executeParallelEventQueries(Batch<EventQuery> batch) {
        return batch.executeParallel(EntityList[]::new, this::executeEventQuery);
    }

    public Future<EntityList> executeEventQuery(String queryString, Object[] parameters, Object listId) {
        return executeEventQuery(new EventQuery(listId, queryString, parameters));
    }

    private Future<EntityList> executeEventQuery(EventQuery eventQuery) {
        SqlCompiled sqlCompiled = dataSourceModel.getDomainModel().compileSelect(eventQuery.queryString, eventQuery.parameters);
        return Platform.getQueryService().executeQuery(new QueryArgument(sqlCompiled.getSql(), eventQuery.parameters, dataSourceModel.getId()))
                .map(rs ->  QueryResultSetToEntityListGenerator.createEntityList(rs, sqlCompiled.getQueryMapping(), eventStore, eventQuery.listId));
    }

    private static class EventQuery {
        Object listId;
        String queryString;
        Object[] parameters;

        EventQuery(Object listId, String queryString, Object... parameters) {
            this.listId = listId;
            this.queryString = queryString;
            this.parameters = parameters;
        }
    }
}
