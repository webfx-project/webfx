package naga.providers.toolkit.swing.nodes.charts;

import naga.toolkit.spi.nodes.charts.LineChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;

/**
 * @author Bruno Salmon
 */
public class SwingLineChart extends SwingChart implements LineChart<ChartPanel> {

    @Override
    protected JFreeChart createChart(CategoryDataset ds) {
        JFreeChart lineChart = ChartFactory.createLineChart(null, null, null, ds);
        return lineChart;
    }
}
