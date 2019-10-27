package webfx.extras.visual.controls.charts.peers.base;

import webfx.extras.visual.controls.charts.VisualLineChart;

/**
 * @author Bruno Salmon
 */
public interface VisualLineChartPeerMixin
        <C, N extends VisualLineChart, NB extends VisualLineChartPeerBase<C, N, NB, NM>, NM extends VisualLineChartPeerMixin<C, N, NB, NM>>

        extends VisualChartPeerMixin<C, N, NB, NM> {
}
