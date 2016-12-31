package naga.fx.spi.gwt.html.viewer;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.fxdata.chart.BarChart;
import naga.fxdata.spi.viewer.base.BarChartViewerBase;
import naga.fxdata.spi.viewer.base.BarChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlBarChartViewer
        <C, N extends BarChart, NB extends BarChartViewerBase<C, N, NB, NM>, NM extends BarChartViewerMixin<C, N, NB, NM>>
        extends HtmlChartViewer<C, N, NB, NM>
        implements BarChartViewerMixin<C, N, NB, NM> {

    public HtmlBarChartViewer() {
        this((NB) new BarChartViewerBase());
    }

    public HtmlBarChartViewer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.BarChart();
    }
}
