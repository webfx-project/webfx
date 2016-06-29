package naga.core.spi.toolkit.gwt.charts;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.googlecode.gwt.charts.client.*;
import com.googlecode.gwt.charts.client.corechart.CoreChartWidget;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;
import naga.core.spi.platform.Platform;
import naga.core.spi.toolkit.charts.Chart;
import naga.core.spi.toolkit.gwt.node.GwtSelectableDisplayResultSetNode;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.type.PrimType;
import naga.core.type.Type;
import naga.core.type.Types;
import naga.core.ui.displayresultset.DisplayColumn;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.ui.displayselection.DisplaySelection;
import naga.core.util.Booleans;
import naga.core.util.Numbers;
import naga.core.util.Strings;

/**
 * @author Bruno Salmon
 */
public abstract class GwtChart extends GwtSelectableDisplayResultSetNode<SimpleLayoutPanel> implements Chart<SimpleLayoutPanel> {

    private CoreChartWidget<?> chartWidget;
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

    protected abstract CoreChartWidget createChartWidget();

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {
    }

    private boolean syncHandlerInstalled;
    private boolean syncingDisplaySelection;

    private void syncToolkitDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            JsArray<Selection> selectionArray = chartWidget.getSelection();
            int length = selectionArray == null ? 0 : selectionArray.length();
            DisplaySelection.Builder selectionBuilder = DisplaySelection.createBuilder(length);
            for (int i = 0; i < length; i++)
                selectionBuilder.addSelectedRow(selectionArray.get(i).getRow());
            setDisplaySelection(selectionBuilder.build());
            syncingDisplaySelection = false;
        }
    }

    private void syncVisualDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            DisplaySelection displaySelection = getDisplaySelection();
            if (displaySelection == null) {
                // chartWidget.setSelection(); // don't know how to clear selection (this code produces a JS error)
            } else {
                Selection[] selectionArray = new Selection[displaySelection.getUnitsCount()];
                int i = 0;
                for (DisplaySelection.Unit unit : displaySelection.getUnits())
                    selectionArray[i++] = Selection.create(unit.getRow(), unit.getColumn());
                chartWidget.setSelection(selectionArray);
            }
            syncingDisplaySelection = false;
        }
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
        int columnCount = displayResultSet.getColumnCount();
        int rowCount = displayResultSet.getRowCount();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            DisplayColumn displayColumn = displayResultSet.getColumns()[columnIndex];
            Type type = displayColumn.getType();
            PrimType primType = Types.getPrimType(type);
            DataColumn dataColumn = DataColumn.create(getColumnType(primType));
            dataColumn.setLabel(Strings.toString(displayColumn.getHeaderValue()));
            dataTable.addColumn(dataColumn);
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex ++) {
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
        // The above code reset the selection so selection handlers are installed here when first data arrives (to consider possible initial selection made by logic)
        if (!syncHandlerInstalled && rowCount > 0) {
            syncVisualDisplaySelection();
            displaySelectionProperty().addListener((observable, oldValue, newValue) -> syncVisualDisplaySelection());
            chartWidget.addSelectHandler(new SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    syncToolkitDisplaySelection();
                }
            });
            syncHandlerInstalled = true;
        }
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
