package naga.providers.toolkit.swing.nodes.charts;

import naga.commons.type.Type;
import naga.commons.util.Numbers;
import naga.commons.util.function.Function;
import naga.providers.toolkit.swing.nodes.SwingSelectableDisplayResultSetNode;
import naga.toolkit.adapters.chart.ChartAdapter;
import naga.toolkit.adapters.chart.ChartFiller;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.nodes.charts.Chart;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * @author Bruno Salmon
 */
public abstract class SwingChart extends SwingSelectableDisplayResultSetNode<ChartPanel> implements Chart<ChartPanel> {

    public SwingChart() {
        this(createChartPanel());
    }

    public static ChartPanel createChartPanel() {
        ChartPanel chartPanel = new ChartPanel(null);
        return chartPanel;
    }

    public SwingChart(ChartPanel node) {
        super(node);
    }

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {
    }

    @Override
    protected void syncVisualDisplayResult(DisplayResultSet rs) {
        chartFiller.fillChart(rs);
    }

    protected abstract JFreeChart createChart(CategoryDataset ds);

    private final ChartFiller chartFiller = new ChartFiller(this, new ChartAdapter() {

        private DefaultCategoryDataset ds;
        private Function<Integer, String> seriesNameGetter;
        private Object xValue;

        @Override
        public void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter) {
            ds = new DefaultCategoryDataset();
/*
            try {
                Field data = DefaultCategoryDataset.class.getDeclaredField("data");
                data.setAccessible(true);
                data.set(ds, new DefaultKeyedValues2D(true));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
*/
            this.seriesNameGetter = seriesNameGetter;
        }

        @Override
        public void setChartDataX(Object xValue, int pointIndex) {
            this.xValue = xValue;
        }

        @Override
        public void setChartDataY(Object yValue, int pointIndex, int seriesIndex) {
            ds.setValue(Numbers.doubleValue(yValue), seriesNameGetter.apply(seriesIndex), (Comparable) xValue);
        }

        @Override
        public void applyChartData() {
            node.setChart(createChart(ds));
        }
    });

}
