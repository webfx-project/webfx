package naga.toolkit.adapters.chart;

import naga.commons.type.PrimType;
import naga.commons.type.Type;
import naga.commons.util.Strings;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.spi.nodes.charts.Chart;
import naga.toolkit.spi.nodes.charts.PieChart;

/**
 * @author Bruno Salmon
 */
public class ChartFiller {

    protected final boolean isPieChart;
    private final ChartAdapter chartAdapter;

    public ChartFiller(Chart chart, ChartAdapter chartAdapter) {
        this(chart instanceof PieChart, chartAdapter);
    }

    public ChartFiller(boolean isPieChart, ChartAdapter chartAdapter) {
        this.isPieChart = isPieChart;
        this.chartAdapter = chartAdapter;
    }

    public boolean isPieChart() {
        return isPieChart;
    }

    public void fillChart(DisplayResultSet rs) {
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
            chartAdapter.createChartData(xType, yType, pointPerSeriesCount, seriesCount, seriesIndex -> columns[firstSeriesColumnIndex + seriesIndex].getName());
            for (int pointIndex = 0; pointIndex < pointPerSeriesCount; pointIndex++) {
                if (hasXAxis)
                    chartAdapter.setChartDataX(rs.getValue(pointIndex, 0), pointIndex);
                for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                    Object yValue = rs.getValue(pointIndex, firstSeriesColumnIndex + seriesIndex);
                    chartAdapter.setChartDataY(yValue, pointIndex, seriesIndex);
                }
            }
        } else {          /***** Row format - see {@link Chart} for format description *****/
            if (isPieChart && columnCount > 2) // ignoring extra columns for pie charts
                columnCount = 2;
            int seriesCount = rowCount;
            int pointPerSeriesCount = columnCount - 1;
            Type xType = hasXAxis ? PrimType.fromObject(columns[1].getName()) : null;
            Type yType = columns[pointPerSeriesCount].getType();
            chartAdapter.createChartData(xType, yType, pointPerSeriesCount, seriesCount, seriesIndex -> Strings.toString(rs.getValue(seriesIndex, 0)));
            for (int pointIndex = 0; pointIndex < pointPerSeriesCount; pointIndex++) {
                if (hasXAxis)
                    chartAdapter.setChartDataX(columns[pointIndex + 1].getName(), pointIndex);
                for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                    Object yValue = rs.getValue(seriesIndex, pointIndex + 1);
                    chartAdapter.setChartDataY(yValue, pointIndex, seriesIndex);
                }
            }
        }
        chartAdapter.applyChartData();
    }

}