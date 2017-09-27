package naga.fx.spi.javafx.peer;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import naga.commons.util.collection.IdentityList;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.fx.util.ImageStore;
import naga.fxdata.cell.rowstyle.RowAdapter;
import naga.fxdata.cell.rowstyle.RowStyleUpdater;
import naga.fxdata.control.DataGrid;
import naga.fxdata.displaydata.*;
import naga.fxdata.displaydata.SelectionMode;
import naga.fxdata.spi.peer.base.DataGridPeerBase;
import naga.fxdata.spi.peer.base.DataGridPeerImageTextMixin;
import naga.fxdata.spi.peer.base.DataGridPeerMixin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class FxDataGridPeer
        <N extends DataGrid, NB extends DataGridPeerBase<TableCell, N, NB, NM>, NM extends DataGridPeerMixin<TableCell, N, NB, NM>>

        extends FxRegionPeer<TableView<Integer>, N, NB, NM>
        implements DataGridPeerImageTextMixin<TableCell, N, NB, NM>, FxLayoutMeasurable {

    public FxDataGridPeer() {
        super((NB) new DataGridPeerBase());
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

    @Override
    public void updateHeaderVisible(boolean headerVisible) {
        if (headerVisible)
            getFxNode().getStyleClass().remove("noheader");
        else
            getFxNode().getStyleClass().add("noheader");
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
            getNodePeerBase().fillGrid(rs);
            tableView.getSelectionModel().clearSelection(); // To avoid internal java 8 API call on Android
            tableView.getColumns().setAll(newColumns);
            currentColumns = newColumns = null;
            tableView.getSelectionModel().clearSelection(); // Clearing selection otherwise an undesired selection event is triggered on new items
            tableView.getItems().setAll(new IdentityList(rs.getRowCount()));
            if (rs.getRowCount() > 0) { // Workaround for the JavaFx wrong resize columns problem when vertical scroll bar appears
                tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
                Toolkit.get().scheduler().scheduleDelay(100, () -> tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY));
            }
            tableView.prefHeightProperty().bind(getNode().prefHeightProperty());
            tableView.minHeightProperty().bind(getNode().minHeightProperty());
            tableView.maxHeightProperty().bind(getNode().maxHeightProperty());
            // Workaround to make the table height fit with its content
            if (getNode().getMaxHeight() == Region.USE_PREF_SIZE) { // ugly trick: we recognize it is necessary only when LayoutUtil.setMinMaxHeightToPref() has been called so max size is bound to min size
                fitHeightToContent(tableView, getNode());
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
        naga.fxdata.displaydata.Label label = displayColumn.getLabel();
        gridColumn.setText(label.getText());
        gridColumn.setGraphic(ImageStore.createLabelIconImageView(label));
        Double prefWidth = displayColumn.getStyle().getPrefWidth();
        if (prefWidth != null) {
            if (prefWidth > 24)
                prefWidth *= 1.3;
            prefWidth = prefWidth + 10; // because of the 5px left and right padding
            gridColumn.setPrefWidth(prefWidth);
            gridColumn.setMinWidth(prefWidth);
            gridColumn.setMaxWidth(prefWidth);
        }
        String textAlign = displayColumn.getStyle().getTextAlign();
        Pos alignment = "right".equals(textAlign) ? Pos.CENTER_RIGHT : "center".equals(textAlign) ? Pos.CENTER : Pos.CENTER_LEFT;
        gridColumn.setCellValueFactory(cdf -> (ObservableValue) getNodePeerBase().getRs().getValue(cdf.getValue(), rsColumnIndex));
        gridColumn.setCellFactory(param -> new TableCell() {
            { setAlignment(alignment); }
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty)
                    getNodePeerBase().fillCell(this, item, displayColumn);
            }
        });
    }

    @Override
    public void setCellImageAndTextContent(TableCell cell, Node image, String text, DisplayColumn displayColumn) {
        cell.setGraphic(image);
        cell.setText(text);
    }

    private Callback<TableView<Integer>, TableRow<Integer>> createRowFactory() {
        return tableView -> {
            TableRow<Integer> row = new TableRow<>();
            RowStyleUpdater rowStyleUpdater = new RowStyleUpdater(new RowAdapter() {
                @Override public int getRowIndex() { return row.getIndex(); }
                @Override public void addStyleClass(String styleClass) { row.getStyleClass().add(styleClass); }
                @Override public void removeStyleClass(String styleClass) { row.getStyleClass().remove(styleClass); }
                @Override public void applyBackground(Paint fill) {
                    if (fill == null)
                        row.backgroundProperty().unbind();
                    else
                        row.backgroundProperty().bind(new SimpleObjectProperty<>(new Background(new BackgroundFill(fill, null, null))));
                }
            }, this::getRowStyleClasses, this::getRowBackground);
            row.getProperties().put("nodeStyleUpdater", rowStyleUpdater); // keeping strong reference to avoid garbage collection
            row.itemProperty().addListener((observable, oldRowIndex, newRowIndex) -> rowStyleUpdater.update());
            return row;
        };
    }

    private Object[] getRowStyleClasses(int rowIndex) {
        NB base = getNodePeerBase();
        Object value = base.getRowStyleResultSetValue(rowIndex);
        if (value instanceof ObservableValue)
            value = ((ObservableValue) value).getValue();
        return base.getRowStyleClasses(value);
    }

    private Paint getRowBackground(int rowIndex) {
        NB base = getNodePeerBase();
        Object value = base.getRowBackgroundResultSetValue(rowIndex);
        if (value instanceof ObservableValue)
            value = ((ObservableValue) value).getValue();
        return base.getRowBackground(value);
    }

    private static void fitHeightToContent(final Control control, DataGrid dataGrid) {
        // Quick ugly hacked code to make the table height fit with the content
        Skin<?> skin = control.getSkin();
        if (skin == null)
            control.skinProperty().addListener(new ChangeListener<Skin<?>>() {
                @Override
                public void changed(ObservableValue<? extends Skin<?>> observableValue, Skin<?> skin, Skin<?> skin2) {
                    control.skinProperty().removeListener(this);
                    fitHeightToContent(control, dataGrid);
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
                Properties.setIfNotBound(dataGrid.prefHeightProperty(), h);
            }
        }
    }
}