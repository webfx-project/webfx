package webfx.extras.visual.controls.charts.peers.gwt.html;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import webfx.extras.visual.controls.charts.peers.base.VisualScatterChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualScatterChartPeerMixin;
import webfx.extras.visual.controls.charts.VisualScatterChart;

/**
 * @author Bruno Salmon
 */
public final class HtmlVisualScatterChartPeer
        <C, N extends VisualScatterChart, NB extends VisualScatterChartPeerBase<C, N, NB, NM>, NM extends VisualScatterChartPeerMixin<C, N, NB, NM>>
        extends HtmlVisualChartPeer<C, N, NB, NM>
        implements VisualScatterChartPeerMixin<C, N, NB, NM> {

    public HtmlVisualScatterChartPeer() {
        this((NB) new VisualScatterChartPeerBase());
    }

    public HtmlVisualScatterChartPeer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.ScatterChart();
    }
}
