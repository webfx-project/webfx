package naga.providers.toolkit.gwt.nodes.charts;

import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.toolkit.spi.nodes.charts.ScatterChart;

/**
 * @author Bruno Salmon
 */
public class GwtScatterChart extends GwtChart implements ScatterChart<SimpleLayoutPanel> {

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.ScatterChart();
    }
}
