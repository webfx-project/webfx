package webfx.fxkit.gwt.mapper.html.peer.extra;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import webfx.fxkit.extra.controls.displaydata.chart.AreaChart;
import webfx.fxkit.extra.controls.mapper.spi.peer.impl.base.AreaChartPeerBase;
import webfx.fxkit.extra.controls.mapper.spi.peer.impl.base.AreaChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlAreaChartPeer
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
