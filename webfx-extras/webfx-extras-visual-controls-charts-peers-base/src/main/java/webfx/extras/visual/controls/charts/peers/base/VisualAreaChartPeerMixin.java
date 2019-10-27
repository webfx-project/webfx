package webfx.extras.visual.controls.charts.peers.base;

import webfx.extras.visual.controls.charts.VisualAreaChart;

/**
 * @author Bruno Salmon
 */
public interface VisualAreaChartPeerMixin
        <C, N extends VisualAreaChart, NB extends VisualAreaChartPeerBase<C, N, NB, NM>, NM extends VisualAreaChartPeerMixin<C, N, NB, NM>>

        extends VisualChartPeerMixin<C, N, NB, NM> {
}
