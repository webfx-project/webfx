package naga.fx.spi.gwt.html.peer;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.fxdata.chart.BarChart;
import naga.fxdata.spi.peer.base.BarChartPeerBase;
import naga.fxdata.spi.peer.base.BarChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlBarChartPeer
        <C, N extends BarChart, NB extends BarChartPeerBase<C, N, NB, NM>, NM extends BarChartPeerMixin<C, N, NB, NM>>
        extends HtmlChartPeer<C, N, NB, NM>
        implements BarChartPeerMixin<C, N, NB, NM> {

    public HtmlBarChartPeer() {
        this((NB) new BarChartPeerBase());
    }

    public HtmlBarChartPeer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.BarChart();
    }
}
