package webfx.extras.visual.controls.charts.peers.gwt.html;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import webfx.extras.visual.controls.charts.peers.base.VisualAreaChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualAreaChartPeerMixin;
import webfx.extras.visual.controls.charts.VisualAreaChart;

/**
 * @author Bruno Salmon
 */
public final class HtmlVisualAreaChartPeer
        <C, N extends VisualAreaChart, NB extends VisualAreaChartPeerBase<C, N, NB, NM>, NM extends VisualAreaChartPeerMixin<C, N, NB, NM>>
        extends HtmlVisualChartPeer<C, N, NB, NM>
        implements VisualAreaChartPeerMixin<C, N, NB, NM> {

    public HtmlVisualAreaChartPeer() {
        this((NB) new VisualAreaChartPeerBase());
    }

    public HtmlVisualAreaChartPeer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.AreaChart();
    }
}
