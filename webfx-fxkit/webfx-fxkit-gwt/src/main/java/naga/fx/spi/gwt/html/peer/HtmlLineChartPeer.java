package naga.fx.spi.gwt.html.peer;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.fxdata.chart.LineChart;
import naga.fxdata.spi.peer.base.LineChartPeerBase;
import naga.fxdata.spi.peer.base.LineChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlLineChartPeer
        <C, N extends LineChart, NB extends LineChartPeerBase<C, N, NB, NM>, NM extends LineChartPeerMixin<C, N, NB, NM>>
        extends HtmlChartPeer<C, N, NB, NM>
        implements LineChartPeerMixin<C, N, NB, NM> {

    public HtmlLineChartPeer() {
        this((NB) new LineChartPeerBase());
    }

    public HtmlLineChartPeer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.LineChart();
    }
}
