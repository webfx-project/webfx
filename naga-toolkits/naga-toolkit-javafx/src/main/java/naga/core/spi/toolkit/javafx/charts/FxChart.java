package naga.core.spi.toolkit.javafx.charts;

import javafx.scene.chart.PieChart;
import naga.core.spi.toolkit.charts.Chart;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.spi.toolkit.javafx.node.FxSelectableDisplayResultSetNode;
import naga.core.type.PrimType;
import naga.core.type.Type;
import naga.core.ui.displayresultset.DisplayColumn;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.util.Strings;

/**
 * @author Bruno Salmon
 */
public abstract class FxChart extends FxSelectableDisplayResultSetNode<javafx.scene.chart.Chart> implements Chart<javafx.scene.chart.Chart> {

    private final boolean isPieChart;

    public FxChart(javafx.scene.chart.Chart chart) {
        super(chart);
        isPieChart = chart instanceof PieChart;
    }

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {

    }

    @Override
    protected void syncVisualDisplayResult(DisplayResultSet rs) {
        int rowCount = rs.getRowCount();
        int columnCount = rs.getColumnCount();
        DisplayColumn[] columns = rs.getColumns();
        boolean rowFormat = columns[0].getRole() == null;
        if (!rowFormat) { /***** Column format - see {@link Chart} for format description *****/
            if (isPieChart && rowCount > 1) // ignoring extra rows for pie chart
                rowCount = 1;
            int seriesCount = isPieChart ? columnCount : columnCount - 1;
            int pointPerSeriesCount = rowCount;
            Type xType = isPieChart ? null : columns[0].getType();
            Type yType = columns[isPieChart ? 0 : 1].getType();
            createChartData(seriesCount, pointPerSeriesCount, xType, yType);
            for (int columnIndex = 1; columnIndex < columnCount; columnIndex++) {
                String seriesName = columns[columnIndex].getName();
                startSeries(seriesName);
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex ++) {
                    Object xValue = isPieChart ? null : rs.getValue(rowIndex, 0);
                    Object yValue = rs.getValue(rowIndex, columnIndex);
                    addPointToCurrentSeries(xValue, yValue);
                }
            }
        } else {          /***** Row format - see {@link Chart} for format description *****/
            if (isPieChart && columnCount > 2) // ignoring extra columns for pie charts
                columnCount = 2;
            int seriesCount = rowCount;
            int pointPerSeriesCount = columnCount - 1;
            Type xType = isPieChart ? null : PrimType.fromObject(columns[1].getName());
            Type yType = columns[pointPerSeriesCount].getType();
            createChartData(seriesCount, pointPerSeriesCount, xType, yType);
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex ++) {
                String seriesName = Strings.toString(rs.getValue(rowIndex, 0));
                startSeries(seriesName);
                for (int columnIndex = 1; columnIndex < columnCount; columnIndex++) {
                    Object xValue = isPieChart ? null : columns[columnIndex].getName();
                    Object yValue = rs.getValue(rowIndex, columnIndex);
                    addPointToCurrentSeries(xValue, yValue);
                }
            }
        }
        applyChartData();
    }

    protected abstract void createChartData(int seriesCount, int pointPerSeriesCount, Type xType, Type yType);

    protected abstract void startSeries(String name);

    protected abstract void addPointToCurrentSeries(Object xValue, Object yValue);

    protected abstract void applyChartData();

}
