package webfx.fxkit.extra.controls.mapper.spi.impl.peer.javafx;

import webfx.fxkit.extra.controls.displaydata.chart.AreaChart;
import webfx.fxkit.extra.controls.mapper.spi.peer.impl.base.AreaChartPeerMixin;
import webfx.fxkit.extra.controls.mapper.spi.peer.impl.base.AreaChartPeerBase;

/**
 * @author Bruno Salmon
 */
public final class FxAreaChartPeer
        <FxN extends javafx.scene.chart.AreaChart, N extends AreaChart, NB extends AreaChartPeerBase<FxN, N, NB, NM>, NM extends AreaChartPeerMixin<FxN, N, NB, NM>>

        extends FxXYChartPeer<FxN, N, NB, NM>
        implements AreaChartPeerMixin<FxN, N, NB, NM> {

    public FxAreaChartPeer() {
        super((NB) new AreaChartPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        // The API requires the axis to be defined now whereas we don't know the structure to display yet
        // So assuming category on x axis and number on y axis (is it possible to generify that?)
        javafx.scene.chart.AreaChart<String, Number> areaChart = new javafx.scene.chart.AreaChart<>(createCategoryAxis(), createNumberAxis());
        areaChart.setCreateSymbols(false);
        return (FxN) areaChart;
    }
}
