package naga.toolkit.providers.gwt.nodes.charts;

import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.toolkit.spi.nodes.charts.BarChart;

/**
 * @author Bruno Salmon
 */
public class GwtBarChart extends GwtChart implements BarChart<SimpleLayoutPanel> {

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.BarChart();
    }
}
