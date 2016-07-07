package naga.toolkit.providers.gwt.nodes.charts;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataColumn;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.Selection;
import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import com.googlecode.gwt.charts.client.corechart.PieChart;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;
import naga.platform.spi.Platform;
import naga.toolkit.spi.nodes.charts.Chart;
import naga.toolkit.providers.gwt.nodes.GwtSelectableDisplayResultSetNode;
import naga.toolkit.spi.properties.markers.SelectionMode;
import naga.commons.type.PrimType;
import naga.commons.type.Type;
import naga.commons.type.Types;
import naga.toolkit.spi.display.DisplayColumn;
import naga.toolkit.spi.display.DisplayResultSet;
import naga.toolkit.spi.display.DisplaySelection;
import naga.commons.util.Booleans;
import naga.commons.util.Numbers;
import naga.commons.util.Strings;
import naga.commons.util.function.Function;

/**
 * @author Bruno Salmon
 */
public abstract class GwtChart extends GwtSelectableDisplayResultSetNode<SimpleLayoutPanel> implements Chart<SimpleLayoutPanel> {

    private CoreChartWidget<?> chartWidget;
    private DisplayResultSet readyDisplayResultSet;
    private boolean isPieChart;

    public GwtChart() {
        super(new SimpleLayoutPanel());
        ChartApiLoader.onChartApiLoaded(() -> {
            chartWidget = createChartWidget();
            isPieChart = chartWidget instanceof PieChart;
            node.setWidget(chartWidget);
            if (readyDisplayResultSet != null)
                syncVisualDisplayResult(readyDisplayResultSet);
            readyDisplayResultSet = null;
            node.onResize();
            if (!node.isAttached())
                node.addAttachHandler(event -> Platform.scheduleDeferred(node::onResize));
        });
    }

    protected abstract CoreChartWidget createChartWidget();

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {
    }

    private boolean syncHandlerInstalled;
    private boolean syncingDisplaySelection;

    private void syncToolkitDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            JsArray<Selection> selectionArray = chartWidget.getSelection();
            int length = selectionArray == null ? 0 : selectionArray.length();
            DisplaySelection.Builder selectionBuilder = DisplaySelection.createBuilder(length);
            for (int i = 0; i < length; i++)
                selectionBuilder.addSelectedRow(selectionArray.get(i).getRow());
            setDisplaySelection(selectionBuilder.build());
            syncingDisplaySelection = false;
        }
    }

    private void syncVisualDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            DisplaySelection displaySelection = getDisplaySelection();
            if (displaySelection == null) {
                // chartWidget.setSelection(); // don't know how to clear selection (this code produces a JS error)
            } else {
                Selection[] selectionArray = new Selection[displaySelection.getUnitsCount()];
                int i = 0;
                for (DisplaySelection.Unit unit : displaySelection.getUnits())
                    selectionArray[i++] = Selection.create(unit.getRow(), unit.getColumn());
                chartWidget.setSelection(selectionArray);
            }
            syncingDisplaySelection = false;
        }
    }


    @Override
    protected void syncVisualDisplayResult(DisplayResultSet rs) {
        if (chartWidget == null) {
            readyDisplayResultSet = rs;
            return;
        }
        syncVisualDisplayResultNow(rs);
        // The above code reset the selection so selection handlers are installed here when first data arrives (to consider possible initial selection made by logic)
        if (!syncHandlerInstalled && rs.getRowCount() > 0) {
            syncVisualDisplaySelection();
            displaySelectionProperty().addListener((observable, oldValue, newValue) -> syncVisualDisplaySelection());
            chartWidget.addSelectHandler(new SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    syncToolkitDisplaySelection();
                }
            });
            syncHandlerInstalled = true;
        }
    }

    protected void syncVisualDisplayResultNow(DisplayResultSet rs) {
        int rowCount = rs.getRowCount();
        int columnCount = rs.getColumnCount();
        DisplayColumn[] columns = rs.getColumns();
        boolean rowFormat = columns[0].getRole() == null;
        boolean hasXAxis = !isPieChart;
        if (!rowFormat) { /***** Column format - see {@link Chart} for format description *****/
            if (isPieChart && rowCount > 1) // ignoring extra rows for pie chart
                rowCount = 1;
            int firstSeriesColumnIndex = hasXAxis ? 1 : 0;
            int seriesCount = columnCount - firstSeriesColumnIndex;
            int pointPerSeriesCount = rowCount;
            Type xType = hasXAxis ? columns[0].getType() : null;
            Type yType = columns[firstSeriesColumnIndex].getType();
            createChartData(xType, yType, pointPerSeriesCount, seriesCount, seriesIndex -> columns[firstSeriesColumnIndex + seriesIndex].getName());
            for (int pointIndex = 0; pointIndex < pointPerSeriesCount; pointIndex++) {
                if (hasXAxis)
                    setChartDataX(rs.getValue(pointIndex, 0), pointIndex);
                for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                    Object yValue = rs.getValue(pointIndex, firstSeriesColumnIndex + seriesIndex);
                    setChartDataY(yValue, pointIndex, seriesIndex);
                }
            }
        } else {          /***** Row format - see {@link Chart} for format description *****/
            if (isPieChart && columnCount > 2) // ignoring extra columns for pie charts
                columnCount = 2;
            int seriesCount = rowCount;
            int pointPerSeriesCount = columnCount - 1;
            Type xType = hasXAxis ? PrimType.fromObject(columns[1].getName()) : null;
            Type yType = columns[pointPerSeriesCount].getType();
            createChartData(xType, yType, pointPerSeriesCount, seriesCount, seriesIndex -> Strings.toString(rs.getValue(seriesIndex, 0)));
            for (int pointIndex = 0; pointIndex < pointPerSeriesCount; pointIndex++) {
                if (hasXAxis)
                    setChartDataX(columns[pointIndex + 1].getName(), pointIndex);
                for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                    Object yValue = rs.getValue(seriesIndex, pointIndex + 1);
                    setChartDataY(yValue, pointIndex, seriesIndex);
                }
            }
        }
        applyChartData();
    }

    private DataTable dataTable;
    private ColumnType xGoogleType;
    private ColumnType yGoogleType;
    private boolean googleRowFormat;
    private int seriesCount;

    private void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter) {
        // Creating a google dataTable in column format (each series is a column)
        xGoogleType = toGoogleColumnType(Types.getPrimType(xType));
        yGoogleType = toGoogleColumnType(Types.getPrimType(yType));
        dataTable = DataTable.create();
        googleRowFormat = isPieChart;
        if (googleRowFormat) {
            dataTable.addRows(seriesCount);
            dataTable.addColumn(ColumnType.STRING); // first column for series names
            if (!isPieChart)
                dataTable.addColumn(DataColumn.create(xGoogleType)); // second column for X
            for (int pointIndex = 0; pointIndex < pointPerSeriesCount; pointIndex++)
                dataTable.addColumn(DataColumn.create(yGoogleType)); // other columns for Y
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++)
                dataTable.setValue(seriesIndex, 0, seriesNameGetter.apply(seriesIndex));
        } else {
            dataTable.addRows(pointPerSeriesCount);
            dataTable.addColumn(DataColumn.create(xGoogleType)); // first column for X
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                DataColumn dataColumn = DataColumn.create(yGoogleType);
                dataColumn.setLabel(seriesNameGetter.apply(seriesIndex));
                dataTable.addColumn(dataColumn);
            }
        }
        this.seriesCount = seriesCount;
    }

    private void setChartDataX(Object xValue, int pointIndex) {
        if (isPieChart)
            return;
        if (googleRowFormat)
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++)
                setDataTableValue(seriesIndex, 1, xValue, xGoogleType);
        else
            setDataTableValue(pointIndex, 0, xValue, xGoogleType);
    }

    private void setChartDataY(Object yValue, int pointIndex, int seriesIndex) {
        if (googleRowFormat)
            setDataTableValue(seriesIndex, pointIndex + (isPieChart ? 1 : 2), yValue, yGoogleType);
        else
            setDataTableValue(pointIndex, seriesIndex + 1, yValue, yGoogleType);
    }

    private void setDataTableValue(int rowIndex, int columnIndex, Object value, ColumnType googleType) {
        if (googleType == ColumnType.BOOLEAN)
            dataTable.setValue(rowIndex, columnIndex, Booleans.booleanValue(value));
        else if (googleType == ColumnType.STRING)
            dataTable.setValue(rowIndex, columnIndex, Strings.stringValue(value));
        else if (googleType == ColumnType.NUMBER)
            dataTable.setValue(rowIndex, columnIndex, Numbers.doubleValue(value));
    }

    private void applyChartData() {
        chartWidget.draw(dataTable);
    }

    private static ColumnType toGoogleColumnType(PrimType primType) {
        if (primType.isBoolean())
            return ColumnType.BOOLEAN;
        if (primType.isString())
            return ColumnType.STRING;
        if (primType.isNumber())
            return ColumnType.NUMBER;
        return null;
    }
}
