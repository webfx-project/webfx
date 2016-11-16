package naga.providers.toolkit.javafx.nodes.controls;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import naga.commons.util.collection.IdentityList;
import naga.providers.toolkit.javafx.JavaFxToolkit;
import naga.providers.toolkit.javafx.nodes.FxSelectableDisplayResultSetNode;
import naga.providers.toolkit.javafx.util.FxImageStore;
import naga.toolkit.adapters.grid.GridFiller;
import naga.toolkit.adapters.grid.ImageTextGridAdapter;
import naga.toolkit.adapters.rowstyle.RowAdapter;
import naga.toolkit.adapters.rowstyle.RowStyleUpdater;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.display.Label;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Table;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Bruno Salmon
 */
public class FxTable extends FxSelectableDisplayResultSetNode<TableView<Integer>> implements Table {

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
        // Workaround to make the table height fit with its content if it is not within a BorderPane
        if (!(node.getParent() instanceof BorderPane))
            fitHeightToContent(node);
    }


    private final GridFiller gridFiller = new GridFiller<TableCell>((ImageTextGridAdapter<TableCell>) (cell, image, text, displayColumn) -> {
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
            // currentColumns = newColumns = null; // Commented as may cause NPE in setUpGridColumn() TODO: check if it's a bug
        }

        @Override
        protected void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn) {
            TableColumn<Integer, ?> gridColumn = gridColumnIndex < currentColumns.size() ? currentColumns.get(gridColumnIndex) : new TableColumn<>();
            newColumns.add(gridColumn);
            Label label = displayColumn.getLabel();
            gridColumn.setText(label.getText());
            gridColumn.setGraphic(FxImageStore.createLabelIconImageView(label));
            Double prefWidth = displayColumn.getStyle().getPrefWidth();
            if (prefWidth != null) {
                // Applying same prefWidth transformation as the PolymerTable (trying to
                if (label.getText() != null)
                    prefWidth = prefWidth * 2.75; // factor compared to JavaFx style (temporary hardcoded)
                prefWidth = prefWidth + 10; // because of the 5px left and right padding
                gridColumn.setPrefWidth(prefWidth);
                gridColumn.setMinWidth(prefWidth);
                gridColumn.setMaxWidth(prefWidth);
            }
            String textAlign = displayColumn.getStyle().getTextAlign();
            Pos alignment = "right".equals(textAlign) ? Pos.CENTER_RIGHT : "center".equals(textAlign) ? Pos.CENTER : Pos.CENTER_LEFT;
            gridColumn.setCellValueFactory(cdf -> (ObservableValue) getDisplayResultSet().getValue(cdf.getValue(), rsColumnIndex));
            gridColumn.setCellFactory(param -> new TableCell() {
                { setAlignment(alignment); }
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    fillCell(this, item, displayColumn);
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
            ObservableList<Node> children = null;
            if (skin instanceof Parent) // happens in java 7
                children = ((Parent) skin).getChildrenUnmodifiable();
            else if (skin instanceof SkinBase) // happens in java 8
                children = ((SkinBase) skin).getChildren();
            if (children != null) {
                double h = 2; // border
                for (Node node : new ArrayList<>(children)) {
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
