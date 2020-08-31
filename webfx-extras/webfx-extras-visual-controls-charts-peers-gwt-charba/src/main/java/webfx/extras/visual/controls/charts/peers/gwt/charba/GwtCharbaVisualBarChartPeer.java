package webfx.extras.visual.controls.charts.peers.gwt.charba;

import org.pepstock.charba.client.BarChart;
import org.pepstock.charba.client.configuration.BarOptions;
import org.pepstock.charba.client.configuration.ConfigurationOptions;
import org.pepstock.charba.client.configuration.Scales;
import webfx.extras.visual.controls.charts.VisualBarChart;
import webfx.extras.visual.controls.charts.peers.base.VisualBarChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualBarChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class GwtCharbaVisualBarChartPeer
        <C, N extends VisualBarChart, NB extends VisualBarChartPeerBase<C, N, NB, NM>, NM extends VisualBarChartPeerMixin<C, N, NB, NM>>
        extends GwtCharbaVisualChartPeer<C, N, NB, NM>
        implements VisualBarChartPeerMixin<C, N, NB, NM> {

    public GwtCharbaVisualBarChartPeer() {
        this((NB) new VisualBarChartPeerBase());
    }

    public GwtCharbaVisualBarChartPeer(NB base) {
        super(base);
    }

    @Override
    protected BarChart createChartWidget() {
        return new BarChart();
    }

    @Override
    protected Scales getScales(ConfigurationOptions options) {
        return ((BarOptions) options).getScales();
    }

}
