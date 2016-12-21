package naga.providers.toolkit.javafx.fx.viewer;

import javafx.scene.chart.XYChart;
import naga.commons.type.Type;
import naga.commons.util.function.Function;
import naga.toolkit.fxdata.chart.Chart;
import naga.toolkit.fxdata.spi.viewer.base.ChartViewerBase;
import naga.toolkit.fxdata.spi.viewer.base.ChartViewerMixin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class FxXYChartViewer
        <FxN extends javafx.scene.chart.XYChart, N extends Chart, NB extends ChartViewerBase<FxN, N, NB, NM>, NM extends ChartViewerMixin<FxN, N, NB, NM>>
        extends FxChartViewer<FxN, N, NB, NM> {

    private List<XYChart.Series> seriesList;
    private Object xValue;

    public FxXYChartViewer(NB base) {
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
