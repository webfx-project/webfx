package webfx.extras.visual.controls.charts.peers.gwt.html;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import webfx.extras.visual.controls.charts.peers.base.VisualPieChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualPieChartPeerMixin;
import webfx.extras.visual.controls.charts.VisualPieChart;

/**
 * @author Bruno Salmon
 */
public final class HtmlVisualPieChartPeer
        <C, N extends VisualPieChart, NB extends VisualPieChartPeerBase<C, N, NB, NM>, NM extends VisualPieChartPeerMixin<C, N, NB, NM>>
        extends HtmlVisualChartPeer<C, N, NB, NM>
        implements VisualPieChartPeerMixin<C, N, NB, NM> {

    public HtmlVisualPieChartPeer() {
        this((NB) new VisualPieChartPeerBase());
    }

    public HtmlVisualPieChartPeer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.PieChart();
    }
}
