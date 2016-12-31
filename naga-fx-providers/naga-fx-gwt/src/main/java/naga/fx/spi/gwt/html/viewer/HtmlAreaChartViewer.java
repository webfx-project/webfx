package naga.fx.spi.gwt.html.viewer;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.fxdata.chart.AreaChart;
import naga.fxdata.spi.viewer.base.AreaChartViewerBase;
import naga.fxdata.spi.viewer.base.AreaChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlAreaChartViewer
        <C, N extends AreaChart, NB extends AreaChartViewerBase<C, N, NB, NM>, NM extends AreaChartViewerMixin<C, N, NB, NM>>
        extends HtmlChartViewer<C, N, NB, NM>
        implements AreaChartViewerMixin<C, N, NB, NM> {

    public HtmlAreaChartViewer() {
        this((NB) new AreaChartViewerBase());
    }

    public HtmlAreaChartViewer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.AreaChart();
    }
}
