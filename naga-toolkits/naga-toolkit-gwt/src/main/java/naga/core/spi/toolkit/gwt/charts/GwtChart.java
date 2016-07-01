package naga.core.spi.toolkit.gwt.charts;

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
import naga.core.spi.platform.Platform;
import naga.core.spi.toolkit.charts.Chart;
import naga.core.spi.toolkit.gwt.node.GwtSelectableDisplayResultSetNode;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.type.PrimType;
import naga.core.type.Type;
import naga.core.type.Types;
import naga.core.ui.displayresultset.DisplayColumn;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.ui.displayselection.DisplaySelection;
import naga.core.util.Booleans;
import naga.core.util.Numbers;
import naga.core.util.Strings;

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
        if (!rowFormat) { /***** Column format - see {@link Chart} for format description *****/
            if (isPieChart && rowCount > 1) // ignoring extra rows for pie chart
                rowCount = 1;
            int seriesCount = isPieChart ? columnCount : columnCount - 1;
            int pointPerSeriesCount = rowCount;
            Type xType = isPieChart ? null : columns[0].getType();
            Type yType = columns[isPieChart ? 0 : 1].getType();
            createChartData(seriesCount, pointPerSeriesCount, xType, yType);
            for (int columnIndex = 1; columnIndex < columnCount; columnIndex++) {
                String seriesName = columns[columnIndex].getName();
                startSeries(seriesName);
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex ++) {
                    Object xValue = isPieChart ? null : rs.getValue(rowIndex, 0);
                    Object yValue = rs.getValue(rowIndex, columnIndex);
                    addPointToCurrentSeries(xValue, yValue);
                }
            }
        } else {          /***** Row format - see {@link Chart} for format description *****/
            if (isPieChart && columnCount > 2) // ignoring extra columns for pie charts
                columnCount = 2;
            int seriesCount = rowCount;
            int pointPerSeriesCount = columnCount - 1;
            Type xType = isPieChart ? null : PrimType.fromObject(columns[1].getName());
            Type yType = columns[pointPerSeriesCount].getType();
            createChartData(seriesCount, pointPerSeriesCount, xType, yType);
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex ++) {
                String seriesName = Strings.toString(rs.getValue(rowIndex, 0));
                startSeries(seriesName);
                for (int columnIndex = 1; columnIndex < columnCount; columnIndex++) {
                    Object xValue = isPieChart ? null : columns[columnIndex].getName();
                    Object yValue = rs.getValue(rowIndex, columnIndex);
                    addPointToCurrentSeries(xValue, yValue);
                }
            }
        }
        applyChartData();
    }

    private DataTable dataTable;
    private int seriesIndex;
    private int pointIndex;
    private ColumnType xGoogleType;
    private ColumnType yGoogleType;
    private boolean googleRowFormat;

    private void createChartData(int seriesCount, int pointPerSeriesCount, Type xType, Type yType) {
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
            for (int i = 0; i < pointPerSeriesCount; i++)
                dataTable.addColumn(DataColumn.create(yGoogleType)); // other columns for Y
        } else {
            dataTable.addRows(pointPerSeriesCount);
            dataTable.addColumn(DataColumn.create(xGoogleType)); // first column for X
        }
        seriesIndex = -1;
    }

    private void startSeries(String name) {
        seriesIndex++;
        if (googleRowFormat) {
            dataTable.setValue(seriesIndex, 0, name);
        } else {
            DataColumn dataColumn = DataColumn.create(yGoogleType);
            dataColumn.setLabel(name);
            dataTable.addColumn(dataColumn);
        }
        pointIndex = -1;
    }

    private void addPointToCurrentSeries(Object xValue, Object yValue) {
        pointIndex++;
        if (googleRowFormat) {
            if (pointIndex == 0 && !isPieChart)
                setDataTableValue(seriesIndex, 1, xValue, xGoogleType);
            setDataTableValue(seriesIndex, pointIndex + (isPieChart ? 1 : 2), yValue, yGoogleType);
        } else {
            if (seriesIndex == 0)
                setDataTableValue(pointIndex, 0, xValue, xGoogleType);
            setDataTableValue(pointIndex, seriesIndex + 1, yValue, yGoogleType);
        }
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
