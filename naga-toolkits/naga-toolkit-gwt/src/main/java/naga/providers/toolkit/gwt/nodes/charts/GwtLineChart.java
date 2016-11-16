package naga.providers.toolkit.gwt.nodes.charts;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.toolkit.spi.nodes.charts.LineChart;

/**
 * @author Bruno Salmon
 */
public class GwtLineChart extends GwtChart implements LineChart {

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.LineChart();
    }
}
