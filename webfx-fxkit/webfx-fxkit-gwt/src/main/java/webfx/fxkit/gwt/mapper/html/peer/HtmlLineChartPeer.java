package webfx.fxkit.gwt.mapper.html.peer;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import webfx.fxkit.extra.chart.LineChart;
import webfx.fxkit.extra.mapper.spi.peer.impl.LineChartPeerBase;
import webfx.fxkit.extra.mapper.spi.peer.impl.LineChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlLineChartPeer
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
