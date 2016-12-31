package naga.fx.spi.gwt.html.viewer;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.fxdata.chart.LineChart;
import naga.fxdata.spi.viewer.base.LineChartViewerBase;
import naga.fxdata.spi.viewer.base.LineChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlLineChartViewer
        <C, N extends LineChart, NB extends LineChartViewerBase<C, N, NB, NM>, NM extends LineChartViewerMixin<C, N, NB, NM>>
        extends HtmlChartViewer<C, N, NB, NM>
        implements LineChartViewerMixin<C, N, NB, NM> {

    public HtmlLineChartViewer() {
        this((NB) new LineChartViewerBase());
    }

    public HtmlLineChartViewer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.LineChart();
    }
}
