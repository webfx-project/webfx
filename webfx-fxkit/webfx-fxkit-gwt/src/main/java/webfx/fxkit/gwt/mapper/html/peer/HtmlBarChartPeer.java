package webfx.fxkit.gwt.mapper.html.peer;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import webfx.fxkit.extra.chart.BarChart;
import webfx.fxkit.extra.mapper.spi.peer.impl.BarChartPeerBase;
import webfx.fxkit.extra.mapper.spi.peer.impl.BarChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlBarChartPeer
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
