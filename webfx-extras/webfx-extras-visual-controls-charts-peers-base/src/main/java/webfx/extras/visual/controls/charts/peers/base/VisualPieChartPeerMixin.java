package webfx.extras.visual.controls.charts.peers.base;

import webfx.extras.visual.controls.charts.VisualPieChart;

/**
 * @author Bruno Salmon
 */
public interface VisualPieChartPeerMixin
        <C, N extends VisualPieChart, NB extends VisualPieChartPeerBase<C, N, NB, NM>, NM extends VisualPieChartPeerMixin<C, N, NB, NM>>

        extends VisualChartPeerMixin<C, N, NB, NM> {
}
