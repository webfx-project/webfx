package naga.toolkit.adapters.chart;

import naga.commons.type.Type;
import naga.commons.util.function.Function;

/**
 * @author Bruno Salmon
 */
public interface ChartAdapter {

    void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter);

    void setChartDataX(Object xValue, int pointIndex);

    void setChartDataY(Object yValue, int pointIndex, int seriesIndex);

    void applyChartData();

}
