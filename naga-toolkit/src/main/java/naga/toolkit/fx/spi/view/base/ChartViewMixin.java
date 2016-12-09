package naga.toolkit.fx.spi.view.base;

import naga.commons.type.Type;
import naga.commons.util.function.Function;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.fx.ext.chart.Chart;
import naga.toolkit.fx.spi.view.ChartView;

/**
 * @author Bruno Salmon
 */
public interface ChartViewMixin
        <C, N extends Chart, NV extends ChartViewBase<C, N, NV, NM>, NM extends ChartViewMixin<C, N, NV, NM>>

        extends ChartView<N>,
        SelectableDisplayResultSetControlViewMixin<C, N, NV, NM> {

    @Override
    default void updateResultSet(DisplayResultSet rs) {
        getNodeViewBase().updateResultSet(rs);
    }

    void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter);

    void setChartDataX(Object xValue, int pointIndex);

    void setChartDataY(Object yValue, int pointIndex, int seriesIndex);

    void applyChartData();

}
