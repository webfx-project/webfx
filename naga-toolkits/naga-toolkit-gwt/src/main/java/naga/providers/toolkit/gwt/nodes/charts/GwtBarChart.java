package naga.providers.toolkit.gwt.nodes.charts;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.toolkit.spi.nodes.charts.BarChart;

/**
 * @author Bruno Salmon
 */
public class GwtBarChart extends GwtChart implements BarChart {

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.BarChart();
    }
}
