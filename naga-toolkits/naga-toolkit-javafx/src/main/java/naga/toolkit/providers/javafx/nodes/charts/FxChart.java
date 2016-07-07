package naga.toolkit.providers.javafx.nodes.charts;

import javafx.scene.chart.PieChart;
import naga.toolkit.spi.nodes.charts.Chart;
import naga.toolkit.spi.properties.markers.SelectionMode;
import naga.toolkit.providers.javafx.nodes.FxSelectableDisplayResultSetNode;
import naga.commons.type.PrimType;
import naga.commons.type.Type;
import naga.toolkit.spi.display.DisplayColumn;
import naga.toolkit.spi.display.DisplayResultSet;
import naga.commons.util.Strings;
import naga.commons.util.function.Function;

/**
 * @author Bruno Salmon
 */
public abstract class FxChart extends FxSelectableDisplayResultSetNode<javafx.scene.chart.Chart> implements Chart<javafx.scene.chart.Chart> {

    private final boolean isPieChart;

    public FxChart(javafx.scene.chart.Chart chart) {
        super(chart);
        isPieChart = chart instanceof PieChart;
        chart.setAnimated(false);
    }

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {
    }

    @Override
    protected void syncVisualDisplayResult(DisplayResultSet rs) {
        int rowCount = rs.getRowCount();
        int columnCount = rs.getColumnCount();
        DisplayColumn[] columns = rs.getColumns();
        boolean rowFormat = "series".equals(columns[0].getRole());
        boolean hasXAxis = !isPieChart;
        if (!rowFormat) { /***** Column format - see {@link Chart} for format description *****/
            if (isPieChart && rowCount > 1) // ignoring extra rows for pie chart
                rowCount = 1;
            int firstSeriesColumnIndex = hasXAxis ? 1 : 0;
            int seriesCount = columnCount - firstSeriesColumnIndex;
            int pointPerSeriesCount = rowCount;
            Type xType = hasXAxis ? columns[0].getType() : null;
            Type yType = columns[firstSeriesColumnIndex].getType();
            createChartData(xType, yType, pointPerSeriesCount, seriesCount, seriesIndex -> columns[firstSeriesColumnIndex + seriesIndex].getName());
            for (int pointIndex = 0; pointIndex < pointPerSeriesCount; pointIndex++) {
                if (hasXAxis)
                    setChartDataX(rs.getValue(pointIndex, 0), pointIndex);
                for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                    Object yValue = rs.getValue(pointIndex, firstSeriesColumnIndex + seriesIndex);
                    setChartDataY(yValue, pointIndex, seriesIndex);
                }
            }
        } else {          /***** Row format - see {@link Chart} for format description *****/
            if (isPieChart && columnCount > 2) // ignoring extra columns for pie charts
                columnCount = 2;
            int seriesCount = rowCount;
            int pointPerSeriesCount = columnCount - 1;
            Type xType = hasXAxis ? PrimType.fromObject(columns[1].getName()) : null;
            Type yType = columns[pointPerSeriesCount].getType();
            createChartData(xType, yType, pointPerSeriesCount, seriesCount, seriesIndex -> Strings.toString(rs.getValue(seriesIndex, 0)));
            for (int pointIndex = 0; pointIndex < pointPerSeriesCount; pointIndex++) {
                if (hasXAxis)
                    setChartDataX(columns[pointIndex + 1].getName(), pointIndex);
                for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                    Object yValue = rs.getValue(seriesIndex, pointIndex + 1);
                    setChartDataY(yValue, pointIndex, seriesIndex);
                }
            }
        }
        applyChartData();
    }

    protected abstract void createChartData(Type xType, Type yType, int pointPerSeriesCount, int seriesCount, Function<Integer, String> seriesNameGetter);

    protected abstract void setChartDataX(Object xValue, int pointIndex);

    protected abstract void setChartDataY(Object yValue, int pointIndex, int seriesIndex);

    protected abstract void applyChartData();

}
