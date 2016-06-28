package naga.core.spi.toolkit.gwt.charts;

import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.googlecode.gwt.charts.client.*;
import naga.core.spi.platform.Platform;
import naga.core.spi.toolkit.charts.Chart;
import naga.core.spi.toolkit.gwt.node.GwtSelectableDisplayResultSetNode;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.type.PrimType;
import naga.core.type.Type;
import naga.core.type.Types;
import naga.core.ui.displayresultset.DisplayColumn;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.util.Booleans;
import naga.core.util.Numbers;
import naga.core.util.Strings;

/**
 * @author Bruno Salmon
 */
public abstract class GwtChart extends GwtSelectableDisplayResultSetNode<SimpleLayoutPanel> implements Chart<SimpleLayoutPanel> {

    private ChartWidget chartWidget;
    private DisplayResultSet readyDisplayResultSet;

    public GwtChart() {
        super(new SimpleLayoutPanel());
        new ChartLoader(ChartPackage.CORECHART).loadApi(() -> {
            chartWidget = createChartWidget();
            node.setWidget(chartWidget);
            if (readyDisplayResultSet != null)
                syncVisualDisplayResult(readyDisplayResultSet);
            readyDisplayResultSet = null;
            node.onResize();
            if (!node.isAttached())
                node.addAttachHandler(event -> Platform.scheduleDeferred(node::onResize));
        });
    }

    protected abstract ChartWidget createChartWidget();

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {

    }

    @Override
    protected void syncVisualDisplayResult(DisplayResultSet displayResultSet) {
        if (chartWidget == null) {
            readyDisplayResultSet = displayResultSet;
            return;
        }
        DataTable dataTable = DataTable.create();
        dataTable.addRows(displayResultSet.getRowCount());
        // First column = X, other columns = series with Y value associated with X
        for (int columnIndex = 0; columnIndex < displayResultSet.getColumnCount(); columnIndex++) {
            DisplayColumn displayColumn = displayResultSet.getColumns()[columnIndex];
            Type type = displayColumn.getType();
            PrimType primType = Types.getPrimType(type);
            DataColumn dataColumn = DataColumn.create(getColumnType(primType));
            dataColumn.setLabel(Strings.toString(displayColumn.getHeaderValue()));
            dataTable.addColumn(dataColumn);
            for (int rowIndex = 0; rowIndex < displayResultSet.getRowCount(); rowIndex ++) {
                Object value = displayResultSet.getValue(rowIndex, columnIndex);
                if (primType.isBoolean())
                    dataTable.setValue(rowIndex, columnIndex, Booleans.booleanValue(value));
                else if (primType.isString())
                    dataTable.setValue(rowIndex, columnIndex, Strings.stringValue(value));
                if (primType.isNumber())
                    dataTable.setValue(rowIndex, columnIndex, Numbers.doubleValue(value));
            }
        }
        chartWidget.draw(dataTable);
    }

    private static ColumnType getColumnType(PrimType primType) {
        if (primType.isBoolean())
            return ColumnType.BOOLEAN;
        if (primType.isString())
            return ColumnType.STRING;
        if (primType.isNumber())
            return ColumnType.NUMBER;
        return ColumnType.DATE;
    }
}
