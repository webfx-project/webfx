package mongoose.backend.activities.statistics;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import mongoose.backend.controls.bookingdetailspanel.BookingDetailsPanel;
import mongoose.backend.controls.masterslave.MasterTableView;
import mongoose.backend.controls.masterslave.group.GroupMasterSlaveView;
import mongoose.backend.controls.masterslave.group.GroupView;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.client.entities.util.filters.FilterButtonSelectorFactoryMixin;
import mongoose.shared.entities.Attendance;
import mongoose.shared.entities.DocumentLine;
import webfx.framework.client.ui.filter.ExpressionColumn;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.framework.client.ui.filter.StringFilter;
import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.fxkit.extra.displaydata.DisplayColumn;
import webfx.fxkit.extra.displaydata.DisplayResult;
import webfx.fxkit.extra.displaydata.DisplayResultBuilder;
import webfx.fxkit.extra.displaydata.DisplayStyle;
import webfx.fxkit.extra.type.PrimType;
import webfx.platform.shared.util.Dates;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class StatisticsActivity extends EventDependentViewDomainActivity implements
        FilterButtonSelectorFactoryMixin,
        ReactiveExpressionFilterFactoryMixin {

    /*==================================================================================================================
    ================================================= Graphical layer ==================================================
    ==================================================================================================================*/

    private final StatisticsPresentationModel pm = new StatisticsPresentationModel();

    @Override
    public StatisticsPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    @Override
    public Node buildUi() {
        BorderPane container = new BorderPane();

        // Building the filter search bar and put it on top
        container.setTop(createFilterSearchBar("statistics", "DocumentLine", container, pm).buildUi());

        container.setCenter(
                GroupMasterSlaveView.createAndBind(
                        GroupView.createTableOnlyAndBind(pm),
                        MasterTableView.createAndBind(pm, this),
                        BookingDetailsPanel.createAndBind(pm, this, container),
                        pm
                ).buildUi());

        return container;
    }


    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    private ReactiveExpressionFilter<DocumentLine> leftGroupFilter, masterFilter;
    private ReactiveExpressionFilter<Attendance> rightAttendanceFilter;
    private final ObjectProperty<DisplayResult> leftDisplayResultProperty = new SimpleObjectProperty<DisplayResult/*GWT*/>() {
        @Override
        protected void invalidated() {
            buildFinalGroupDisplayResultIfReady();
        }
    };
    private final ObjectProperty<DisplayResult> rightDisplayResultProperty = new SimpleObjectProperty<DisplayResult/*GWT*/>() {
        @Override
        protected void invalidated() {
            buildFinalGroupDisplayResultIfReady();
        }
    };

    @Override
    protected void startLogic() {
        // Setting up the left group filter for the left content displayed in the group view
        leftGroupFilter = this.<DocumentLine>createReactiveExpressionFilter("{class: 'DocumentLine', alias: 'dl'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `document.event=" + eventId + "`}")
                // Applying the condition and group selected by the user
                .combineIfNotNullOtherwiseForceEmptyResult(pm.conditionStringFilterProperty(), stringFilter -> stringFilter)
                //.combine("{where: '!cancelled'}")
                .combineIfNotNullOtherwiseForceEmptyResult(pm.groupStringFilterProperty(), stringFilter -> stringFilter.contains("groupBy") ? stringFilter : "{where: 'false'}")
                // Displaying the result in the group view
                .displayResultInto(leftDisplayResultProperty)
                // Reacting to a group selection
                .setSelectedEntityHandler(pm.groupDisplaySelectionProperty(), pm::setSelectedGroup)
                // Everything set up, let's start now!
                .start();
        pm.setSelectedGroupReferenceResolver(leftGroupFilter.getRootAliasReferenceResolver());

        // Setting up the right group filter
        rightAttendanceFilter = this.<Attendance>createReactiveExpressionFilter("{class: 'Attendance', alias: 'a', where: 'present', orderBy: 'date'}")
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `documentLine.document.event=" + eventId + "`}")
                // Applying the condition and group selected by the user
                .combineIfNotNullOtherwiseForceEmptyResult(pm.conditionStringFilterProperty(), stringFilter -> {
                    StringFilter gsf = new StringFilter(stringFilter);
                    String where = gsf.getWhere();
                    if (where == null)
                        return "{where: 'false'}";
                    where = "a.[documentLine as dl].(" + where + ')';
                    return "{where: `" + where + "`}";
                })
                .combineIfNotNullOtherwiseForceEmptyResult(pm.groupStringFilterProperty(), stringFilter -> {
                    StringFilter gsf = new StringFilter(stringFilter);
                    String groupBy = gsf.getGroupBy();
                    if (groupBy == null)
                        return "{where: 'false'}";
                    groupBy = "documentLine.(" + groupBy + ')';
                    return "{columns: `" + groupBy + ",date,count(1)`, groupBy: `" + groupBy + ",date`}";
                })
                // Displaying the result in the master view
                .displayResultInto(rightDisplayResultProperty)
                .start();

        // Setting up the master filter for the content displayed in the master view
        masterFilter = this.<DocumentLine>createReactiveExpressionFilter("{class: 'DocumentLine', alias: 'dl', orderBy: 'document.ref,item.family.ord,site..ord,item.ord'}")
                // Always loading the fields required for viewing the booking details
                .combine("{fields: `document.(" + BookingDetailsPanel.REQUIRED_FIELDS_STRING_FILTER + ")`}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `document.event=" + eventId + "`}")
                // Applying the condition and columns selected by the user
                .combineIfNotNullOtherwiseForceEmptyResult(pm.conditionStringFilterProperty(), stringFilter -> stringFilter)
                .combineIfNotNullOtherwiseForceEmptyResult(pm.columnsStringFilterProperty(),   stringFilter -> stringFilter)
                // Also, in case groups are showing and a group is selected, applying the condition associated with that group
                .combineIfNotNullOtherwiseForceEmptyResult(pm.selectedGroupConditionStringFilterProperty(), s -> s)
                // Limit clause
                .combineIfPositive(pm.limitProperty(), limit -> "{limit: `" + limit + "`}")
                // Colorizing the rows
                .applyDomainModelRowStyle()
                // Displaying the result in the master view
                .displayResultInto(pm.masterDisplayResultProperty())
                // Reacting the a booking selection
                .setSelectedEntityHandler(pm.masterDisplaySelectionProperty(), pm::setSelectedMaster)
                // Everything set up, let's start now!
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        leftGroupFilter       .refreshWhenActive();
        rightAttendanceFilter .refreshWhenActive();
        masterFilter          .refreshWhenActive();
    }

    private DisplayResult lastLeftResult, lastRightResult;

    private void buildFinalGroupDisplayResultIfReady() {
        DisplayResult leftResult = leftDisplayResultProperty.get();
        DisplayResult rightResult = rightDisplayResultProperty.get();
        if (leftResult == lastLeftResult || rightResult == lastRightResult)
            return;
        lastLeftResult = leftResult;
        lastRightResult = rightResult;
        int rowCount = leftResult.getRowCount();
        int leftColCount = leftResult.getColumnCount();
        List<LocalDate> dates = new ArrayList<>();
        EntityList<Attendance> rightAttendances = rightAttendanceFilter.getCurrentEntityList();
        rightAttendances.forEach(a -> {
            LocalDate date = a.getDate();
            if (dates.isEmpty() || !date.equals(dates.get(dates.size() - 1)))
                dates.add(date);
        });
        int rightColCount = dates.size();
        DisplayColumn[] columns = new DisplayColumn[leftColCount + rightColCount];
        System.arraycopy(leftResult.getColumns(), 0, columns, 0, leftColCount);
        for (int col = 0; col < rightColCount; col++)
            columns[leftColCount + col] = DisplayColumn.create(Dates.format(dates.get(col), "dd/MM"), PrimType.INTEGER, DisplayStyle.RIGHT_STYLE); //, new DisplayStyleImpl(32d, "right"));
        DisplayResultBuilder rsb = DisplayResultBuilder.create(rowCount, columns);
        for (int row = 0; row < rowCount; row++)
            for (int col = 0; col < leftColCount; col++)
                rsb.setValue(row, col, leftResult.getValue(row, col));
        EntityList<DocumentLine> leftDocumentLines = leftGroupFilter.getCurrentEntityList();
        ExpressionColumn[] leftColumns = leftGroupFilter.getExpressionColumns();
        rightAttendances.forEach(a -> {
            LocalDate date = a.getDate();
            DocumentLine rightDocumentLine = a.getDocumentLine();
            for (int row = 0; row < rowCount; row++) {
                DocumentLine leftDocumentLine = leftDocumentLines.get(row);
                boolean match = true;
                for (int col = 0; col < leftColumns.length - 1; col++) {
                    Expression<DocumentLine> expression = leftColumns[col].getExpression();
                    if (!Objects.equals(leftDocumentLine.evaluate(expression), rightDocumentLine.evaluate(expression))) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    rsb.setValue(row, leftColCount + dates.indexOf(date), a.getFieldValue("count"));
                    break;
                }
            }
        });
        pm.groupDisplayResultProperty().set(rsb.build());
    }
}
