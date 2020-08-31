package webfx.extras.visual.controls.charts.peers.gwt.charba;

import org.pepstock.charba.client.ScatterChart;
import org.pepstock.charba.client.configuration.ConfigurationOptions;
import org.pepstock.charba.client.configuration.Scales;
import org.pepstock.charba.client.configuration.ScatterOptions;
import webfx.extras.visual.controls.charts.VisualScatterChart;
import webfx.extras.visual.controls.charts.peers.base.VisualScatterChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualScatterChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class GwtCharbaVisualScatterChartPeer
        <C, N extends VisualScatterChart, NB extends VisualScatterChartPeerBase<C, N, NB, NM>, NM extends VisualScatterChartPeerMixin<C, N, NB, NM>>
        extends GwtCharbaVisualChartPeer<C, N, NB, NM>
        implements VisualScatterChartPeerMixin<C, N, NB, NM> {

    public GwtCharbaVisualScatterChartPeer() {
        this((NB) new VisualScatterChartPeerBase());
    }

    public GwtCharbaVisualScatterChartPeer(NB base) {
        super(base);
    }

    @Override
    protected ScatterChart createChartWidget() {
        return new ScatterChart();
    }

    @Override
    protected Scales getScales(ConfigurationOptions options) {
        return ((ScatterOptions) options).getScales();
    }

}
