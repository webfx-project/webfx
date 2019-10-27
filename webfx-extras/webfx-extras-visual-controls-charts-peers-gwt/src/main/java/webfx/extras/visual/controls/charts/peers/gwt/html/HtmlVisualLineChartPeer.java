package webfx.extras.visual.controls.charts.peers.gwt.html;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import webfx.extras.visual.controls.charts.peers.base.VisualLineChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualLineChartPeerMixin;
import webfx.extras.visual.controls.charts.VisualLineChart;

/**
 * @author Bruno Salmon
 */
public final class HtmlVisualLineChartPeer
        <C, N extends VisualLineChart, NB extends VisualLineChartPeerBase<C, N, NB, NM>, NM extends VisualLineChartPeerMixin<C, N, NB, NM>>
        extends HtmlVisualChartPeer<C, N, NB, NM>
        implements VisualLineChartPeerMixin<C, N, NB, NM> {

    public HtmlVisualLineChartPeer() {
        this((NB) new VisualLineChartPeerBase());
    }

    public HtmlVisualLineChartPeer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.LineChart();
    }
}
