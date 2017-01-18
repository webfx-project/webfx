package naga.fxdata.spi.peer.base;

import naga.commons.type.Type;
import naga.commons.util.function.Function;
import naga.fxdata.chart.Chart;
import naga.fxdata.displaydata.DisplayResultSet;

/**
 * @author Bruno Salmon
 */
public interface ChartPeerMixin
        <C, N extends Chart, NB extends ChartPeerBase<C, N, NB, NM>, NM extends ChartPeerMixin<C, N, NB, NM>>

        extends SelectableDisplayResultSetControlPeerMixin<C, N, NB, NM> {

    @Override
    default void updateResultSet(DisplayResultSet rs) {
        getNodePeerBase().updateResultSet(rs);
    }

    void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter);

    void setChartDataX(Object xValue, int pointIndex);

    void setChartDataY(Object yValue, int pointIndex, int seriesIndex);

    void applyChartData();

}
