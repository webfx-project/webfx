package naga.toolkit.fx.spi.viewer.base;

import naga.commons.type.Type;
import naga.commons.util.function.Function;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.fx.ext.chart.Chart;

/**
 * @author Bruno Salmon
 */
public interface ChartViewerMixin
        <C, N extends Chart, NB extends ChartViewerBase<C, N, NB, NM>, NM extends ChartViewerMixin<C, N, NB, NM>>

        extends SelectableDisplayResultSetControlViewerMixin<C, N, NB, NM> {

    @Override
    default void updateResultSet(DisplayResultSet rs) {
        getNodeViewerBase().updateResultSet(rs);
    }

    void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter);

    void setChartDataX(Object xValue, int pointIndex);

    void setChartDataY(Object yValue, int pointIndex, int seriesIndex);

    void applyChartData();

}
