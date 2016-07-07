package naga.toolkit.providers.gwt.nodes.charts;

import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.toolkit.spi.nodes.charts.PieChart;

/**
 * @author Bruno Salmon
 */
public class GwtPieChart extends GwtChart implements PieChart<SimpleLayoutPanel> {

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.PieChart();
    }
}
