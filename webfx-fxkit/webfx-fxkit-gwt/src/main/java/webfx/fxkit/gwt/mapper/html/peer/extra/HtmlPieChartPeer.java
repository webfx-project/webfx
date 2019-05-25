package webfx.fxkit.gwt.mapper.html.peer.extra;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import webfx.fxkit.extra.controls.displaydata.chart.PieChart;
import webfx.fxkit.extra.controls.mapper.spi.peer.impl.base.PieChartPeerMixin;
import webfx.fxkit.extra.controls.mapper.spi.peer.impl.base.PieChartPeerBase;

/**
 * @author Bruno Salmon
 */
public final class HtmlPieChartPeer
        <C, N extends PieChart, NB extends PieChartPeerBase<C, N, NB, NM>, NM extends PieChartPeerMixin<C, N, NB, NM>>
        extends HtmlChartPeer<C, N, NB, NM>
        implements PieChartPeerMixin<C, N, NB, NM> {

    public HtmlPieChartPeer() {
        this((NB) new PieChartPeerBase());
    }

    public HtmlPieChartPeer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.PieChart();
    }
}
