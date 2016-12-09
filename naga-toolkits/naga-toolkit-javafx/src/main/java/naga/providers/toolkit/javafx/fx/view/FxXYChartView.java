package naga.providers.toolkit.javafx.fx.view;

import javafx.scene.chart.XYChart;
import naga.commons.type.Type;
import naga.commons.util.function.Function;
import naga.toolkit.fx.ext.chart.Chart;
import naga.toolkit.fx.spi.view.base.ChartViewBase;
import naga.toolkit.fx.spi.view.base.ChartViewMixin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class FxXYChartView
        <FxN extends javafx.scene.chart.XYChart, N extends Chart, NV extends ChartViewBase<FxN, N, NV, NM>, NM extends ChartViewMixin<FxN, N, NV, NM>>
        extends FxChartView<FxN, N, NV, NM> {

    private List<XYChart.Series> seriesList;
    private Object xValue;

    public FxXYChartView(NV base) {
        super(base);
    }

    @Override
    public void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter) {
        seriesList = new ArrayList<>();
        for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
            XYChart.Series series = new XYChart.Series<>();
            series.setName(seriesNameGetter.apply(seriesIndex));
            seriesList.add(series);
        }
    }

    @Override
    public void setChartDataX(Object xValue, int pointIndex) {
        this.xValue = xValue;
    }

    @Override
    public void setChartDataY(Object yValue, int pointIndex, int seriesIndex) {
        seriesList.get(seriesIndex).getData().add(new XYChart.Data<>(xValue, yValue));
    }

    @Override
    public void applyChartData() {
        getFxNode().getData().setAll(seriesList);
    }
}
