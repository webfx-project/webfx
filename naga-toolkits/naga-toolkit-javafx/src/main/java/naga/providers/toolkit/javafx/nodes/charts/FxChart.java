package naga.providers.toolkit.javafx.nodes.charts;

import javafx.scene.chart.NumberAxis;
import naga.providers.toolkit.javafx.nodes.FxSelectableDisplayResultSetNode;
import naga.toolkit.adapters.chart.ChartAdapter;
import naga.toolkit.adapters.chart.ChartFiller;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.nodes.charts.Chart;

/**
 * @author Bruno Salmon
 */
public abstract class FxChart<N extends javafx.scene.chart.Chart> extends FxSelectableDisplayResultSetNode<N> implements Chart<N>, ChartAdapter {

    private final ChartFiller chartFiller = new ChartFiller(this, this);

    public FxChart(N chart) {
        super(chart);
        chart.setAnimated(false);
    }

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {
    }

    @Override
    protected void syncVisualDisplayResult(DisplayResultSet rs) {
        chartFiller.fillChart(rs);
    }

    protected static NumberAxis createNumberAxis() {
        NumberAxis axis = new NumberAxis();
        axis.setForceZeroInRange(false);
        return axis;
    }

}
