package naga.core.spi.toolkit.gwt.charts;

import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.googlecode.gwt.charts.client.ChartWidget;
import naga.core.spi.toolkit.charts.PieChart;

/**
 * @author Bruno Salmon
 */
public class GwtPieChart extends GwtChart implements PieChart<SimpleLayoutPanel> {

    @Override
    protected ChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.PieChart();
    }
}
