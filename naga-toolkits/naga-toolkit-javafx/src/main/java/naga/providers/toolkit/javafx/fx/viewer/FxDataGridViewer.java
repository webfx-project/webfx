package naga.providers.toolkit.javafx.fx.viewer;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Callback;
import naga.commons.util.collection.IdentityList;
import naga.providers.toolkit.javafx.util.FxImageStore;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplayResultSetBuilder;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.fx.ext.cell.rowstyle.RowAdapter;
import naga.toolkit.fx.ext.cell.rowstyle.RowStyleUpdater;
import naga.toolkit.fx.ext.control.DataGrid;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.viewer.base.DataGridViewerBase;
import naga.toolkit.fx.spi.viewer.base.DataGridViewerImageTextMixin;
import naga.toolkit.fx.spi.viewer.base.DataGridViewerMixin;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.Toolkit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class FxDataGridViewer
        <N extends DataGrid, NV extends DataGridViewerBase<TableCell, N, NV, NM>, NM extends DataGridViewerMixin<TableCell, N, NV, NM>>

        extends FxRegionViewer<TableView<Integer>, N, NV, NM>
        implements DataGridViewerImageTextMixin<TableCell, N, NV, NM>, FxLayoutMeasurable {

    public FxDataGridViewer() {
        super((NV) new DataGridViewerBase());
    }

    @Override
    protected TableView<Integer> createFxNode() {
        TableView<Integer> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setRowFactory(createRowFactory());
        tableView.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<Integer>) c -> updateNodeDisplaySelection());
        return tableView;
    }

    @Override
    public void updateSelectionMode(SelectionMode mode) {
        javafx.scene.control.SelectionMode fxSelectionMode = null;
        switch (mode) {
            case DISABLED:
            case SINGLE:   fxSelectionMode = javafx.scene.control.SelectionMode.SINGLE; break;
            case MULTIPLE: fxSelectionMode = javafx.scene.control.SelectionMode.MULTIPLE; break;
        }
        getFxNode().getSelectionModel().setSelectionMode(fxSelectionMode);
    }

    private boolean syncingDisplaySelection;

    private void updateNodeDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            getNode().setDisplaySelection(DisplaySelection.createRowsSelection(getFxNode().getSelectionModel().getSelectedIndices()));
            syncingDisplaySelection = false;
        }
    }

    @Override
    public void updateDisplaySelection(DisplaySelection selection) {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            TableView.TableViewSelectionModel<Integer> selectionModel = getFxNode().getSelectionModel();
            selectionModel.clearSelection();
            if (selection != null)
                selection.forEachRow(selectionModel::select);
            syncingDisplaySelection = false;
        }
    }

    private List<TableColumn<Integer, ?>> currentColumns, newColumns;

    @Override
    public void updateResultSet(DisplayResultSet rs) {
        if (rs == null)
            return;
        rs = transformDisplayResultSetValuesToProperties(rs);
        TableView<Integer> tableView = getFxNode();
        synchronized (this) {
            currentColumns = tableView.getColumns();
            newColumns = new ArrayList<>();
            // Clearing the columns to completely rebuild them when table was empty (as the columns widths were not considering the content)
            if (tableView.getItems().isEmpty() && rs.getRowCount() > 0)
                currentColumns.clear();
            getNodeViewerBase().fillGrid(rs);
            tableView.getColumns().setAll(newColumns);
            currentColumns = newColumns = null;
            tableView.getSelectionModel().clearSelection(); // Clearing selection otherwise an undesired selection event is triggered on new items
            tableView.getItems().setAll(new IdentityList(rs.getRowCount()));
            if (rs.getRowCount() > 0) { // Workaround for the JavaFx wrong resize columns problem when vertical scroll bar appears
                tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
                Toolkit.get().scheduler().scheduleDelay(100, () -> tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY));
            }
            // Workaround to make the table height fit with its content if it is not within a BorderPane
            if (!(getNode().getParent() instanceof naga.toolkit.fx.scene.layout.BorderPane)) {
                fitHeightToContent(tableView);
                addStylesheet("css/tableview-no-vertical-scrollbar.css");
                addStylesheet("css/tableview-no-horizontal-scrollbar.css");
            }
        }
    }

    private void addStylesheet(String css) {
        ObservableList<String> stylesheets = getFxNode().getStylesheets();
        if (!stylesheets.contains(css))
            stylesheets.add(css);
    }

    private static DisplayResultSet transformDisplayResultSetValuesToProperties(DisplayResultSet rs) {
        return DisplayResultSetBuilder.convertDisplayResultSet(rs, SimpleObjectProperty::new);
    }

    @Override
    public void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn) {
        TableColumn<Integer, ?> gridColumn = gridColumnIndex < currentColumns.size() ? currentColumns.get(gridColumnIndex) : new TableColumn<>();
        newColumns.add(gridColumn);
        naga.toolkit.display.Label label = displayColumn.getLabel();
        gridColumn.setText(label.getText());
        gridColumn.setGraphic(FxImageStore.createLabelIconImageView(label));
        Double prefWidth = displayColumn.getStyle().getPrefWidth();
        if (prefWidth != null) {
            prefWidth = prefWidth + 10; // because of the 5px left and right padding
            gridColumn.setPrefWidth(prefWidth);
            gridColumn.setMinWidth(prefWidth);
            gridColumn.setMaxWidth(prefWidth);
        }
        String textAlign = displayColumn.getStyle().getTextAlign();
        Pos alignment = "right".equals(textAlign) ? Pos.CENTER_RIGHT : "center".equals(textAlign) ? Pos.CENTER : Pos.CENTER_LEFT;
        gridColumn.setCellValueFactory(cdf -> (ObservableValue) getNodeViewerBase().getRs().getValue(cdf.getValue(), rsColumnIndex));
        gridColumn.setCellFactory(param -> new TableCell() {
            { setAlignment(alignment); }
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                getNodeViewerBase().fillCell(this, item, displayColumn);
            }
        });
    }

    @Override
    public void setCellImageAndTextContent(TableCell cell, Node image, String text, DisplayColumn displayColumn) {
        cell.setGraphic(toFxNode(image, getNode().getScene()));
        cell.setText(text);
    }

    private Callback<TableView<Integer>, TableRow<Integer>> createRowFactory() {
        return tableView -> {
            TableRow<Integer> row = new TableRow<>();
            RowStyleUpdater rowStyleUpdater = new RowStyleUpdater(new RowAdapter() {
                @Override public int getRowIndex() { return row.getIndex(); }
                @Override public void addStyleClass(String styleClass) { row.getStyleClass().add(styleClass); }
                @Override public void removeStyleClass(String styleClass) { row.getStyleClass().remove(styleClass); }
            }, this::getRowStyleClasses);
            row.getProperties().put("nodeStyleUpdater", rowStyleUpdater); // keeping strong reference to avoid garbage collection
            row.itemProperty().addListener((observable, oldRowIndex, newRowIndex) -> rowStyleUpdater.update());
            return row;
        };
    }

    private Object[] getRowStyleClasses(int rowIndex) {
        NV base = getNodeViewerBase();
        Object value = base.getRowStyleResultSetValue(rowIndex);
        if (value instanceof ObservableValue)
            value = ((ObservableValue) value).getValue();
        return base.getRowStyleClasses(value);
    }

    private static void fitHeightToContent(final Control control) {
        // Quick ugly hacked code to make the table height fit with the content
        Skin<?> skin = control.getSkin();
        if (skin == null)
            control.skinProperty().addListener(new ChangeListener<Skin<?>>() {
                @Override
                public void changed(ObservableValue<? extends Skin<?>> observableValue, Skin<?> skin, Skin<?> skin2) {
                    control.skinProperty().removeListener(this);
                    fitHeightToContent(control);
                }
            });
        else {
            ObservableList<javafx.scene.Node> children = null;
            if (skin instanceof Parent) // happens in java 7
                children = ((Parent) skin).getChildrenUnmodifiable();
            else if (skin instanceof SkinBase) // happens in java 8
                children = ((SkinBase) skin).getChildren();
            if (children != null) {
                double h = 2; // border
                for (javafx.scene.Node node : new ArrayList<>(children)) {
                    double nodePrefHeight = 0;
                    if (node instanceof VirtualFlow) { // the problem with Virtual Flow is that it limits the computation to the first 10 rows
                        VirtualFlow flow = (VirtualFlow) node;
                        if (control instanceof TableView) {
                            int size = ((TableView) control).getItems().size();
                            try {
                                Method m = flow.getClass().getDeclaredMethod("getCellLength", int.class);
                                m.setAccessible(true);
                                for (int i = 0; i < size; i++)
                                    nodePrefHeight +=  (Double) m.invoke(flow, i);
                            } catch (Exception e) {
                            }
                        }
                    }
                    if (nodePrefHeight == 0)
                        nodePrefHeight = node.prefHeight(-1);
                    h += nodePrefHeight;
                }
                control.setMinHeight(h);
            }
        }
    }
}