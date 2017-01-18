package naga.fxdata.cell.collator;

import emul.javafx.beans.value.ObservableValue;
import emul.javafx.collections.ObservableList;
import emul.javafx.geometry.HPos;
import emul.javafx.geometry.VPos;
import emul.javafx.scene.Node;
import emul.javafx.scene.Scene;
import emul.javafx.scene.effect.BlendMode;
import emul.javafx.scene.effect.Effect;
import emul.javafx.scene.layout.Background;
import emul.javafx.scene.layout.Border;
import emul.javafx.scene.layout.BorderPane;
import emul.javafx.scene.transform.Transform;
import naga.fx.properties.ObservableLists;
import naga.fx.scene.SceneRequester;
import naga.fx.spi.peer.NodePeer;
import naga.fxdata.cell.renderer.ArrayRenderer;
import naga.fxdata.cell.renderer.ValueRenderer;
import naga.fxdata.control.DataGrid;
import naga.fxdata.displaydata.DisplayColumn;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.displaydata.DisplaySelection;
import naga.fxdata.displaydata.SelectionMode;
import naga.fxdata.spi.peer.base.DataGridPeerBase;
import naga.fxdata.spi.peer.base.DataGridPeerMixin;

import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class GridCollator extends DataGrid {

    private final NodeCollator columnCollator;
    private final NodeCollator rowCollator;
    private final BorderPane container;

    public GridCollator(String columnCollator, String rowCollator) {
        this(NodeCollatorRegistry.getCollator(columnCollator), NodeCollatorRegistry.getCollator(rowCollator));
    }

    public GridCollator(NodeCollator columnCollator, NodeCollator rowCollator) {
        this.columnCollator = columnCollator;
        this.rowCollator = rowCollator;
        container = new BorderPane();
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
    }

    @Override
    public NodePeer getNodePeer() {
        NodePeer nodePeer = super.getNodePeer();
        if (nodePeer == null) {
            Scene scene = getScene();
            container.setScene(scene);
            NodePeer containerPeer = container.getOrCreateAndBindNodePeer();
            setNodePeer(nodePeer = containerPeer);
            gridCollatorPeer = new GridCollatorPeer();
            gridCollatorPeer.bind(this, new SceneRequester() {

                @Override
                public void requestNodePeerPropertyUpdate(Node node, ObservableValue changedProperty) {
                    gridCollatorPeer.updateProperty(changedProperty);
                }

                @Override
                public void requestNodePeerListUpdate(Node node, ObservableList changedList) {
                    gridCollatorPeer.updateList(changedList);
                }
            });
        }
        return nodePeer;
    }

    @Override
    protected void layoutChildren() {
        layoutInArea(container, getLayoutX(), getLayoutY(), getWidth(), getHeight(), 0, HPos.LEFT, VPos.TOP);
        container.layout();
    }

    private GridCollatorPeer gridCollatorPeer;

    private class GridCollatorPeer
            <N extends DataGrid, NB extends DataGridPeerBase<GridCollator, N, NB, NM>, NM extends DataGridPeerMixin<GridCollator, N, NB, NM>>

            extends DataGridPeerBase<GridCollator, N, NB, NM>
            implements DataGridPeerMixin<GridCollator, N, NB, NM> {

        private ValueRenderer[] renderers;
        private int[] rsColumnIndexes;

        {
            setMixin((NM) this);
        }

        @Override
        public void requestFocus() {
        }

        @Override
        public void updateSelectionMode(SelectionMode mode) {
        }

        @Override
        public void updateDisplaySelection(DisplaySelection selection) {
        }

        @Override
        public void updateBackground(Background background) {
        }

        @Override
        public void updateBorder(Border border) {
        }

        @Override
        public void updateResultSet(DisplayResultSet rs) {
            if (rs == null)
                return;
            int columnCount = rs.getColumnCount();
            renderers = new ValueRenderer[columnCount];
            rsColumnIndexes = new int[columnCount];
            fillGrid(rs);
            int rowCount = rs.getRowCount();
            Node[] rowNodes = new Node[rowCount];
            Object[] columnValues = new Object[getGridColumnCount()];
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                for (int gridColumnIndex = 0; gridColumnIndex < getGridColumnCount(); gridColumnIndex++)
                    columnValues[gridColumnIndex] = rs.getValue(rowIndex, rsColumnIndexes[gridColumnIndex]);
                rowNodes[rowIndex] = ArrayRenderer.renderCellValue(columnValues, renderers, columnCollator);
            }
            Node finalNode = rowCollator.collateNodes(rowNodes);
            container.setCenter(finalNode);
            ObservableLists.setAllNonNulls(getChildren(), finalNode);
        }

        @Override
        public void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn) {
            renderers[gridColumnIndex] = displayColumn.getValueRenderer();
            rsColumnIndexes[gridColumnIndex] = rsColumnIndex;
        }

        @Override
        public void setCellContent(GridCollator cell, Node content, DisplayColumn displayColumn) {
            // not called
        }

        @Override
        public void updateWidth(Double width) {
            container.setWidth(width);
        }

        @Override
        public void updateHeight(Double height) {
            container.setHeight(height);
        }

        @Override
        public NB getNodePeerBase() {
            return (NB) this;
        }

        @Override
        public void updateMouseTransparent(Boolean mouseTransparent) {
            container.setMouseTransparent(mouseTransparent);
        }

        @Override
        public void updateVisible(Boolean visible) {
            container.setVisible(visible);
        }

        @Override
        public void updateOpacity(Double opacity) {
            container.setOpacity(opacity);
        }

        @Override
        public void updateDisabled(Boolean disabled) {
        }

        @Override
        public void updateClip(Node clip) {
            container.setClip(clip);
        }

        @Override
        public void updateBlendMode(BlendMode blendMode) {
            container.setBlendMode(blendMode);
        }

        @Override
        public void updateEffect(Effect effect) {
            container.setEffect(effect);
        }

        @Override
        public void updateLayoutX(Double layoutX) {
            container.setLayoutX(layoutX);
        }

        @Override
        public void updateLayoutY(Double layoutY) {
            container.setLayoutY(layoutY);
        }

        @Override
        public void updateTransforms(List<Transform> transforms) {
            container.getTransforms().setAll(transforms);
        }

        @Override
        public void updateLocalToParentTransforms(Collection<Transform> localToParentTransforms) {
        }
    }
}
