package naga.providers.toolkit.swing.fx.viewer;

import naga.commons.type.Type;
import naga.commons.util.function.Function;
import naga.commons.util.tuples.Triplet;
import naga.providers.toolkit.swing.nodes.charts.XChartJPanel;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.fx.ext.chart.Chart;
import naga.toolkit.fx.spi.viewer.base.ChartViewerBase;
import naga.toolkit.fx.spi.viewer.base.ChartViewerMixin;
import naga.toolkit.properties.markers.SelectionMode;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
abstract class SwingChartViewer
        <N extends Chart, NV extends ChartViewerBase<XChartJPanel, N, NV, NM>, NM extends ChartViewerMixin<XChartJPanel, N, NV, NM>>
        extends SwingRegionViewer<N, NV, NM>
        implements ChartViewerMixin<XChartJPanel, N, NV, NM>, SwingLayoutMeasurable<N> {

    private final XChartJPanel xChartJPanel = new XChartJPanel();

    SwingChartViewer(NV base) {
        super(base);
    }

    @Override
    public JComponent getSwingComponent() {
        return xChartJPanel;
    }

    @Override
    public void updateSelectionMode(SelectionMode mode) {
    }

    @Override
    public void updateDisplaySelection(DisplaySelection selection) {
    }

    private Triplet<String, List, List>[] seriesInfos;
    private Object xValue;

    @Override
    public void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter) {
        seriesInfos = new Triplet[seriesCount];
        for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++)
            seriesInfos[seriesIndex] = new Triplet(seriesNameGetter.apply(seriesIndex), new ArrayList(pointPerSeriesCount), new ArrayList(pointPerSeriesCount));
    }

    @Override
    public void setChartDataX(Object xValue, int pointIndex) {
        this.xValue = xValue;
    }

    @Override
    public void setChartDataY(Object yValue, int pointIndex, int seriesIndex) {
        Triplet<String, List, List> seriesInfo = seriesInfos[seriesIndex];
        seriesInfo.get2().add(xValue);
        seriesInfo.get3().add(yValue);
    }

    @Override
    public void applyChartData() {
        XYChart chart = createChart();
        for (Triplet<String, List, List> seriesInfo : seriesInfos)
            if (!seriesInfo.get3().isEmpty())
                chart.addSeries(seriesInfo.get1(), seriesInfo.get2(), seriesInfo.get3());
        xChartJPanel.setChart(chart);
    }

    abstract XYChart createChart();
}
