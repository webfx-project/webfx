package naga.toolkit.fx.ext.cell.collator;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.fx.ext.cell.renderer.ArrayRenderer;
import naga.toolkit.fx.ext.cell.renderer.ValueRenderer;
import naga.toolkit.fx.ext.impl.DataGridImpl;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.effect.BlendMode;
import naga.toolkit.fx.scene.effect.Effect;
import naga.toolkit.fx.scene.layout.BorderPane;
import naga.toolkit.fx.scene.layout.impl.BorderPaneImpl;
import naga.toolkit.fx.scene.transform.Transform;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.impl.SceneImpl;
import naga.toolkit.fx.spi.viewer.NodeViewer;
import naga.toolkit.fx.spi.viewer.base.DataGridViewerBase;
import naga.toolkit.fx.spi.viewer.base.DataGridViewerMixin;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.events.UiEventHandler;
import naga.toolkit.util.ObservableLists;

import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class GridCollator extends DataGridImpl {

    private final NodeCollator columnCollator;
    private final NodeCollator rowCollator;
    private final BorderPane container;

    public GridCollator(String columnCollator, String rowCollator) {
        this(NodeCollatorRegistry.getCollator(columnCollator), NodeCollatorRegistry.getCollator(rowCollator));
    }

    public GridCollator(NodeCollator columnCollator, NodeCollator rowCollator) {
        this.columnCollator = columnCollator;
        this.rowCollator = rowCollator;
        this.container = BorderPane.create();
    }

    @Override
    public NodeViewer getNodeViewer() {
        NodeViewer nodeViewer = super.getNodeViewer();
        if (nodeViewer == null) {
            SceneImpl drawing = (SceneImpl) getScene();
            ((BorderPaneImpl) container).setScene(drawing);
            NodeViewer containerViewer = container.getOrCreateAndBindNodeViewer();
            setNodeViewer(nodeViewer = containerViewer);
            gridCollatorViewer = new GridCollatorViewer();
            gridCollatorViewer.bind(this, new SceneRequester() {

                @Override
                public void requestNodeViewerPropertyUpdate(Node node, ObservableValue changedProperty) {
                    gridCollatorViewer.updateProperty(changedProperty);
                }

                @Override
                public void requestNodeViewerListUpdate(Node node, ObservableList changedList) {
                    gridCollatorViewer.updateList(changedList);
                }
            });
        }
        return nodeViewer;
    }

    @Override
    protected void layoutChildren() {
        container.layout();
    }

    private GridCollatorViewer gridCollatorViewer;

    private class GridCollatorViewer extends DataGridViewerBase<GridCollator>
        implements DataGridViewerMixin<GridCollator> {

        private ValueRenderer[] renderers;
        private int[] rsColumnIndexes;

        {
            setMixin(this);
        }

        @Override
        public void updateSelectionMode(SelectionMode mode) {
        }

        @Override
        public void updateDisplaySelection(DisplaySelection selection) {
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
        public DataGridViewerBase<GridCollator> getNodeViewerBase() {
            return this;
        }

        @Override
        public void updateOnMouseClicked(UiEventHandler onMouseClicked) {
            container.setOnMouseClicked(onMouseClicked);
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
