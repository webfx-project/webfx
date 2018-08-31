package webfx.fx.spi.gwt.html.peer;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import webfx.fxdata.chart.ScatterChart;
import webfx.fxdata.spi.peer.base.ScatterChartPeerBase;
import webfx.fxdata.spi.peer.base.ScatterChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlScatterChartPeer
        <C, N extends ScatterChart, NB extends ScatterChartPeerBase<C, N, NB, NM>, NM extends ScatterChartPeerMixin<C, N, NB, NM>>
        extends HtmlChartPeer<C, N, NB, NM>
        implements ScatterChartPeerMixin<C, N, NB, NM> {

    public HtmlScatterChartPeer() {
        this((NB) new ScatterChartPeerBase());
    }

    public HtmlScatterChartPeer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.ScatterChart();
    }
}
