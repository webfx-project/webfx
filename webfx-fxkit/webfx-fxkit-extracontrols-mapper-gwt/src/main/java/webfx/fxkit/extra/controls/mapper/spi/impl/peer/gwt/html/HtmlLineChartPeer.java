package webfx.fxkit.extra.controls.mapper.spi.impl.peer.gwt.html;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import webfx.fxkit.extra.controls.displaydata.chart.LineChart;
import webfx.fxkit.extra.controls.mapper.spi.peer.impl.base.LineChartPeerBase;
import webfx.fxkit.extra.controls.mapper.spi.peer.impl.base.LineChartPeerMixin;

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
