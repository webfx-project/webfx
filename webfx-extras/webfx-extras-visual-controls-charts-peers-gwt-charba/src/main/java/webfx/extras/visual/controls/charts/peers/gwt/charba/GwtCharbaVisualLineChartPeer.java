package webfx.extras.visual.controls.charts.peers.gwt.charba;

import org.pepstock.charba.client.LineChart;
import org.pepstock.charba.client.configuration.ConfigurationOptions;
import org.pepstock.charba.client.configuration.LineOptions;
import org.pepstock.charba.client.configuration.Scales;
import webfx.extras.visual.controls.charts.VisualLineChart;
import webfx.extras.visual.controls.charts.peers.base.VisualLineChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualLineChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class GwtCharbaVisualLineChartPeer
        <C, N extends VisualLineChart, NB extends VisualLineChartPeerBase<C, N, NB, NM>, NM extends VisualLineChartPeerMixin<C, N, NB, NM>>
        extends GwtCharbaVisualChartPeer<C, N, NB, NM>
        implements VisualLineChartPeerMixin<C, N, NB, NM> {

    public GwtCharbaVisualLineChartPeer() {
        this((NB) new VisualLineChartPeerBase());
    }

    public GwtCharbaVisualLineChartPeer(NB base) {
        super(base);
    }

    @Override
    protected LineChart createChartWidget() {
        return new LineChart();
    }

    @Override
    protected Scales getScales(ConfigurationOptions options) {
        return ((LineOptions) options).getScales();
    }

}
