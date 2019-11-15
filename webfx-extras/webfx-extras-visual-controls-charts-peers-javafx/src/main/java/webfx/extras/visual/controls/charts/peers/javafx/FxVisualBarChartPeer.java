package webfx.extras.visual.controls.charts.peers.javafx;

import javafx.scene.chart.BarChart;
import webfx.extras.visual.controls.charts.VisualBarChart;
import webfx.extras.visual.controls.charts.peers.base.VisualBarChartPeerBase;
import webfx.extras.visual.controls.charts.peers.base.VisualBarChartPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class FxVisualBarChartPeer
        <FxN extends BarChart, N extends VisualBarChart, NB extends VisualBarChartPeerBase<FxN, N, NB, NM>, NM extends VisualBarChartPeerMixin<FxN, N, NB, NM>>

        extends FxVisualXYChartPeer<FxN, N, NB, NM>
        implements VisualBarChartPeerMixin<FxN, N, NB, NM> {

    public FxVisualBarChartPeer() {
        super((NB) new VisualBarChartPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        // The API requires the axis to be defined now whereas we don't know the structure to display yet
        // So assuming category on x axis and number on y axis (is it possible to generify that?)
        return (FxN) new BarChart(createCategoryAxis(), createNumberAxis());
    }
}
