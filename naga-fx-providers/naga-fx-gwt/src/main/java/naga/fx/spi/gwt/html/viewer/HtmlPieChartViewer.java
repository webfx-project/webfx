package naga.fx.spi.gwt.html.viewer;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.fxdata.chart.PieChart;
import naga.fxdata.spi.viewer.base.PieChartViewerBase;
import naga.fxdata.spi.viewer.base.PieChartViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlPieChartViewer
        <C, N extends PieChart, NB extends PieChartViewerBase<C, N, NB, NM>, NM extends PieChartViewerMixin<C, N, NB, NM>>
        extends HtmlChartViewer<C, N, NB, NM>
        implements PieChartViewerMixin<C, N, NB, NM> {

    public HtmlPieChartViewer() {
        this((NB) new PieChartViewerBase());
    }

    public HtmlPieChartViewer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.PieChart();
    }
}
