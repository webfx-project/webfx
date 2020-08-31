package webfx.extras.visual.controls.charts.peers.gwt.charba;

import org.pepstock.charba.client.PieChart;
import org.pepstock.charba.client.configuration.ConfigurationOptions;
import org.pepstock.charba.client.configuration.Scales;
import webfx.extras.visual.controls.charts.VisualPieChart;
import webfx.extras.visual.controls.charts.peers.base.VisualPieChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualPieChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class GwtCharbaVisualPieChartPeer
        <C, N extends VisualPieChart, NB extends VisualPieChartPeerBase<C, N, NB, NM>, NM extends VisualPieChartPeerMixin<C, N, NB, NM>>
        extends GwtCharbaVisualChartPeer<C, N, NB, NM>
        implements VisualPieChartPeerMixin<C, N, NB, NM> {

    public GwtCharbaVisualPieChartPeer() {
        this((NB) new VisualPieChartPeerBase());
    }

    public GwtCharbaVisualPieChartPeer(NB base) {
        super(base);
    }

    @Override
    protected PieChart createChartWidget() {
        return new PieChart();
    }

    @Override
    protected Scales getScales(ConfigurationOptions options) {
        return null;
    }

}
