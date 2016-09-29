package mongoose.services;

import naga.commons.util.async.Batch;
import naga.commons.util.async.Future;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.orm.domainmodel.DataSourceModel;
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
public class EventService {

    private static Map<Integer, EventService> services = new HashMap<>();

    public static EventService get(Integer eventId) {
        return services.get(eventId);
    }

    public static EventService getOrCreate(Integer eventId, DataSourceModel dataSourceModel) {
        EventService service = get(eventId);
        if (service == null)
            services.put(eventId, service = new EventService(eventId, dataSourceModel));
        return service;
    }

    private final Integer eventId;
    private final DataSourceModel dataSourceModel;
    private final EntityStore eventStore;

    public EventService(Integer eventId, DataSourceModel dataSourceModel) {
        this.eventId = eventId;
        this.dataSourceModel = dataSourceModel;
        eventStore = EntityStore.create(dataSourceModel);
    }

    public Future<Batch<EntityList>> loadEventOptions() {
        String host = getHost();
        Object[] parameters = {eventId, host, host, false /* isDeveloper */};
        // Loading event options
        String optionCondition = "event.(id=? and (host=null or host=? or ?='localhost')) and online and (!dev or ?=true)";
        String siteIds = "(select site.id from Option where " + optionCondition + ")";
        String rateCondition = "site.id in " + siteIds + " and (startDate is null or startDate <= site.event.endDate) and (endDate is null or endDate >= site.event.startDate) and (onDate is null or onDate <= now()) and (offDate is null or offDate > now())";
        return executeParallelEventQueries(
                new EventQuery("options",   "select <frontend_loadEvent> from Option where " + optionCondition + " order by ord", parameters),
                new EventQuery("sites",     "select <frontend_loadEvent> from Site where id in " + siteIds, parameters),
                new EventQuery("rates",     "select <frontend_loadEvent> from Rate where " + rateCondition, parameters),
                new EventQuery("dateInfos", "select <frontend_loadEvent> from DateInfo where event=? order by id", eventId)
        );
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
        Future<EntityList> future = Future.future();
        SqlCompiled sqlCompiled = dataSourceModel.getDomainModel().compileSelect(eventQuery.queryString, eventQuery.parameters);
        Platform.getQueryService().executeQuery(new QueryArgument(sqlCompiled.getSql(), eventQuery.parameters, dataSourceModel.getId()))
                .setHandler(asyncResult -> {
                    if (asyncResult.failed())
                        future.fail(asyncResult.cause());
                    else
                        future.complete(QueryResultSetToEntityListGenerator.createEntityList(asyncResult.result(), sqlCompiled.getQueryMapping(), eventStore, eventQuery.listId));
                });
        return future;
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
