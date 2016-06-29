package naga.core.spi.toolkit.gwt.charts;

import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.core.spi.toolkit.charts.LineChart;

/**
 * @author Bruno Salmon
 */
public class GwtLineChart extends GwtChart implements LineChart<SimpleLayoutPanel> {

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.LineChart();
    }
}
