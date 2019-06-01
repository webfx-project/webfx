package mongoose.backend.activities.bookings;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import mongoose.backend.operations.bookings.RouteToNewBackendBookingRequest;
import mongoose.backend.operations.cloneevent.RouteToCloneEventRequest;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.client.activity.table.GenericTableView;
import mongoose.shared.domainmodel.functions.AbcNames;
import mongoose.shared.entities.Document;
import webfx.framework.client.operation.action.OperationActionFactoryMixin;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.fxkit.util.properties.Properties;
import webfx.platform.shared.services.json.WritableJsonObject;
import webfx.platform.shared.util.Booleans;
import webfx.platform.shared.util.Objects;
import webfx.platform.shared.util.Strings;

import java.time.LocalDate;

import static mongoose.backend.activities.bookings.routing.BookingsRouting.parseDayParam;
import static webfx.framework.client.ui.layouts.LayoutUtil.setHGrowable;
import static webfx.framework.client.ui.layouts.LayoutUtil.setUnmanagedWhenInvisible;
import static webfx.framework.shared.expression.sqlcompiler.terms.ConstantSqlCompiler.toSqlDate;

final class BookingsActivity extends EventDependentViewDomainActivity
        implements OperationActionFactoryMixin,
        ReactiveExpressionFilterFactoryMixin {

    private GenericTableView genericTableView;
    private final BookingsPresentationModel pm = new BookingsPresentationModel();

    @Override
    public Node buildUi() {
        Button newBookingButton = newButton(newAction(() -> new RouteToNewBackendBookingRequest(getEventId(), getHistory())));
        Button cloneEventButton = newButton(newAction(() -> new RouteToCloneEventRequest(getEventId(), getHistory())));
        return (genericTableView = new GenericTableView() {
            @Override
            public Node buildUi() {
                Node node = super.buildUi();

                borderPane.setTop(new HBox(setUnmanagedWhenInvisible(newBookingButton), setHGrowable(searchBox), setUnmanagedWhenInvisible(cloneEventButton)));

                // Initialization from the presentation model current state
                searchBox.setText(pm.searchTextProperty().getValue());

                // Binding the UI with the presentation model for further state changes
                // User inputs: the UI state changes are transferred in the presentation model
                pm.searchTextProperty().bind(searchBox.textProperty());
                Properties.runNowAndOnPropertiesChange(() -> pm.limitProperty().setValue(limitCheckBox.isSelected() ? 20 : -1), limitCheckBox.selectedProperty());
                table.fullHeightProperty().bind(limitCheckBox.selectedProperty());
                //pm.limitProperty().bind(limitCheckBox.selectedProperty());
                pm.genericDisplaySelectionProperty().bind(table.displaySelectionProperty());
                // User outputs: the presentation model changes are transferred in the UI
                table.displayResultProperty().bind(pm.genericDisplayResultProperty());

                return node;
            }
        }).buildUi();
    }

    @Override
    public void onResume() {
        super.onResume();
        genericTableView.onResume();
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
    protected void updateModelFromContextParameters() {
        LocalDate day;
        setActive(false);
        super.updateModelFromContextParameters(); // for eventId and organizationId
        pm.setColumns(getParameter("columns"));
        pm.setDay(day = parseDayParam(getParameter("day")));
        pm.setArrivals(day != null && Booleans.isTrue(getParameter("arrivals")));
        pm.setDepartures(day != null && Booleans.isTrue(getParameter("departures")));
        pm.setMinDay(parseDayParam(getParameter("minDay")));
        pm.setMaxDay(parseDayParam(getParameter("maxDay")));
        pm.setFilter(getParameter("filter"));
        pm.setGroupBy(getParameter("groupBy"));
        pm.setOrderBy(getParameter("orderBy"));
        pm.setEventId(getEventId());
        pm.setOrganizationId(getOrganizationId());
        setActive(true);
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
    protected void startLogic() {
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
                    ObservableList<Node> items = genericTableView.getSplitPane().getItems();
                    if (document != null)
                        items.setAll(items.get(0), new Button(document.getFullName()));
                    else if (items.size() >= 2)
                        items.setAll(items.get(0));

                })
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        filter.refreshWhenActive();
    }
}
