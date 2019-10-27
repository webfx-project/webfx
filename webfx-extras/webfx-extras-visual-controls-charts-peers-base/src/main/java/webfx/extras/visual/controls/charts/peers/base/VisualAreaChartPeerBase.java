package webfx.extras.visual.controls.charts.peers.base;

import webfx.extras.visual.controls.charts.VisualAreaChart;

/**
 * @author Bruno Salmon
 */
public class VisualAreaChartPeerBase
        <C, N extends VisualAreaChart, NB extends VisualAreaChartPeerBase<C, N, NB, NM>, NM extends VisualAreaChartPeerMixin<C, N, NB, NM>>

        extends VisualChartPeerBase<C, N, NB, NM> {
}
