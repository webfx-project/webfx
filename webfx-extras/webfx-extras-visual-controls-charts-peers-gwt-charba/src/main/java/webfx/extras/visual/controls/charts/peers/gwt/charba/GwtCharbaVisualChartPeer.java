package webfx.extras.visual.controls.charts.peers.gwt.charba;

import javafx.scene.paint.Color;
import org.pepstock.charba.client.AbstractChart;
import org.pepstock.charba.client.configuration.*;
import org.pepstock.charba.client.data.DataPoint;
import org.pepstock.charba.client.data.Dataset;
import org.pepstock.charba.client.data.HasDataPoints;
import org.pepstock.charba.client.enums.InteractionMode;
import org.pepstock.charba.client.enums.Position;
import org.pepstock.charba.client.resources.EmbeddedResources;
import org.pepstock.charba.client.resources.ResourcesType;
import webfx.extras.type.PrimType;
import webfx.extras.type.Type;
import webfx.extras.type.Types;
import webfx.extras.visual.SelectionMode;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualSelection;
import webfx.extras.visual.controls.charts.VisualBarChart;
import webfx.extras.visual.controls.charts.VisualChart;
import webfx.extras.visual.controls.charts.VisualLineChart;
import webfx.extras.visual.controls.charts.VisualPieChart;
import webfx.extras.visual.controls.charts.peers.base.VisualChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualChartPeerMixin;
import webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlRegionPeer;
import webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutMeasurable;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import webfx.kit.util.properties.Properties;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.util.Dates;
import webfx.platform.shared.util.Numbers;
import webfx.platform.shared.util.Strings;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
abstract class GwtCharbaVisualChartPeer
        <C, N extends VisualChart, NB extends VisualChartPeerBase<C, N, NB, NM>, NM extends VisualChartPeerMixin<C, N, NB, NM>>
        extends HtmlRegionPeer<N, NB, NM>
        implements VisualChartPeerMixin<C, N, NB, NM>, HtmlLayoutMeasurable {

    static {
        ResourcesType.setClientBundle(EmbeddedResources.INSTANCE);
    }

    protected AbstractChart chartWidget;

    GwtCharbaVisualChartPeer(NB base) {
        super(base, HtmlUtil.createDivElement());
        onChartApiLoaded(() -> {
            chartWidget = createChartWidget();
            HtmlUtil.setChild(getElement(), chartWidget.getChartElement().as());
            if (getNode() != null)
                onNodeAndWidgetReady();
            else
                UiScheduler.scheduleDeferred(this::onNodeAndWidgetReady);
        });
    }

    private void onNodeAndWidgetReady() {
        N node = getNode();
        updateVisualResult(node.getVisualResult());
        Properties.runNowAndOnPropertiesChange(p -> {
/*
            Canvas canvas = chartWidget.getCanvas();
            canvas.setWidth ((int) (double) node.getWidth());
            canvas.setHeight((int) (double) node.getHeight());
            chartWidget.update();
*/
        }, node.widthProperty(), node.heightProperty());
    }

    private static boolean apiLoaded = true;
    //private static List<Runnable> pendingCallbacks;

    /**
     * A method responsible for loading the google chart API. It's a wrapper of the google ChartLoader which has the
     * drawback that it can't be used several times (only the first call is working, further calls do nothing, even not
     * calling the callback). This wrapper always call the callback.
     */
    private static void onChartApiLoaded(Runnable callback) {
        if (apiLoaded)
            callback.run();
        else {
/*
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
*/
        }
    }

    protected abstract AbstractChart createChartWidget();

    protected abstract Scales getScales(ConfigurationOptions options);

    @Override
    public void updateSelectionMode(SelectionMode mode) {

    }

    @Override
    public void updateVisualSelection(VisualSelection selection) {

    }

    @Override
    public void updateVisualResult(VisualResult rs) {
        if (chartWidget != null)
            getNodePeerBase().updateResult(rs);
    }

    private final static String[] JAVAFX_CHART_COLOR_PALETTE = {"#f3622d", "#fba71b", "#57b757", "#41a9c9", "#4258c9", "#9a42c8", "#c84164", "#888888"};

    PrimType xPrimType, yPrimType;
    private boolean isPieChart;
    List<Dataset> datasets;
    private SeriesInfo[] seriesInfos;
/*
    private DataTable dataTable;
    private ColumnType xGoogleType;
    private ColumnType yGoogleType;
    private boolean googleRowFormat;
*/
    private int seriesCount;
    private int pointPerSeriesCount;

    private String[] labels; // used for Pie
    private String[] xLabels; // used for chats with categories on xAxis
    private static class SeriesInfo {
        double[] data;
        List<DataPoint> dataPoints;
    }

    @Override
    public void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter) {
        //Logger.log("xType = " + xType + ", yType = " + yType);
        //seriesCount = 1;
        this.pointPerSeriesCount = pointPerSeriesCount;
        xPrimType = Types.getPrimType(xType);
        yPrimType = Types.getPrimType(yType);

        ConfigurationOptions options = chartWidget.getOptions();
        options.setResponsive(true);
        options.setMaintainAspectRatio(false);
        options.getLegend().setDisplay(true);
        options.getLegend().setPosition(Position.BOTTOM);
        //options.getTitle().setDisplay(true);
        //options.getTitle().setText("Line chart");
        options.getTooltips().setMode(InteractionMode.INDEX);
        options.getTooltips().setIntersect(false);
        options.getHover().setMode(InteractionMode.NEAREST);
        options.getHover().setIntersect(true);
        options.getElements().getPoint().setRadius(0.5); // 1px dot
        options.getElements().getLine().setTension(0.0); // No Bezier interpolation
        //options.setShowLines(false);

        Axis xAxis = null;
        Scales scales = getScales(options);
        if (scales != null) {
            if (xPrimType.isNumber())
                xAxis = new CartesianLinearAxis(chartWidget);
            else if (xPrimType.isDate())
                xAxis = new CartesianTimeAxis(chartWidget);
            else
                xAxis = new CartesianCategoryAxis(chartWidget);
            xAxis.setDisplay(true);
            //xAxis.getScaleLabel().setDisplay(true);
            //xAxis.getScaleLabel().setLabelString("X");

            Axis yAxis = new CartesianLinearAxis(chartWidget);
            yAxis.setDisplay(true);
            //yAxis.getScaleLabel().setDisplay(true);
            //yAxis.getScaleLabel().setLabelString("Y");

            scales.setXAxes(xAxis);
            scales.setYAxes(yAxis);
        }

        datasets = chartWidget.getData().getDatasets(true);
        datasets.clear();

        N node = getNode();
        isPieChart = node instanceof VisualPieChart;
        if (isPieChart) {
            labels = new String[seriesCount];
            Dataset dataset = chartWidget.newDataset();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                labels[seriesIndex] = seriesNameGetter.apply(seriesIndex);
            }
            dataset.setBackgroundColor((chart, context) -> JAVAFX_CHART_COLOR_PALETTE[context.getIndex() % JAVAFX_CHART_COLOR_PALETTE.length]);
            dataset.setBorderColor((chart, context) -> "white");
            dataset.setBorderWidth((chart, context) -> 1);
            SeriesInfo seriesInfo = new SeriesInfo();
            seriesInfo.data = new double[seriesCount];
            seriesInfos = new SeriesInfo[1];
            seriesInfos[0] = seriesInfo;
            datasets.add(dataset);
        } else {
            boolean hasXLabels = xAxis instanceof CartesianCategoryAxis;
            if (hasXLabels)
                xLabels = new String[pointPerSeriesCount];
            seriesInfos = new SeriesInfo[seriesCount];
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                Dataset dataset = chartWidget.newDataset();
                dataset.setLabel(seriesNameGetter.apply(seriesIndex));
                String hexColor = JAVAFX_CHART_COLOR_PALETTE[seriesIndex % JAVAFX_CHART_COLOR_PALETTE.length];
                dataset.setBackgroundColor((chart, context) -> node instanceof VisualLineChart ? "transparent" : "#" + Color.valueOf(hexColor).deriveColor(1, 1, 1, node instanceof VisualBarChart ? 1 : 0.2).toString().substring(2));
                dataset.setBorderColor((chart, context) -> hexColor);
                dataset.setBorderWidth((chart, context) -> 0);
                datasets.add(dataset);
                SeriesInfo seriesInfo = new SeriesInfo();
                if (dataset instanceof HasDataPoints && !hasXLabels)
                    seriesInfo.dataPoints = ((HasDataPoints) dataset).getDataPoints(true);
                else
                    seriesInfo.data = new double[pointPerSeriesCount];
                seriesInfos[seriesIndex] = seriesInfo;
            }
        }
/*
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
*/
        this.seriesCount = seriesCount;
    }

    private Object xValue;

    @Override
    public void setChartDataX(Object xValue, int pointIndex) {
        this.xValue = xValue;
/*
        if (isPieChart)
            return;
        if (googleRowFormat)
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++)
                setDataTableValue(seriesIndex, 1, xValue, xGoogleType);
        else
            setDataTableValue(pointIndex, 0, xValue, xGoogleType);
*/
    }

    @Override
    public void setChartDataY(Object yValue, int pointIndex, int seriesIndex) {
        if (isPieChart) {
            SeriesInfo seriesInfo = seriesInfos[0];
            seriesInfo.data[seriesIndex] = Numbers.toDouble(yValue);
            return;
        }
/*
        if (labels != null)
            labels[pointIndex] = Strings.toString(xValue);
*/
        if (xLabels != null)
            xLabels[pointIndex] = Strings.toString(xValue);
        if (seriesIndex >= seriesCount)
            return;
        SeriesInfo seriesInfo = seriesInfos[seriesIndex];
        if (seriesInfo.dataPoints != null)
            seriesInfo.dataPoints.add(pointIndex, newDataPoint(xValue, yValue));
        else
            seriesInfo.data[pointIndex] = Numbers.toDouble(yValue);
/*
        if (googleRowFormat)
            setDataTableValue(seriesIndex, pointIndex + (isPieChart ? 1 : 2), yValue, yGoogleType);
        else
            setDataTableValue(pointIndex, seriesIndex + 1, yValue, yGoogleType);
*/
    }

    private DataPoint newDataPoint(Object xValue, Object yValue) {
        //Logger.log("x = " + xValue + ", y = " + yValue);
        DataPoint dataPoint = new DataPoint();
        if (xPrimType.isNumber())
            dataPoint.setX(Numbers.toDouble(xValue));
        else if (xPrimType.isDate())
            dataPoint.setX(new Date(Dates.toInstant(xValue).toEpochMilli()));
        else
            dataPoint.setX(Strings.toSafeString(xValue));
        dataPoint.setY(Numbers.toDouble(yValue));
        return dataPoint;
    }

    @Override
    public void applyChartData() {
        if (labels != null)
            chartWidget.getData().setLabels(labels);
        if (xLabels != null)
            chartWidget.getData().setXLabels(xLabels);
        if (isPieChart) {
            SeriesInfo seriesInfo = seriesInfos[0];
            Dataset dataset = datasets.get(0);
            if (seriesInfo.data != null)
                dataset.setData(seriesInfo.data);
        } else
        for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
            SeriesInfo seriesInfo = seriesInfos[seriesIndex];
            Dataset dataset = datasets.get(seriesIndex);
            if (seriesInfo.data != null)
                dataset.setData(seriesInfo.data);
        }
        chartWidget.update();
/*
        chartWidget.draw(dataTable);
*/
    }

/*
    private void setDataTableValue(int rowIndex, int columnIndex, Object value, ColumnType googleType) {
        if (googleType == ColumnType.BOOLEAN)
            dataTable.setValue(rowIndex, columnIndex, Booleans.booleanValue(value));
        else if (googleType == ColumnType.STRING)
            dataTable.setValue(rowIndex, columnIndex, Strings.stringValue(value));
        else if (googleType == ColumnType.NUMBER)
            dataTable.setValue(rowIndex, columnIndex, Numbers.doubleValue(value));
    }

    private ColumnType toGoogleColumnType(PrimType primType) {
        if (primType != null) {
            if (primType.isBoolean())
                return ColumnType.BOOLEAN;
            if (primType.isNumber())
                return ColumnType.NUMBER;
            if (primType.isDate())
                return ColumnType.DATETIME;
        }
        return ColumnType.STRING;
    }
*/
}
