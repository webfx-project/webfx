package webfx.extras.visual.controls.charts.peers.base;

import webfx.extras.visual.controls.charts.VisualBarChart;

/**
 * @author Bruno Salmon
 */
public interface VisualBarChartPeerMixin
        <C, N extends VisualBarChart, NB extends VisualBarChartPeerBase<C, N, NB, NM>, NM extends VisualBarChartPeerMixin<C, N, NB, NM>>

        extends VisualChartPeerMixin<C, N, NB, NM> {
}
