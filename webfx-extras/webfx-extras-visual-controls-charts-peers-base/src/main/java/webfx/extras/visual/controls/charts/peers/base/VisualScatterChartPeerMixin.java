package webfx.extras.visual.controls.charts.peers.base;

import webfx.extras.visual.controls.charts.VisualScatterChart;

/**
 * @author Bruno Salmon
 */
public interface VisualScatterChartPeerMixin
        <C, N extends VisualScatterChart, NB extends VisualScatterChartPeerBase<C, N, NB, NM>, NM extends VisualScatterChartPeerMixin<C, N, NB, NM>>

        extends VisualChartPeerMixin<C, N, NB, NM> {
}
