package webfx.extras.visual.controls.charts.peers.gwt.html;

import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import webfx.extras.visual.controls.charts.peers.base.VisualBarChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualBarChartPeerMixin;
import webfx.extras.visual.controls.charts.VisualBarChart;

/**
 * @author Bruno Salmon
 */
public final class HtmlVisualBarChartPeer
        <C, N extends VisualBarChart, NB extends VisualBarChartPeerBase<C, N, NB, NM>, NM extends VisualBarChartPeerMixin<C, N, NB, NM>>
        extends HtmlVisualChartPeer<C, N, NB, NM>
        implements VisualBarChartPeerMixin<C, N, NB, NM> {

    public HtmlVisualBarChartPeer() {
        this((NB) new VisualBarChartPeerBase());
    }

    public HtmlVisualBarChartPeer(NB base) {
        super(base);
    }

    @Override
    protected CoreChartWidget createChartWidget() {
        return new com.googlecode.gwt.charts.client.corechart.ColumnChart(); // Vertical Bar chart
    }
}
