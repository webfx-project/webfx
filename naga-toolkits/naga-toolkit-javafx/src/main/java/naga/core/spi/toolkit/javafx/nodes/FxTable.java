package naga.core.spi.toolkit.javafx.nodes;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.ui.displayselection.DisplaySelection;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.spi.toolkit.nodes.Table;
import naga.core.util.Strings;
import naga.core.util.collection.IdentityList;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Bruno Salmon
 */
public class FxTable extends FxSelectableDisplayResultSetNode<TableView<Integer>> implements Table<TableView<Integer>> {

    public FxTable() {
        this(new TableView<>());
    }

    public FxTable(TableView<Integer> tableView) {
        super(tableView);
        node.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        displaySelectionProperty().addListener((observable, oldValue, newValue) -> syncVisualDisplaySelection());
        node.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<Integer>) c -> syncToolkitDisplaySelection());
    }

    private boolean syncingDisplaySelection;

    private void syncToolkitDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            ObservableList<Integer> selectedIndices = node.getSelectionModel().getSelectedIndices();
            int length = selectedIndices.size();
            int[] selectedRows = new int[length];
            for (int i = 0; i < length; i++)
                selectedRows[i] = selectedIndices.get(i);
            setDisplaySelection(new DisplaySelection(selectedRows));
            syncingDisplaySelection = false;
        }
    }

    private void syncVisualDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            DisplaySelection displaySelection = getDisplaySelection();
            node.getSelectionModel().selectIndices(displaySelection.getSelectedRow(), displaySelection.getSelectedRows());
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
    protected void syncVisualDisplayResult(DisplayResultSet displayResultSet) {
        syncVisualColumns(displayResultSet);
        node.getItems().setAll(new IdentityList(displayResultSet.getRowCount()));
        if (displayResultSet.getRowCount() > 0) { // Workaround for the JavaFx wrong resize columns problem when vertical scroll bar appears
            node.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
            Toolkit.get().scheduler().scheduleDelay(100, () -> node.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY));
        }
    }

    private void syncVisualColumns(DisplayResultSet displayResultSet) {
        ObservableList<TableColumn<Integer, ?>> currentColumns = node.getColumns();
        // Clearing the columns to completely rebuild them when table was empty (as the columns widths were not considering the content)
        if (node.getItems().isEmpty() && displayResultSet.getRowCount() > 0)
            currentColumns.clear();
        List<TableColumn<Integer, ?>> newColumns = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < displayResultSet.getColumnCount(); columnIndex++) {
            TableColumn<Integer, ?> tableColumn = columnIndex < currentColumns.size() ? currentColumns.get(columnIndex) : new TableColumn<>();
            setUpVisualColumn(tableColumn, columnIndex, displayResultSet);
            newColumns.add(tableColumn);
        }
        node.getColumns().setAll(newColumns);
    }

    private TableColumn<Integer, ?> setUpVisualColumn(TableColumn<Integer, ?> tableColumn, int columnIndex, DisplayResultSet displayResultSet) {
        tableColumn.setText(Strings.toString(displayResultSet.getColumns()[columnIndex].getHeaderValue()));
        tableColumn.setGraphic(null);
        //tableColumn.setCellFactory(ignored -> new FxTableCell(column));
        tableColumn.setCellValueFactory(cdf -> (ObservableValue) displayResultSet.getValue(cdf.getValue(), columnIndex));
        return tableColumn;
    }

}
