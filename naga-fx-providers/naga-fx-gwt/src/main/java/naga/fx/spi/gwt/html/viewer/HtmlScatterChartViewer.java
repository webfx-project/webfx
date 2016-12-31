package naga.fx.spi.gwt.html.viewer;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.fxdata.chart.ScatterChart;
import naga.fxdata.spi.viewer.base.ScatterChartViewerBase;
import naga.fxdata.spi.viewer.base.ScatterChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlScatterChartViewer
        <C, N extends ScatterChart, NB extends ScatterChartViewerBase<C, N, NB, NM>, NM extends ScatterChartViewerMixin<C, N, NB, NM>>
        extends HtmlChartViewer<C, N, NB, NM>
        implements ScatterChartViewerMixin<C, N, NB, NM> {

    public HtmlScatterChartViewer() {
        this((NB) new ScatterChartViewerBase());
    }

    public HtmlScatterChartViewer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.ScatterChart();
    }
}
