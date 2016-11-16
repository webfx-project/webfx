package naga.providers.toolkit.swing.nodes.charts;

import naga.commons.type.Type;
import naga.commons.util.function.Function;
import naga.commons.util.tuples.Triplet;
import naga.providers.toolkit.swing.nodes.SwingSelectableDisplayResultSetNode;
import naga.toolkit.adapters.chart.ChartAdapter;
import naga.toolkit.adapters.chart.ChartFiller;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.properties.markers.SelectionMode;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class SwingChart extends SwingSelectableDisplayResultSetNode<XChartJPanel> implements naga.toolkit.spi.nodes.charts.Chart {

    public SwingChart() {
        this(new XChartJPanel());
    }

    public SwingChart(XChartJPanel node) {
        super(node);
    }

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {
    }

    @Override
    protected void syncVisualDisplayResult(DisplayResultSet rs) {
        chartFiller.fillChart(rs);
    }

    protected abstract XYChart createChart();

    private final ChartFiller chartFiller = new ChartFiller(this, new ChartAdapter() {

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
            node.setChart(chart);
        }
    });

}
