package webfx.extras.cell.collator.grid;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.SkinBase;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Transform;
import webfx.extras.visual.controls.grid.peers.base.VisualGridPeerBase;
import webfx.extras.visual.controls.grid.peers.base.VisualGridPeerMixin;
import webfx.extras.cell.collator.NodeCollator;
import webfx.extras.cell.collator.NodeCollatorRegistry;
import webfx.extras.cell.renderer.ArrayRenderer;
import webfx.extras.cell.renderer.ValueRenderer;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualSelection;
import webfx.extras.visual.SelectionMode;
import webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import webfx.kit.util.properties.ObservableLists;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class GridCollator extends VisualGrid {

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
        ObservableLists.setAllNonNulls(getChildren(), container);
        setSkin(new SkinBase(this) {}); // So the peer displays (skin) children
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
                public void requestNodePeerListUpdate(Node node, ObservableList changedList, ListChangeListener.Change change) {
                    gridCollatorPeer.updateList(changedList, change);
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

    private final class GridCollatorPeer
            <N extends VisualGrid, NB extends VisualGridPeerBase<GridCollator, N, NB, NM>, NM extends VisualGridPeerMixin<GridCollator, N, NB, NM>>

            extends VisualGridPeerBase<GridCollator, N, NB, NM>
            implements VisualGridPeerMixin<GridCollator, N, NB, NM> {

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
        public void updateVisualSelection(VisualSelection selection) {
        }

        @Override
        public void updateHeaderVisible(boolean headerVisible) {
        }

        @Override
        public void updateFullHeight(boolean fullHeight) {
        }

        @Override
        public void updateBackground(Background background) {
            container.setBackground(background);
        }

        @Override
        public void updateBorder(Border border) {
            container.setBorder(border);
        }

        @Override
        public void updatePadding(Insets padding) {
            container.setPadding(padding);
        }

        @Override
        public void updateCursor(Cursor cursor) {
        }

        @Override
        public void updateOnDragDetected(EventHandler<? super MouseEvent> eventHandler) {
        }

        @Override
        public void updateOnDragEntered(EventHandler<? super DragEvent> eventHandler) {
        }

        @Override
        public void updateOnDragOver(EventHandler<? super DragEvent> eventHandler) {
        }

        @Override
        public void updateOnDragDropped(EventHandler<? super DragEvent> eventHandler) {
        }

        @Override
        public void updateOnDragExited(EventHandler<? super DragEvent> eventHandler) {
        }

        @Override
        public void updateOnDragDone(EventHandler<? super DragEvent> eventHandler) {
        }

        @Override
        public void updateVisualResult(VisualResult rs) {
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
                rowNodes[rowIndex] = ArrayRenderer.renderValue(columnValues, renderers, columnCollator);
            }
            Node finalNode = rowCollator.collateNodes(rowNodes);
            container.setTop(finalNode);
            layoutChildren();
        }

        @Override
        public void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, VisualColumn visualColumn) {
            renderers[gridColumnIndex] = visualColumn.getValueRenderer();
            rsColumnIndexes[gridColumnIndex] = rsColumnIndex;
        }

        @Override
        public void setCellContent(GridCollator cell, Node content, VisualColumn visualColumn) {
            // not called
        }

        @Override
        public void updateWidth(Number width) {
        }

        @Override
        public void updateHeight(Number height) {
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
        public void updateId(String id) {
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
        public void updateLayoutX(Number layoutX) {
        }

        @Override
        public void updateLayoutY(Number layoutY) {
        }

        @Override
        public void updateTranslateX(Number translateX) {
        }

        @Override
        public void updateTranslateY(Number translateY) {
        }

        @Override
        public void updateScaleX(Number scaleX) {
        }

        @Override
        public void updateScaleY(Number scaleX) {
        }

        @Override
        public void updateRotate(Number rotate) {
        }

        @Override
        public void updateTransforms(List<Transform> transforms, ListChangeListener.Change<Transform> change) {
            container.getTransforms().setAll(transforms);
        }

        @Override
        public void updateLocalToParentTransforms(List<Transform> localToParentTransforms) {
        }

        @Override
        public void updateStyleClass(List<String> styleClass, ListChangeListener.Change<String> change) {
            container.getStyleClass().setAll(styleClass);
        }
    }
}
