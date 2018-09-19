package webfx.fxkit.gwt.mapper.html.peer;

import com.googlecode.gwt.charts.client.*;
import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import elemental2.dom.Node;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkits.core.util.properties.Properties;
import webfx.fxkits.extra.chart.Chart;
import webfx.fxkits.extra.chart.PieChart;
import webfx.fxkits.extra.displaydata.DisplayResult;
import webfx.fxkits.extra.displaydata.DisplaySelection;
import webfx.fxkits.extra.displaydata.SelectionMode;
import webfx.fxkits.extra.mapper.spi.peer.impl.ChartPeerBase;
import webfx.fxkits.extra.mapper.spi.peer.impl.ChartPeerMixin;
import webfx.fxkits.extra.type.PrimType;
import webfx.fxkits.extra.type.Type;
import webfx.fxkits.extra.type.Types;
import webfx.platforms.core.services.uischeduler.UiScheduler;
import webfx.platforms.core.util.Booleans;
import webfx.platforms.core.util.Numbers;
import webfx.platforms.core.util.Strings;
import webfx.platforms.core.util.function.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
abstract class HtmlChartPeer
        <C, N extends Chart, NB extends ChartPeerBase<C, N, NB, NM>, NM extends ChartPeerMixin<C, N, NB, NM>>
        extends HtmlRegionPeer<N, NB, NM>
        implements ChartPeerMixin<C, N, NB, NM>, HtmlLayoutMeasurable {

    private CoreChartWidget<?> chartWidget;

    HtmlChartPeer(NB base) {
        super(base, HtmlUtil.createDivElement());
        onChartApiLoaded(() -> {
            chartWidget = createChartWidget();
            HtmlUtil.setChild(getElement(), (Node) (Object) chartWidget.getElement());
            if (getNode() != null)
                onNodeAndWidgetReady();
            else
                UiScheduler.scheduleDeferred(this::onNodeAndWidgetReady);
        });
    }

    private void onNodeAndWidgetReady() {
        N node = getNode();
        updateResult(node.getDisplayResult());
        Properties.runNowAndOnPropertiesChange(p -> {
            chartWidget.setWidth(toPx(node.getWidth()));
            chartWidget.setHeight(toPx(node.getHeight()));
            chartWidget.redraw();
        }, node.widthProperty(), node.heightProperty());
    }

    private static boolean apiLoaded;
    private static List<Runnable> pendingCallbacks;

    /**
     * A method responsible for loading the google chart API. It's a wrapper of the google ChartLoader which has the
     * drawback that it can't be used several times (only the first call is working, further calls do nothing, even not
     * calling the callback). This wrapper always call the callback.
     */
    private static void onChartApiLoaded(Runnable callback) {
        if (apiLoaded)
            callback.run();
        else {
            boolean firstTime = pendingCallbacks == null;
            if (firstTime)
                pendingCallbacks = new ArrayList<>();
            pendingCallbacks.add(callback);
            if (firstTime)
                new ChartLoader(ChartPackage.CORECHART).loadApi(() -> {
                    apiLoaded = true;
                    for (Runnable pendingCallback : pendingCallbacks)
                        pendingCallback.run();
                    pendingCallbacks = null;
                });
        }
    }

    protected abstract CoreChartWidget createChartWidget();

    @Override
    public void updateSelectionMode(SelectionMode mode) {

    }

    @Override
    public void updateDisplaySelection(DisplaySelection selection) {

    }

    @Override
    public void updateResult(DisplayResult rs) {
        if (chartWidget != null)
            getNodePeerBase().updateResult(rs);
    }

    private boolean isPieChart;
    private DataTable dataTable;
    private ColumnType xGoogleType;
    private ColumnType yGoogleType;
    private boolean googleRowFormat;
    private int seriesCount;

    @Override
    public void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter) {
        // Creating a google dataTable in column format (each series is a column)
        xGoogleType = toGoogleColumnType(Types.getPrimType(xType));
        yGoogleType = toGoogleColumnType(Types.getPrimType(yType));
        dataTable = DataTable.create();
        isPieChart = getNode() instanceof PieChart;
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

    @Override
    public void setChartDataX(Object xValue, int pointIndex) {
        if (isPieChart)
            return;
        if (googleRowFormat)
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++)
                setDataTableValue(seriesIndex, 1, xValue, xGoogleType);
        else
            setDataTableValue(pointIndex, 0, xValue, xGoogleType);

    }

    @Override
    public void setChartDataY(Object yValue, int pointIndex, int seriesIndex) {
        if (googleRowFormat)
            setDataTableValue(seriesIndex, pointIndex + (isPieChart ? 1 : 2), yValue, yGoogleType);
        else
            setDataTableValue(pointIndex, seriesIndex + 1, yValue, yGoogleType);
    }

    @Override
    public void applyChartData() {
        chartWidget.draw(dataTable);
    }

    private void setDataTableValue(int rowIndex, int columnIndex, Object value, ColumnType googleType) {
        if (googleType == ColumnType.BOOLEAN)
            dataTable.setValue(rowIndex, columnIndex, Booleans.booleanValue(value));
        else if (googleType == ColumnType.STRING)
            dataTable.setValue(rowIndex, columnIndex, Strings.stringValue(value));
        else if (googleType == ColumnType.NUMBER)
            dataTable.setValue(rowIndex, columnIndex, Numbers.doubleValue(value));
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
}
