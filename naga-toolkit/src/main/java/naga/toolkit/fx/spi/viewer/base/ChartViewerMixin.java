package naga.toolkit.fx.spi.viewer.base;

import naga.commons.type.Type;
import naga.commons.util.function.Function;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.fx.ext.chart.Chart;
import naga.toolkit.fx.spi.viewer.ChartViewer;

/**
 * @author Bruno Salmon
 */
public interface ChartViewerMixin
        <C, N extends Chart, NV extends ChartViewerBase<C, N, NV, NM>, NM extends ChartViewerMixin<C, N, NV, NM>>

        extends ChartViewer<N>,
        SelectableDisplayResultSetControlViewerMixin<C, N, NV, NM> {

    @Override
    default void updateResultSet(DisplayResultSet rs) {
        getNodeViewerBase().updateResultSet(rs);
    }

    void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter);

    void setChartDataX(Object xValue, int pointIndex);

    void setChartDataY(Object yValue, int pointIndex, int seriesIndex);

    void applyChartData();

}
