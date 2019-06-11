package webfx.fxkit.extra.controls.mapper.spi.impl.peer.javafx;

import webfx.fxkit.extra.controls.displaydata.chart.BarChart;
import webfx.fxkit.extra.controls.mapper.spi.peer.impl.base.BarChartPeerMixin;
import webfx.fxkit.extra.controls.mapper.spi.peer.impl.base.BarChartPeerBase;

/**
 * @author Bruno Salmon
 */
public final class FxBarChartPeer
        <FxN extends javafx.scene.chart.BarChart, N extends BarChart, NB extends BarChartPeerBase<FxN, N, NB, NM>, NM extends BarChartPeerMixin<FxN, N, NB, NM>>

        extends FxXYChartPeer<FxN, N, NB, NM>
        implements BarChartPeerMixin<FxN, N, NB, NM> {

    public FxBarChartPeer() {
        super((NB) new BarChartPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        // The API requires the axis to be defined now whereas we don't know the structure to display yet
        // So assuming category on x axis and number on y axis (is it possible to generify that?)
        return (FxN) new javafx.scene.chart.BarChart(createCategoryAxis(), createNumberAxis());
    }
}
