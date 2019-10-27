package webfx.extras.visual.controls.charts.peers.base;

import webfx.extras.visual.controls.charts.VisualBarChart;

/**
 * @author Bruno Salmon
 */
public class VisualBarChartPeerBase
        <C, N extends VisualBarChart, NB extends VisualBarChartPeerBase<C, N, NB, NM>, NM extends VisualBarChartPeerMixin<C, N, NB, NM>>

        extends VisualChartPeerBase<C, N, NB, NM> {
}
