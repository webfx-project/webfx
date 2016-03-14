package naga.core.spi.gui.javafx.node;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.node.Table;
import naga.core.util.Strings;
import naga.core.util.collection.IdentityList;
import rx.Subscriber;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Bruno Salmon
 */
public class FxTable extends FxNode<TableView<Integer>> implements Table<TableView<Integer>> {

    public FxTable() {
        this(new TableView<>());
    }

    public FxTable(TableView<Integer> tableView) {
        super(tableView);
    }

    @Override
    public Subscriber<DisplayResult> getDisplayResultSubscriber() {
        return new Subscriber<DisplayResult>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onNext(DisplayResult displayResult) {
                updateColumns(displayResult);
                node.getItems().setAll(new IdentityList(displayResult.getRowCount()));
            }
        };
    }

    private void updateColumns(DisplayResult displayResult) {
        List<TableColumn> currentColumns = ((TableView) node).getColumns();
        List<TableColumn<Integer, Object>> newColumns = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < displayResult.getColumnCount(); columnIndex++) {
            TableColumn<Integer, Object> tableColumn = columnIndex < currentColumns.size() ? currentColumns.get(columnIndex) : new TableColumn<>();
            setUpTableColumn(tableColumn, columnIndex, displayResult);
            newColumns.add(tableColumn);
        }
        node.getColumns().setAll(newColumns);
    }

    private TableColumn<Integer, ?> setUpTableColumn(TableColumn<Integer, Object> tableColumn, int columnIndex, DisplayResult displayResult) {
        tableColumn.setText(Strings.toString(displayResult.getHeaderValues()[columnIndex]));
        tableColumn.setGraphic(null);
        //tableColumn.setCellFactory(ignored -> new FxTableCell(column));
        tableColumn.setCellValueFactory(cdf -> (ObservableValue<Object>) displayResult.getValue(cdf.getValue(), columnIndex));
        return tableColumn;
    }

}
