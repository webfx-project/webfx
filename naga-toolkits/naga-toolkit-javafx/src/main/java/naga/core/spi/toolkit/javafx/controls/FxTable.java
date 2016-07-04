package naga.core.spi.toolkit.javafx.controls;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.controls.Table;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.spi.toolkit.javafx.JavaFxToolkit;
import naga.core.spi.toolkit.javafx.node.FxSelectableDisplayResultSetNode;
import naga.core.ui.displayresultset.DisplayColumn;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.ui.displayselection.DisplaySelection;
import naga.core.util.Objects;
import naga.core.util.Strings;
import naga.core.util.collection.IdentityList;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Bruno Salmon
 */
public class FxTable extends FxSelectableDisplayResultSetNode<TableView<Integer>> implements Table<TableView<Integer>> {

    private int rowStyleColumnIndex;

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
        syncVisualColumns(rs);
        node.getItems().setAll(new IdentityList(rs.getRowCount()));
        if (rs.getRowCount() > 0) { // Workaround for the JavaFx wrong resize columns problem when vertical scroll bar appears
            node.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
            Toolkit.get().scheduler().scheduleDelay(100, () -> node.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY));
        }
    }

    private void syncVisualColumns(DisplayResultSet rs) {
        rowStyleColumnIndex = -1;
        int columnCount = rs.getColumnCount();
        int rowCount = rs.getRowCount();
        DisplayColumn[] columns = rs.getColumns();
        ObservableList<TableColumn<Integer, ?>> currentColumns = node.getColumns();
        // Clearing the columns to completely rebuild them when table was empty (as the columns widths were not considering the content)
        if (node.getItems().isEmpty() && rowCount > 0)
            currentColumns.clear();
        List<TableColumn<Integer, ?>> newColumns = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            DisplayColumn displayColumn = columns[columnIndex];
            String role = displayColumn.getRole();
            if ("style".equals(role))
                rowStyleColumnIndex = columnIndex;
            else if (role == null) {
                TableColumn<Integer, ?> tableColumn = columnIndex < currentColumns.size() ? currentColumns.get(columnIndex) : new TableColumn<>();
                setUpVisualColumn(tableColumn, columnIndex, rs);
                newColumns.add(tableColumn);
            }
        }
        node.getColumns().setAll(newColumns);
    }

    private TableColumn<Integer, ?> setUpVisualColumn(TableColumn<Integer, ?> tableColumn, int columnIndex, DisplayResultSet rs) {
        tableColumn.setText(rs.getColumns()[columnIndex].getName());
        tableColumn.setGraphic(null);
        //tableColumn.setCellFactory(ignored -> new FxTableCell(column));
        tableColumn.setCellValueFactory(cdf -> (ObservableValue) rs.getValue(cdf.getValue(), columnIndex));
        return tableColumn;
    }

    Callback<TableView<Integer>, TableRow<Integer>> createRowFactory() {
        return tableView -> {
            final TableRow<Integer> row = new TableRow<>();
            NodeStyleUpdater rowStyleUpdater = new NodeStyleUpdater(row);
            row.itemProperty().addListener((observable, oldRowIndex, newRowIndex) -> {
                if (newRowIndex != null && rowStyleColumnIndex >= 0) {
                    Object value = getDisplayResultSet().getValue(newRowIndex, rowStyleColumnIndex);
                    if (value instanceof ObservableValue)
                        value = ((ObservableValue) value).getValue();
                    if (value instanceof Object[])
                        rowStyleUpdater.update((Object[]) value);
                }
            });
            return row;
        };
    }

    static class NodeStyleUpdater {
        final Node node;
        Object[] styles;

        NodeStyleUpdater(Node node) {
            this.node = node;
            node.getProperties().put("nodeStyleUpdater", this); // keeping strong reference to avoid garbage collection
        }

        void update(Object[] newStyles) {
            ObservableList<String> nodeStyleClass = node.getStyleClass();
            for (int i = 0; i < newStyles.length; i++) {
                String newStyleClass = Strings.toString(newStyles[i]);
                String oldStyleClass = styles == null ? null : Strings.toString(styles[i]);
                if (!Objects.areEquals(newStyleClass, oldStyleClass)) {
                    if (oldStyleClass != null)
                        nodeStyleClass.remove(oldStyleClass);
                    if (newStyleClass != null)
                        nodeStyleClass.add(newStyleClass);
                }
            }
            styles = newStyles;
        }
    }
}
