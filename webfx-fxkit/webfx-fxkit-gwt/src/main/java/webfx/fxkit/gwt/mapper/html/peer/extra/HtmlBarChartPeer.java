package webfx.fxkit.gwt.mapper.html.peer.extra;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import webfx.fxkit.extra.controls.displaydata.chart.BarChart;
import webfx.fxkit.extra.mapper.spi.peer.impl.extra.BarChartPeerBase;
import webfx.fxkit.extra.mapper.spi.peer.impl.extra.BarChartPeerMixin;

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
