package naga.providers.toolkit.gwt.nodes.charts;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.toolkit.spi.nodes.charts.AreaChart;

/**
 * @author Bruno Salmon
 */
public class GwtAreaChart extends GwtChart implements AreaChart {

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.AreaChart();
    }
}
