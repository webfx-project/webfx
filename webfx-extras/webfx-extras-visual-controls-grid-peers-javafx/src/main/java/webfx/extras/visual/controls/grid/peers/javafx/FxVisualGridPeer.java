package webfx.extras.visual.controls.grid.peers.javafx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import webfx.extras.cell.rowstyle.RowAdapter;
import webfx.extras.cell.rowstyle.RowStyleUpdater;
import webfx.extras.imagestore.ImageStore;
import webfx.extras.label.Label;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualResultBuilder;
import webfx.extras.visual.VisualSelection;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.extras.visual.controls.grid.peers.base.VisualGridPeerBase;
import webfx.extras.visual.controls.grid.peers.base.VisualGridPeerImageTextMixin;
import webfx.extras.visual.controls.grid.peers.base.VisualGridPeerMixin;
import webfx.kit.mapper.peers.javafxgraphics.javafx.FxLayoutMeasurable;
import webfx.kit.mapper.peers.javafxgraphics.javafx.FxRegionPeer;
import webfx.kit.util.properties.Properties;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.util.collection.IdentityList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class FxVisualGridPeer
        <N extends VisualGrid, NB extends VisualGridPeerBase<TableCell, N, NB, NM>, NM extends VisualGridPeerMixin<TableCell, N, NB, NM>>

        extends FxRegionPeer<TableView<Integer>, N, NB, NM>
        implements VisualGridPeerImageTextMixin<TableCell, N, NB, NM>, FxLayoutMeasurable {

    public FxVisualGridPeer() {
        super((NB) new VisualGridPeerBase());
    }

    static final String DEFER_TO_PARENT_PREF_WIDTH = "deferToParentPrefWidth";

    @Override
    protected TableView<Integer> createFxNode() {
        TableView<Integer> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setRowFactory(createRowFactory());
        tableView.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<Integer>) c -> updateNodeVisualSelection());
        try { // Try catch block for java 9 where TableViewSkin is not accessible anymore
            tableView.setSkin(new TableViewSkin<>(tableView) {
                @Override
                protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
                    double pw = leftInset + rightInset;
                    for (TableColumn<Integer, ?> tc : tableView.getVisibleLeafColumns()) {
                        if (!tc.isResizable())
                            pw += tc.getWidth();
                        else {
                            int rows = getItemCount();
                            if (rows == 0) continue;

                            Callback/*<TableColumn<T, ?>, TableCell<T,?>>*/ cellFactory = tc.getCellFactory();
                            if (cellFactory == null) continue;

                            TableCell<Integer, ?> cell = (TableCell<Integer, ?>) cellFactory.call(tc);
                            if (cell == null) continue;

                            // set this property to tell the TableCell we want to know its actual
                            // preferred width, not the width of the associated TableColumnBase
                            cell.getProperties().put(DEFER_TO_PARENT_PREF_WIDTH, Boolean.TRUE);

                            // determine cell padding
                            double padding = 10;
                            Node n = cell.getSkin() == null ? null : cell.getSkin().getNode();
                            if (n instanceof Region) {
                                Region r = (Region) n;
                                padding = r.snappedLeftInset() + r.snappedRightInset();
                            }

                            //int rows = items.size(); //maxRows == -1 ? items.size() : Math.min(items.size(), maxRows);
                            double maxWidth = 0;
                            for (int row = 0; row < rows; row++) {
                                cell.updateTableColumn(tc);
                                cell.updateTableView(tableView);
                                cell.updateIndex(row);

                                if ((cell.getText() != null && !cell.getText().isEmpty()) || cell.getGraphic() != null) {
                                    getChildren().add(cell);
                                    cell.applyCss();
                                    double prefWidth = cell.prefWidth(-1);
                                    maxWidth = Math.max(maxWidth, prefWidth);
                                    getChildren().remove(cell);
                                    System.out.println("prefWidth = " + prefWidth + " for " + cell.getText() + ", font = " + cell.getFont());
                                }
                            }

                            // dispose of the cell to prevent it retaining listeners (see RT-31015)
                            cell.updateIndex(-1);

                            pw += maxWidth + padding;
                        }
                    }
                    return pw;
                }
            });
        } catch (Throwable e) { // Probably java 9
            System.out.println("FxDataGridPeer can't access TableViewSkin to override computePrefWidth() -- Java 9 issue");
        }
        return tableView;
    }

    @Override
    protected void onFxNodeCreated() {
        TableView<Integer> tableView = getFxNode();
        N node = getNode();
        tableView.prefHeightProperty().bind(node.prefHeightProperty());
        tableView.minHeightProperty().bind(node.minHeightProperty());
        tableView.maxHeightProperty().bind(node.maxHeightProperty());
    }

    @Override
    public void updateSelectionMode(webfx.extras.visual.SelectionMode mode) {
        javafx.scene.control.SelectionMode fxSelectionMode = null;
        switch (mode) {
            case DISABLED:
            case SINGLE:   fxSelectionMode = javafx.scene.control.SelectionMode.SINGLE; break;
            case MULTIPLE: fxSelectionMode = javafx.scene.control.SelectionMode.MULTIPLE; break;
        }
        getFxNode().getSelectionModel().setSelectionMode(fxSelectionMode);
    }

    private boolean syncingVisualSelection;

    private void updateNodeVisualSelection() {
        if (!syncingVisualSelection) {
            syncingVisualSelection = true;
            getNode().setVisualSelection(VisualSelection.createRowsSelection(getFxNode().getSelectionModel().getSelectedIndices()));
            syncingVisualSelection = false;
        }
    }

    @Override
    public void updateVisualSelection(VisualSelection selection) {
        if (!syncingVisualSelection) {
            syncingVisualSelection = true;
            TableView.TableViewSelectionModel<Integer> selectionModel = getFxNode().getSelectionModel();
            selectionModel.clearSelection();
            if (selection != null)
                selection.forEachRow(selectionModel::select);
            syncingVisualSelection = false;
        }
    }

    @Override
    public void updateHeaderVisible(boolean headerVisible) {
        TableView<Integer> tableView = getFxNode();
        if (headerVisible)
            tableView.getStyleClass().remove("noHeader");
        else
            tableView.getStyleClass().add("noHeader");
    }

    private final static EventHandler<Event> eventConsumer = Event::consume;
    @Override
    public void updateFullHeight(boolean fullHeight) {
        TableView<Integer> tableView = getFxNode();
        if (fullHeight) {
            tableView.getStyleClass().add("fullHeight");
            tableView.addEventFilter(ScrollEvent.ANY, eventConsumer);
        } else {
            tableView.getStyleClass().remove("fullHeight");
            tableView.removeEventFilter(ScrollEvent.ANY, eventConsumer);
        }
    }

    private List<TableColumn<Integer, ?>> currentColumns, newColumns;

    @Override
    public void updateVisualResult(VisualResult rs) {
        if (rs == null)
            return;
        rs = transformVisualResultValuesToProperties(rs);
        TableView<Integer> tableView = getFxNode();
        N dataGrid = getNode();
        synchronized (this) {
            currentColumns = tableView.getColumns();
            newColumns = new ArrayList<>();
            // Clearing the columns to completely rebuild them when table was empty (as the columns widths were not considering the content)
            int rowCount = rs.getRowCount();
            if (tableView.getItems().isEmpty() && rowCount > 0)
                currentColumns.clear();
            getNodePeerBase().fillGrid(rs);
            tableView.getSelectionModel().clearSelection(); // To avoid internal java 8 API call on Android
            tableView.getColumns().setAll(newColumns);
            currentColumns = newColumns = null;
            tableView.getSelectionModel().clearSelection(); // Clearing selection otherwise an undesired selection event is triggered on new items
            tableView.getItems().setAll(new IdentityList(rowCount));
            if (rowCount > 0) { // Workaround for the JavaFx wrong resize columns problem when vertical scroll bar appears
                tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
                UiScheduler.scheduleDelay(100, () -> tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY));
            }
            if (dataGrid.isFullHeight())
                fitHeightToContent(tableView, dataGrid);
        }
        dataGrid.requestLayout(); // this is essentially to clear the cached sized values (prefWith, etc...)
    }

    private static VisualResult transformVisualResultValuesToProperties(VisualResult rs) {
        return VisualResultBuilder.convertVisualResult(rs, SimpleObjectProperty::new);
    }

    @Override
    public void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, VisualColumn visualColumn) {
        TableColumn<Integer, ?> gridColumn = gridColumnIndex < currentColumns.size() ? currentColumns.get(gridColumnIndex) : new TableColumn<>();
        newColumns.add(gridColumn);
        Label label = visualColumn.getLabel();
        gridColumn.setText(label.getText());
        gridColumn.setGraphic(ImageStore.createImageView(label.getIconPath()));
        Double prefWidth = visualColumn.getStyle().getPrefWidth();
        if (prefWidth != null) {
            if (prefWidth > 24)
                prefWidth *= 1.3;
            prefWidth = prefWidth + 10; // because of the 5px left and right padding
            gridColumn.setPrefWidth(prefWidth);
            gridColumn.setMinWidth(prefWidth);
            gridColumn.setMaxWidth(prefWidth);
        }
        String textAlign = visualColumn.getStyle().getTextAlign();
        Pos alignment = "right".equals(textAlign) ? Pos.CENTER_RIGHT : "center".equals(textAlign) ? Pos.CENTER : Pos.CENTER_LEFT;
        gridColumn.setCellValueFactory(cdf -> (ObservableValue) getNodePeerBase().getRs().getValue(cdf.getValue(), rsColumnIndex));
        gridColumn.setCellFactory(param -> new TableCell() {
            { setAlignment(alignment); }
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty)
                    getNodePeerBase().fillCell(this, item, visualColumn);
            }
        });
    }

    @Override
    public void setCellImageAndTextContent(TableCell cell, Node image, String text, VisualColumn visualColumn) {
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
        Object value = base.getRowStyleResultValue(rowIndex);
        if (value instanceof ObservableValue)
            value = ((ObservableValue) value).getValue();
        return base.getRowStyleClasses(value);
    }

    private Paint getRowBackground(int rowIndex) {
        NB base = getNodePeerBase();
        Object value = base.getRowBackgroundResultValue(rowIndex);
        if (value instanceof ObservableValue)
            value = ((ObservableValue) value).getValue();
        return base.getRowBackground(value);
    }

    private static void fitHeightToContent(Control control, VisualGrid visualGrid) {
        // Quick ugly hacked code to make the table height fit with the content
        Properties.onPropertySet(control.skinProperty(), skin -> {
            ObservableList<javafx.scene.Node> children = null;
            if (skin instanceof Parent) // happens in java 7
                children = ((Parent) skin).getChildrenUnmodifiable();
            else if (skin instanceof SkinBase) // happens in java 8
                children = ((SkinBase) skin).getChildren();
            if (children != null) {
                Insets insets = control.getInsets();
                double h = insets.getTop() + insets.getBottom();
                for (javafx.scene.Node node : new ArrayList<>(children)) {
                    double nodePrefHeight = 0;
                    // Note: not compatible with Java 9 (VirtualFlow made inaccessible)
                    try {
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
                    } catch (Throwable e) {
                        // Java 9
                    }
                    if (nodePrefHeight == 0)
                        nodePrefHeight = node.prefHeight(-1);
                    h += nodePrefHeight;
                }
                Properties.setIfNotBound(visualGrid.prefHeightProperty(), h);
            }
        });
    }
}