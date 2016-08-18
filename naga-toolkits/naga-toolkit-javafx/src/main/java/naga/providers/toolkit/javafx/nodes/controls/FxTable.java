package naga.providers.toolkit.javafx.nodes.controls;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import naga.commons.util.collection.IdentityList;
import naga.providers.toolkit.javafx.FxImageStore;
import naga.providers.toolkit.javafx.JavaFxToolkit;
import naga.providers.toolkit.javafx.nodes.FxSelectableDisplayResultSetNode;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.display.Label;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.cell.*;
import naga.toolkit.cell.renderers.CellRenderer;
import naga.toolkit.spi.nodes.controls.Table;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Bruno Salmon
 */
public class FxTable extends FxSelectableDisplayResultSetNode<TableView<Integer>> implements Table<TableView<Integer>> {

    public FxTable() {
        this(createTableView());
    }

    public FxTable(TableView<Integer> tableView) {
        super(tableView);
        node.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        displaySelectionProperty().addListener((observable, oldValue, newValue) -> syncVisualDisplaySelection());
        node.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<Integer>) c -> syncToolkitDisplaySelection());
        node.setRowFactory(createRowFactory());
    }

    private static TableView<Integer> createTableView() {
        return new TableView<>();
    }

    private boolean syncingDisplaySelection;

    private void syncToolkitDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            setDisplaySelection(DisplaySelection.createRowsSelection(node.getSelectionModel().getSelectedIndices()));
            syncingDisplaySelection = false;
        }
    }

    private void syncVisualDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            node.getSelectionModel().clearSelection();
            DisplaySelection displaySelection = getDisplaySelection();
            if (displaySelection != null)
                displaySelection.forEachRow(node.getSelectionModel()::select);
            syncingDisplaySelection = false;
        }
    }

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {
        javafx.scene.control.SelectionMode fxSelectionMode = null;
        switch (selectionMode) {
            case DISABLED:
            case SINGLE:   fxSelectionMode = javafx.scene.control.SelectionMode.SINGLE; break;
            case MULTIPLE: fxSelectionMode = javafx.scene.control.SelectionMode.MULTIPLE; break;
        }
        node.getSelectionModel().setSelectionMode(fxSelectionMode);
    }

    @Override
    protected void syncVisualDisplayResult(DisplayResultSet rs) {
        rs = JavaFxToolkit.transformDisplayResultSetValuesToProperties(rs);
        gridFiller.fillGrid(rs);
        node.getItems().setAll(new IdentityList(rs.getRowCount()));
        if (rs.getRowCount() > 0) { // Workaround for the JavaFx wrong resize columns problem when vertical scroll bar appears
            node.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
            Toolkit.get().scheduler().scheduleDelay(100, () -> node.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY));
        }
    }


    private final GridFiller gridFiller = new GridFiller<TableCell>((ImageTextGridAdapter<TableCell, Node>) (cell, image, text, displayColumn) -> {
        cell.setGraphic(Toolkit.unwrapToNativeNode(image));
        cell.setText(text);
    }) {
        private List<TableColumn<Integer, ?>> currentColumns, newColumns;

        @Override
        public void fillGrid(DisplayResultSet rs) {
            currentColumns = node.getColumns();
            newColumns = new ArrayList<>();
            // Clearing the columns to completely rebuild them when table was empty (as the columns widths were not considering the content)
            if (node.getItems().isEmpty() && rs.getRowCount() > 0)
                currentColumns.clear();
            super.fillGrid(rs);
            node.getColumns().setAll(newColumns);
            currentColumns = newColumns = null;
        }

        @Override
        protected void setUpGridColumn(int gridColumnIndex, DisplayColumn displayColumn, CellRenderer cellRenderer, int rsColumnIndex, DisplayResultSet rs) {
            TableColumn<Integer, ?> gridColumn = gridColumnIndex < currentColumns.size() ? currentColumns.get(gridColumnIndex) : new TableColumn<>();
            newColumns.add(gridColumn);
            Label label = displayColumn.getLabel();
            gridColumn.setText(label.getText());
            gridColumn.setGraphic(FxImageStore.createLabelIconImageView(label));
            Double prefWidth = displayColumn.getPrefWidth();
            if (prefWidth != null) {
                // Applying same prefWidth transformation as the PolymerTable (trying to
                if (label.getText() != null)
                    prefWidth = prefWidth * 2.75; // factor compared to JavaFx style (temporary hardcoded)
                prefWidth = prefWidth + 10; // because of the 5px left and right padding
                gridColumn.setPrefWidth(prefWidth);
                gridColumn.setMinWidth(prefWidth);
                gridColumn.setMaxWidth(prefWidth);
            }
            gridColumn.setCellValueFactory(cdf -> (ObservableValue) rs.getValue(cdf.getValue(), rsColumnIndex));
            gridColumn.setCellFactory(param -> new TableCell() {
                { setAlignment("right".equals(displayColumn.getTextAlign()) ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT); }
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    fillCell(this, item, cellRenderer, displayColumn);
                }
            });
        }

        @Override
        public Object getRowStyleResultSetValue(int rowIndex) {
            Object value = super.getRowStyleResultSetValue(rowIndex);
            if (value instanceof ObservableValue)
                value = ((ObservableValue) value).getValue();
            return value;
        }
    };

    private Callback<TableView<Integer>, TableRow<Integer>> createRowFactory() {
        return tableView -> {
            TableRow<Integer> row = new TableRow<>();
            RowStyleUpdater rowStyleUpdater = new RowStyleUpdater(new RowAdapter() {
                @Override public int getRowIndex() { return row.getIndex(); }
                @Override public void addStyleClass(String styleClass) { row.getStyleClass().add(styleClass); }
                @Override public void removeStyleClass(String styleClass) { row.getStyleClass().remove(styleClass); }
            }, gridFiller);
            row.getProperties().put("nodeStyleUpdater", rowStyleUpdater); // keeping strong reference to avoid garbage collection
            row.itemProperty().addListener((observable, oldRowIndex, newRowIndex) -> rowStyleUpdater.update());
            return row;
        };
    }
}
