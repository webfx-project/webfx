package naga.providers.toolkit.gwt.nodes.charts;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataColumn;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.Selection;
import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;
import naga.commons.type.PrimType;
import naga.commons.type.Type;
import naga.commons.type.Types;
import naga.commons.util.Booleans;
import naga.commons.util.Numbers;
import naga.commons.util.Strings;
import naga.commons.util.function.Function;
import naga.providers.toolkit.gwt.nodes.GwtSelectableDisplayResultSetNode;
import naga.toolkit.adapters.chart.ChartAdapter;
import naga.toolkit.adapters.chart.ChartFiller;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.charts.Chart;

/**
 * @author Bruno Salmon
 */
public abstract class GwtChart extends GwtSelectableDisplayResultSetNode<SimpleLayoutPanel> implements Chart<SimpleLayoutPanel> {

    private CoreChartWidget<?> chartWidget;
    private DisplayResultSet readyDisplayResultSet;

    public GwtChart() {
        super(new SimpleLayoutPanel());
        ChartApiLoader.onChartApiLoaded(() -> {
            chartWidget = createChartWidget();
            node.setWidget(chartWidget);
            if (readyDisplayResultSet != null)
                syncVisualDisplayResult(readyDisplayResultSet);
            readyDisplayResultSet = null;
            node.onResize();
            if (!node.isAttached())
                node.addAttachHandler(event -> Toolkit.get().scheduler().scheduleDeferred(node::onResize));
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
        chartFiller.fillChart(rs);
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

    private final ChartFiller chartFiller = new ChartFiller(this, new ChartAdapter() {
        private DataTable dataTable;
        private ColumnType xGoogleType;
        private ColumnType yGoogleType;
        private boolean googleRowFormat;
        private int seriesCount;

        public void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter) {
            // Creating a google dataTable in column format (each series is a column)
            xGoogleType = toGoogleColumnType(Types.getPrimType(xType));
            yGoogleType = toGoogleColumnType(Types.getPrimType(yType));
            dataTable = DataTable.create();
            googleRowFormat = chartFiller.isPieChart();
            if (googleRowFormat) {
                dataTable.addRows(seriesCount);
                dataTable.addColumn(ColumnType.STRING); // first column for series names
                if (!chartFiller.isPieChart())
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

        public void setChartDataX(Object xValue, int pointIndex) {
            if (chartFiller.isPieChart())
                return;
            if (googleRowFormat)
                for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++)
                    setDataTableValue(seriesIndex, 1, xValue, xGoogleType);
            else
                setDataTableValue(pointIndex, 0, xValue, xGoogleType);
        }

        public void setChartDataY(Object yValue, int pointIndex, int seriesIndex) {
            if (googleRowFormat)
                setDataTableValue(seriesIndex, pointIndex + (chartFiller.isPieChart() ? 1 : 2), yValue, yGoogleType);
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

        public void applyChartData() {
            chartWidget.draw(dataTable);
        }

        private ColumnType toGoogleColumnType(PrimType primType) {
            if (primType.isBoolean())
                return ColumnType.BOOLEAN;
            if (primType.isString())
                return ColumnType.STRING;
            if (primType.isNumber())
                return ColumnType.NUMBER;
            return null;
        }
    });

}
