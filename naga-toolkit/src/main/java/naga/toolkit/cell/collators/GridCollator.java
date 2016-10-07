package naga.toolkit.cell.collators;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.adapters.grid.GridFiller;
import naga.toolkit.cell.renderers.ArrayRenderer;
import naga.toolkit.cell.renderers.ValueRenderer;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.SelectableDisplayResultSetNode;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class GridCollator implements SelectableDisplayResultSetNode {

    private final NodeCollator columnCollator;
    private final NodeCollator rowCollator;
    private final VPage container;

    public GridCollator(String columnCollator, String rowCollator) {
        this(NodeCollatorRegistry.getCollator(columnCollator), NodeCollatorRegistry.getCollator(rowCollator));
    }

    public GridCollator(NodeCollator columnCollator, NodeCollator rowCollator) {
        this.columnCollator = columnCollator;
        this.rowCollator = rowCollator;
        this.container = Toolkit.get().createVPage();
        syncVisualSelectionMode(getSelectionMode());
        selectionModeProperty.addListener((observable, oldValue, newValue) -> syncVisualSelectionMode(newValue));
        displayResultProperty.addListener((observable, oldValue, newValue) -> syncVisualDisplayResult(newValue));
    }

    @Override
    public Object unwrapToNativeNode() {
        return container.unwrapToNativeNode();
    }

    @Override
    public void requestFocus() {
    }

    private final Property<DisplayResultSet> displayResultProperty = new SimpleObjectProperty<>();
    @Override
    public Property<DisplayResultSet> displayResultSetProperty() {
        return displayResultProperty;
    }

    private final Property<SelectionMode> selectionModeProperty = new SimpleObjectProperty<>(SelectionMode.DISABLED);
    @Override
    public Property<SelectionMode> selectionModeProperty() {
        return selectionModeProperty;
    }

    private final Property<DisplaySelection> displaySelectionProperty = new SimpleObjectProperty<>();
    @Override
    public Property<DisplaySelection> displaySelectionProperty() {
        return displaySelectionProperty;
    }

    protected void syncVisualSelectionMode(SelectionMode selectionMode) {
    }

    protected void syncVisualDisplayResult(DisplayResultSet rs) {
        Toolkit.get().scheduler().runInUiThread(() -> gridFiller.fillGrid(rs));
    }

    @SuppressWarnings("unchecked")
    private GridFiller<Object> gridFiller = new GridFiller(null) {

        private ValueRenderer[] renderers;
        private int[] rsColumnIndexes;

        @Override
        public void fillGrid(DisplayResultSet rs) {
            int columnCount = rs.getColumnCount();
            renderers = new ValueRenderer[columnCount];
            rsColumnIndexes = new int[columnCount];
            super.fillGrid(rs);
            int rowCount = rs.getRowCount();
            GuiNode[] rowNodes = new GuiNode[rowCount];
            Object[] columnValues = new Object[gridColumnCount];
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                for (int gridColumnIndex = 0; gridColumnIndex < gridColumnCount; gridColumnIndex++)
                    columnValues[gridColumnIndex] = rs.getValue(rowIndex, rsColumnIndexes[gridColumnIndex]);
                rowNodes[rowIndex] = ArrayRenderer.renderCellValue(columnValues, renderers, columnCollator);
            }
            GuiNode finalNode = rowCollator.collateNodes(rowNodes);
            container.setCenter(finalNode);
        }

        @Override
        protected void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn) {
            renderers[gridColumnIndex] = displayColumn.getValueRenderer();
            rsColumnIndexes[gridColumnIndex] = rsColumnIndex;
        }
    };
}
