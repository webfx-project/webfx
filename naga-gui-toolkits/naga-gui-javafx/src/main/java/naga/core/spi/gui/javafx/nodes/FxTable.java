package naga.core.spi.gui.javafx.nodes;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.nodes.Table;
import naga.core.util.Strings;
import naga.core.util.collection.IdentityList;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Bruno Salmon
 */
public class FxTable extends FxDisplayNode<TableView<Integer>> implements Table<TableView<Integer>> {

    public FxTable() {
        this(new TableView<>());
    }

    public FxTable(TableView<Integer> tableView) {
        super(tableView);
        node.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @Override
    protected void onNextDisplayResult(DisplayResult displayResult) {
        updateColumns(displayResult);
        node.getItems().setAll(new IdentityList(displayResult.getRowCount()));
        if (displayResult.getRowCount() > 0) { // Workaround for the JavaFx wrong resized columns problem when vertical scrollbar appears
            node.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
            GuiToolkit.get().scheduler().scheduleDelay(100, () -> node.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY));
        }
    }

    private void updateColumns(DisplayResult displayResult) {
        ObservableList<TableColumn<Integer, ?>> currentColumns = node.getColumns();
        // Clearing the columns to completely rebuild them when table was empty (as the columns widths were not considering the content)
        if (node.getItems().isEmpty() && displayResult.getRowCount() > 0)
            currentColumns.clear();
        List<TableColumn<Integer, ?>> newColumns = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < displayResult.getColumnCount(); columnIndex++) {
            TableColumn<Integer, ?> tableColumn = columnIndex < currentColumns.size() ? currentColumns.get(columnIndex) : new TableColumn<>();
            setUpTableColumn(tableColumn, columnIndex, displayResult);
            newColumns.add(tableColumn);
        }
        node.getColumns().setAll(newColumns);
    }

    private TableColumn<Integer, ?> setUpTableColumn(TableColumn<Integer, ?> tableColumn, int columnIndex, DisplayResult displayResult) {
        tableColumn.setText(Strings.toString(displayResult.getHeaderValues()[columnIndex]));
        tableColumn.setGraphic(null);
        //tableColumn.setCellFactory(ignored -> new FxTableCell(column));
        tableColumn.setCellValueFactory(cdf -> (ObservableValue) displayResult.getValue(cdf.getValue(), columnIndex));
        return tableColumn;
    }

}
