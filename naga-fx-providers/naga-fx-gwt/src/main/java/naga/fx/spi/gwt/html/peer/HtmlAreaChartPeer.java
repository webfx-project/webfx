package naga.fx.spi.gwt.html.peer;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import naga.fxdata.chart.AreaChart;
import naga.fxdata.spi.peer.base.AreaChartPeerBase;
import naga.fxdata.spi.peer.base.AreaChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlAreaChartPeer
        <C, N extends AreaChart, NB extends AreaChartPeerBase<C, N, NB, NM>, NM extends AreaChartPeerMixin<C, N, NB, NM>>
        extends HtmlChartPeer<C, N, NB, NM>
        implements AreaChartPeerMixin<C, N, NB, NM> {

    public HtmlAreaChartPeer() {
        this((NB) new AreaChartPeerBase());
    }

    public HtmlAreaChartPeer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.AreaChart();
    }
}
