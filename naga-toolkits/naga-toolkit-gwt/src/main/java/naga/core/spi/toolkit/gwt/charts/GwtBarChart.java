package naga.core.spi.toolkit.gwt.charts;

import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.googlecode.gwt.charts.client.ChartWidget;
import naga.core.spi.toolkit.charts.BarChart;

/**
 * @author Bruno Salmon
 */
public class GwtBarChart extends GwtChart implements BarChart<SimpleLayoutPanel> {

    @Override
    protected ChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.BarChart();
    }
}
