package mongoose.backend.activities.statistics;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import mongoose.backend.activities.bookings.BookingDetailsPanel;
import mongoose.backend.activities.bookings.GroupView;
import mongoose.client.activity.eventdependent.EventDependentPresentationModel;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.client.activity.table.GroupMasterSlaveView;
import mongoose.client.entities.util.FilterButtonSelectorFactoryMixin;
import mongoose.client.entities.util.Filters;
import mongoose.shared.entities.DocumentLine;
import mongoose.shared.entities.Filter;
import webfx.framework.client.operation.action.OperationActionFactoryMixin;
import webfx.framework.client.ui.controls.button.EntityButtonSelector;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;
import webfx.fxkit.util.properties.Properties;

import java.util.Objects;

final class StatisticsActivity extends EventDependentViewDomainActivity
        implements OperationActionFactoryMixin,
        FilterButtonSelectorFactoryMixin,
        ReactiveExpressionFilterFactoryMixin {

    private final StatisticsPresentationModel pm = new StatisticsPresentationModel();

    @Override
    public EventDependentPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    @Override
    public Node buildUi() {
        BorderPane container = new BorderPane();

        // Building the top bar
        EntityButtonSelector<Filter> //conditionSelector = createDocumentConditionFilterButtonSelector(container),
                groupSelector     = createGroupFilterButtonSelector("DocumentLine", container)/*,
                columnsSelector   = createDocumentColumnsFilterButtonSelector(container)*/;
        container.setTop(groupSelector.getButton());

        DataGrid masterTable = new DataGrid();
        BorderPane.setAlignment(masterTable, Pos.TOP_CENTER);
        CheckBox limitCheckBox = newCheckBox("LimitTo100");
        limitCheckBox.setSelected(true);
        BorderPane masterPane = new BorderPane(masterTable, null, null, limitCheckBox, null);

        GroupView<DocumentLine> groupView = new GroupView<>();
        BookingDetailsPanel bookingDetailsPanel = new BookingDetailsPanel(container, this, getDataSourceModel());

        GroupMasterSlaveView groupMasterSlaveView = new GroupMasterSlaveView(Orientation.VERTICAL,
                groupView.buildUi(),
                masterPane,
                bookingDetailsPanel.buildUi());
        container.setCenter(groupMasterSlaveView.getSplitPane());

        Properties.runNowAndOnPropertiesChange(() -> pm.limitProperty().setValue(limitCheckBox.isSelected() ? 30 : -1), limitCheckBox.selectedProperty());
        masterTable.fullHeightProperty().bind(limitCheckBox.selectedProperty());
        //pm.limitProperty().bind(limitCheckBox.selectedProperty());
        pm.genericDisplaySelectionProperty().bindBidirectional(masterTable.displaySelectionProperty());
        // User outputs: the presentation model changes are transferred in the UI
        masterTable.displayResultProperty().bind(pm.genericDisplayResultProperty());

        //pm.conditionStringFilterProperty() .bind(Properties.compute(conditionSelector .selectedItemProperty(), Filters::toStringJson));
        pm.groupStringFilterProperty()     .bind(Properties.compute(groupSelector     .selectedItemProperty(), Filters::toStringJson));
        //pm.columnsStringFilterProperty()   .bind(Properties.compute(columnsSelector   .selectedItemProperty(), Filters::toStringJson));

        bookingDetailsPanel.selectedDocumentProperty().bind(pm.selectedDocumentProperty());
        bookingDetailsPanel.activeProperty().bind(activeProperty());

        groupMasterSlaveView.setGroupVisible(true);
        groupMasterSlaveView.masterVisibleProperty().bind(Properties.compute(pm.selectedGroupProperty(), Objects::nonNull));
        groupMasterSlaveView.slaveVisibleProperty() .bind(Properties.combine(groupMasterSlaveView.masterVisibleProperty(), pm.selectedDocumentProperty(), (masterVisible, selectedDocument) -> masterVisible && selectedDocument != null));

        groupView.groupDisplayResultProperty().bind(pm.groupDisplayResultProperty());
        groupView.selectedGroupProperty().bind(pm.selectedGroupProperty());
        groupView.groupStringFilterProperty().bind(pm.groupStringFilterProperty());
        pm.selectedGroupConditionStringFilterProperty().bind(groupView.selectedGroupConditionStringFilterProperty());
        pm.groupDisplaySelectionProperty().bind(groupView.groupDisplaySelectionProperty());
        groupView.setReferenceResolver(groupFilter.getRootAliasReferenceResolver());

        return container;
    }

    private ReactiveExpressionFilter<DocumentLine> groupFilter;
    private ReactiveExpressionFilter<DocumentLine> masterFilter;

    @Override
    protected void startLogic() {
        // Setting up the group filter that controls the content displayed in the group view
        groupFilter = this.<DocumentLine>createReactiveExpressionFilter("{class: 'DocumentLine', alias: 'dl'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `document.event=" + eventId + "`}")
                // Applying the condition and group selected by the user
                //.combineIfNotNullOtherwiseForceEmptyResult(pm.conditionStringFilterProperty(), stringFilter -> stringFilter)
                .combine("{where: '!cancelled'}")
                .combineIfNotNullOtherwiseForceEmptyResult(pm.groupStringFilterProperty(), stringFilter -> stringFilter.contains("groupBy") ? stringFilter : "{where: 'false'}")
                // Displaying the result in the group view
                .displayResultInto(pm.groupDisplayResultProperty())
                // Reacting to a group selection
                .setSelectedEntityHandler(pm.groupDisplaySelectionProperty(), pm::setSelectedGroup)
                // Everything set up, let's start now!
                .start();
        // Setting up the master filter that controls the content displayed in the master view
        masterFilter = this.<DocumentLine>createReactiveExpressionFilter("{class: 'DocumentLine', alias: 'dl', orderBy: 'id desc'}")
                .combine("{columns: '<statistics>'}")
                // Always loading the fields required for viewing the booking details
                .combine("{fields: `document.(" + BookingDetailsPanel.REQUIRED_FIELDS_STRING_FILTER + ")`}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `document.event=" + eventId + "`}")
                // Applying the condition and columns selected by the user
                //.combineIfNotNullOtherwiseForceEmptyResult(pm.conditionStringFilterProperty(), stringFilter -> stringFilter)
                //.combineIfNotNullOtherwiseForceEmptyResult(pm.columnsStringFilterProperty(),   stringFilter -> stringFilter)
                // Also, in case groups are showing and a group is selected, applying the condition associated with that group
                .combineIfNotNullOtherwiseForceEmptyResult(pm.selectedGroupConditionStringFilterProperty(), s -> s)
                // Applying the user search
/*
                .combineIfNotEmptyTrim(pm.searchTextProperty(), s ->
                        Character.isDigit(s.charAt(0)) ? "{where: `ref = " + s + "`}"
                                : s.contains("@") ? "{where: `lower(person_email) like '%" + s.toLowerCase() + "%'`}"
                                : "{where: `person_abcNames like '" + AbcNames.evaluate(s, true) + "'`}")
*/
                // Limit clause
                .combineIfPositive(pm.limitProperty(), limit -> "{limit: `" + limit + "`}")
                // Colorizing the rows
                .applyDomainModelRowStyle()
                // Displaying the result in the master view
                .displayResultInto(pm.genericDisplayResultProperty())
                // Reacting the a booking selection
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), dl -> {
                    pm.setSelectedDocumentLine(dl);
                    pm.setSelectedDocument(dl.getDocument());
                })
                // Everything set up, let's start now!
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        groupFilter .refreshWhenActive();
        masterFilter.refreshWhenActive();
    }

}
