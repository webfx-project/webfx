package mongooses.core.backend.activities.bookings;

import mongooses.core.sharedends.activities.generic.eventdependent.EventDependentPresentationLogicActivity;
import mongooses.core.sharedends.businesslogic.workingdocument.WorkingDocumentLoader;
import mongooses.core.shared.domainmodel.functions.AbcNames;
import mongooses.core.shared.entities.Document;
import webfx.framework.ui.filter.ReactiveExpressionFilter;
import webfx.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.platforms.core.services.json.WritableJsonObject;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.util.Booleans;
import webfx.platforms.core.util.Objects;
import webfx.platforms.core.util.Strings;

import java.time.LocalDate;

import static mongooses.core.backend.activities.bookings.BookingsRouting.parseDayParam;
import static webfx.framework.expression.sqlcompiler.terms.ConstantSqlCompiler.toSqlDate;

/**
 * @author Bruno Salmon
 */
final class BookingsPresentationLogicActivity
        extends EventDependentPresentationLogicActivity<BookingsPresentationModel>
        implements ReactiveExpressionFilterFactoryMixin {

    BookingsPresentationLogicActivity() {
        super(BookingsPresentationModel::new);
    }

    @Override
    protected void updateContextParametersFromRoute() {
        super.updateContextParametersFromRoute();
        String routingPath = getRoutingPath();
        WritableJsonObject contextParams = (WritableJsonObject) getParams(); // not beautiful...
        contextParams.set("arrivals", Strings.contains(routingPath, "/arrivals"));
        contextParams.set("departures", Strings.contains(routingPath, "/departures"));
    }

    @Override
    protected void updatePresentationModelFromContextParameters(BookingsPresentationModel pm) {
        LocalDate day;
        pm.setColumns(getParameter("columns"));
        pm.setDay(day = parseDayParam(getParameter("day")));
        pm.setArrivals(day != null && Booleans.isTrue(getParameter("arrivals")));
        pm.setDepartures(day != null && Booleans.isTrue(getParameter("departures")));
        pm.setMinDay(parseDayParam(getParameter("minDay")));
        pm.setMaxDay(parseDayParam(getParameter("maxDay")));
        pm.setFilter(getParameter("filter"));
        pm.setGroupBy(getParameter("groupBy"));
        pm.setOrderBy(getParameter("orderBy"));
        super.updatePresentationModelFromContextParameters(pm); // for eventId and organizationId
    }

    private static final String DEFAULT_COLUMNS = "[" +
            "'ref'," +
            "'multipleBookingIcon','countryOrLangIcon','genderIcon'," +
            "'person_firstName'," +
            "'person_lastName'," +
            "'person_age','noteIcon'," +
            "{expression: 'price_net', format: 'price'}," +
            "{expression: 'price_minDeposit', format: 'price'}," +
            "{expression: 'price_deposit', format: 'price'}," +
            "{expression: 'price_balance', format: 'price'}" +
            "]";
    private static final String DEFAULT_FILTER = "!cancelled";
    private static final String DEFAULT_ORDER_BY = "ref desc";
    private ReactiveExpressionFilter<Document> filter;
    @Override
    protected void startLogic(BookingsPresentationModel pm) {
        filter = this.<Document>createReactiveExpressionFilter("{class: 'Document', alias: 'd', fields: 'cart.uuid'}")
            // Columns to display
            .combine(pm.columnsProperty(), columns -> "{columns: `" + Objects.coalesce(columns, DEFAULT_COLUMNS) + "`}")
            // Condition clause
            .combine(         pm.filterProperty(),   filter -> "{where: `" + Objects.coalesce(filter, DEFAULT_FILTER) + "`}")
            .combineIfNotNull(pm.organizationIdProperty(),
                                             organisationId -> "{where:  `event.organization=" + organisationId + "`}")
            .combineIfNotNull(pm.eventIdProperty(), eventId -> "{where:  `event=" + eventId + "`}")
            .combineIfNotNull(pm.dayProperty(),         day -> "{where:  `exists(select Attendance where documentLine.document=d and date="  + toSqlDate(day) + ")`}")
            .combineIfTrue(   pm.arrivalsProperty(),     () -> "{where: `!exists(select Attendance where documentLine.document=d and date="  + toSqlDate(pm.getDay().minusDays(1)) + ")`}")
            .combineIfTrue(   pm.departuresProperty(),   () -> "{where: `!exists(select Attendance where documentLine.document=d and date="  + toSqlDate(pm.getDay().plusDays(1)) + ")`}")
            .combineIfNotNull(pm.minDayProperty(),   minDay -> "{where:  `exists(select Attendance where documentLine.document=d and date>=" + toSqlDate(minDay) + ")`}")
            .combineIfNotNull(pm.maxDayProperty(),   maxDay -> "{where:  `exists(select Attendance where documentLine.document=d and date<=" + toSqlDate(maxDay) + ")`}")
            // Search box condition
            .combineIfNotEmptyTrim(pm.searchTextProperty(), s ->
                Character.isDigit(s.charAt(0)) ? "{where: `ref = " + s + "`}"
                : s.contains("@") ? "{where: `lower(person_email) like '%" + s.toLowerCase() + "%'`}"
                : "{where: `person_abcNames like '" + AbcNames.evaluate(s, true) + "'`}")
            // Group by clause
            .combineIfNotNull(pm.groupByProperty(), groupBy -> "{groupBy: `" + groupBy + "`}")
            // Order by clause
            .combine(pm.orderByProperty(), orderBy -> "{orderBy: `" + Objects.coalesce(orderBy, DEFAULT_ORDER_BY) + "`}")
            // Limit clause
            .combineIfPositive(pm.limitProperty(), l -> "{limit: `" + l + "`}")
            .applyDomainModelRowStyle()
            .displayResultInto(pm.genericDisplayResultProperty())
            .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), document -> {
                if (document != null)
                    WorkingDocumentLoader.load(getEventService(), document.getPrimaryKey()).setHandler(ar -> {
                        if (ar.failed())
                            Logger.log("Error loading document", ar.cause());
/* Commented for now TODO
                        else
                            new RouteToOptionsRequest(ar.result(), getHistory()).execute();
*/
                    });
            })
            .start();
    }

    @Override
    protected void refreshDataOnActive() {
        filter.refreshWhenActive();
    }
}
