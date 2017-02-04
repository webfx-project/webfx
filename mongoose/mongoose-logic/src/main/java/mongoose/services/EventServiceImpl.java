package mongoose.services;

import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.*;
import naga.commons.util.Numbers;
import naga.commons.util.Objects;
import naga.commons.util.async.Batch;
import naga.commons.util.async.Future;
import naga.commons.util.collection.Collections;
import naga.commons.util.function.Predicate;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.mapping.QueryResultSetToEntityListGenerator;
import naga.platform.client.bus.WebSocketBusOptions;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;
import naga.platform.spi.Platform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
class EventServiceImpl implements EventService {

    private static Map<Object, EventService> services = new HashMap<>();

    static EventService get(Object eventId) {
        return EventServiceImpl.services.get(eventId);
    }

    static EventService getOrCreate(Object eventId, DataSourceModel dataSourceModel) {
        EventService service = get(eventId);
        if (service == null)
            EventServiceImpl.services.put(eventId, service = new EventServiceImpl(eventId, dataSourceModel));
        return service;
    }

    private final Object eventId;
    private final DataSourceModel dataSourceModel;
    private final EntityStore store;
    private Event event;

    EventServiceImpl(Object eventId, DataSourceModel dataSourceModel) {
        this.eventId = eventId;
        this.dataSourceModel = dataSourceModel;
        store = EntityStore.create(dataSourceModel);
    }

    @Override
    public DataSourceModel getEventDataSourceModel() {
        return dataSourceModel;
    }

    // Event options loading method

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

    // Event options accessors (once loaded)

    @Override
    public Event getEvent() {
        if (event == null) {
            event = store.getEntity("Event", eventId); // eventId may be from the wrong type (ex: String) because coming from the url
            if (event == null) // If not found, trying now with integer (should work for Java platforms)
                event = store.getEntity("Event", Numbers.toInteger(eventId));
            if (event == null) // If not found, trying now with double (should work for Web platforms)
                event = store.getEntity("Event", Numbers.toDouble(eventId));
        }
        return event;
    }

    @Override
    public <E extends Entity> EntityList<E> getEntityList(Object listId) {
        return store.getEntityList(listId);
    }

    @Override
    public void clearEntityList(Object listId) {
        store.clearEntityList(listId);
    }

    public List<Option> selectDefaultOptions() {
        return selectOptions(o -> o.isIncludedByDefault() && (o.isTeaching() || (o.isMeals() ? mealsAreIncludedByDefault() : o.isObligatory())) && !o.isDependant());
    }

    private boolean mealsAreIncludedByDefault() {
        String eventName = getEvent().getName();
        return !eventName.contains("Day Course") && !eventName.contains("Public Talk");
    }


    //// Breakfast option

    private Option breakfastOption;

    @Override
    public Option getBreakfastOption() {
        if (breakfastOption == null)
            breakfastOption = findFirstOption(Option::isBreakfast);
        return breakfastOption;
    }

    private Rate findFirstRate(Predicate<? super Rate> predicate) {
        return Collections.findFirst(getEventRates(), predicate);
    }

    private boolean hasRate(Predicate<? super Rate> predicate) {
        return findFirstRate(predicate) != null;
    }

    private Boolean hasUnemployedRate;
    @Override
    public boolean hasUnemployedRate() {
        if (hasUnemployedRate == null)
            hasUnemployedRate = hasRate(rate -> Objects.anyNotNull(rate.getUnemployedPrice(), rate.getUnemployedDiscount()));
        return hasUnemployedRate;
    }

    private Boolean hasFacilityFeeRate;
    @Override
    public boolean hasFacilityFeeRate() {
        if (hasFacilityFeeRate == null)
            hasFacilityFeeRate = hasRate(rate -> Objects.anyNotNull(rate.getFacilityFeePrice(), rate.getFacilityFeeDiscount()));
        return hasFacilityFeeRate;
    }

    // Event availability loading method

    @Override
    public Future<QueryResultSet> onEventAvailabilities() {
        return executeQuery(
                // getting all resource availabilities (per site, per item, per day) for this event
                "with ra as (select * from resource_availability_by_event_items(?) where max>0)," + // resources with max(=max_online)=0 (like private rooms) are not displayed in the frontend
                        // let's see if some options for this event require to have the per day availabilities details
                        " pda as (select site_id,item_id,item_family_id from option where per_day_availability and event_id=?)" +
                        // for such options we keep all the details: site, item and date (this applies to availabilities having site=option.site and item=option.item if set, item_family=item.family otherwise)
                        " (select row_number,      site_id as site,      item_id as item,      date,         max - current as available,      i.ord as ord      from ra join item i on i.id=item_id where     exists(select * from pda where site_id=ra.site_id and (item_id=ra.item_id or item_id is null and item_family_id=i.family_id)) )" +
                        " union " + // union of both queries
                        // for others, we group by site and item (=> dates disappears => simpler and less data to transfer to browser) and keep the min values for availability all over the event time range
                        " (select min(row_number), min(site_id) as site, min(item_id) as item, null as date, min(max - current) as available, min(i.ord) as ord from ra join item i on i.id=item_id where not exists(select * from pda where site_id=ra.site_id and (item_id=ra.item_id or item_id is null and item_family_id=i.family_id)) group by site_id,item_id)" +
                        // finally we order this query union by site, item and date
                        " order by site,ord,date",
                eventId, eventId
        ).map(rs -> eventAvailabilities = rs);
    }

    // Event availability accessor

    private QueryResultSet eventAvailabilities;
    @Override
    public QueryResultSet getEventAvailabilities() {
        return eventAvailabilities;
    }


    // Private implementation methods

    private static String getHost() {
        return ((WebSocketBusOptions) Platform.getBusOptions()).getServerHost();
    }

    private Future<Batch<EntityList>> executeParallelEventQueries(EventQuery... eventQueries) {
        return executeParallelEventQueries(new Batch<>(eventQueries));
    }

    private Future<Batch<EntityList>> executeParallelEventQueries(Batch<EventQuery> batch) {
        return batch.executeParallel(EntityList[]::new, this::executeEventQuery);
    }

    private Future<EntityList> executeEventQuery(String queryString, Object[] parameters, Object listId) {
        return executeEventQuery(new EventQuery(listId, queryString, parameters));
    }

    private Future<EntityList> executeEventQuery(EventQuery eventQuery) {
        SqlCompiled sqlCompiled = dataSourceModel.getDomainModel().compileSelect(eventQuery.queryString, eventQuery.parameters);
        return executeQuery(sqlCompiled.getSql(), eventQuery.parameters)
                .map(rs ->  QueryResultSetToEntityListGenerator.createEntityList(rs, sqlCompiled.getQueryMapping(), store, eventQuery.listId));
    }

    private Future<QueryResultSet> executeQuery(String queryString, Object... parameters) {
        return Platform.getQueryService().executeQuery(new QueryArgument(queryString, parameters, dataSourceModel.getId()));
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

    //

    private OptionsPreselection selectedOptionsPreselection;
    @Override
    public void setSelectedOptionsPreselection(OptionsPreselection selectedOptionsPreselection) {
        this.selectedOptionsPreselection = selectedOptionsPreselection;
    }

    @Override
    public OptionsPreselection getSelectedOptionsPreselection() {
        return selectedOptionsPreselection;
    }

    private WorkingDocument workingDocument;
    @Override
    public void setWorkingDocument(WorkingDocument workingDocument) {
        this.workingDocument = workingDocument;
    }

    @Override
    public WorkingDocument getWorkingDocument() {
        if (workingDocument == null && selectedOptionsPreselection != null)
            workingDocument = selectedOptionsPreselection.getWorkingDocument();
        return workingDocument;
    }
}
