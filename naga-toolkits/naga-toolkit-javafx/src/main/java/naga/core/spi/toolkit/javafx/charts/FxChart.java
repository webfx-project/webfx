package naga.core.spi.toolkit.javafx.charts;

import javafx.scene.chart.PieChart;
import naga.core.spi.toolkit.charts.Chart;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.spi.toolkit.javafx.node.FxSelectableDisplayResultSetNode;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.util.Strings;

/**
 * @author Bruno Salmon
 */
public abstract class FxChart extends FxSelectableDisplayResultSetNode<javafx.scene.chart.Chart> implements Chart<javafx.scene.chart.Chart> {

    protected int seriesMaxPointsCount;
    protected boolean seriesAreColumns; // first column = point name
    //Setting to false means series are rows and first column = series name, other columns = point names

    public FxChart(javafx.scene.chart.Chart chart) {
        super(chart);
        if (chart instanceof PieChart) {
            seriesAreColumns = false;
            seriesMaxPointsCount = 1;
        } else {
            seriesAreColumns = true;
            seriesMaxPointsCount = -1;
        }
    }

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {

    }

    @Override
    protected void syncVisualDisplayResult(DisplayResultSet displayResultSet) {
        createChartData();
        int columnCount = displayResultSet.getColumnCount();
        int rowCount = displayResultSet.getRowCount();
        if (seriesAreColumns) {
            if (seriesMaxPointsCount > 0 && rowCount > seriesMaxPointsCount)
                rowCount = seriesMaxPointsCount;
            for (int columnIndex = 1; columnIndex < columnCount; columnIndex++) {
                String seriesName = Strings.stringValue(displayResultSet.getColumns()[columnIndex].getHeaderValue());
                startSeries(seriesName);
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex ++)
                    addValueToCurrentSeries(displayResultSet.getValue(rowIndex, columnIndex), Strings.toString(displayResultSet.getValue(rowIndex, 0)));
            }
        } else {
            if (seriesMaxPointsCount > 0 && columnCount > seriesMaxPointsCount + 1)
                columnCount = seriesMaxPointsCount + 1;
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex ++) {
                String seriesName = Strings.toString(displayResultSet.getValue(rowIndex, 0));
                startSeries(seriesName);
                for (int columnIndex = 1; columnIndex < columnCount; columnIndex++)
                    addValueToCurrentSeries(displayResultSet.getValue(rowIndex, columnIndex), Strings.stringValue(displayResultSet.getColumns()[columnIndex].getHeaderValue()));
            }
        }
        applyChartData();
    }

    protected abstract void createChartData();

    protected abstract void startSeries(String name);

    protected abstract void addValueToCurrentSeries(Object value, String valueName);

    protected abstract void applyChartData();

}
