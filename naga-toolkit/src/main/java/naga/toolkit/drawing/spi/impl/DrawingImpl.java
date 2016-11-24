package naga.toolkit.drawing.spi.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import naga.commons.scheduler.Scheduled;
import naga.commons.util.collection.Collections;
import naga.toolkit.drawing.scene.Node;
import naga.toolkit.drawing.scene.Parent;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.NodeView;
import naga.toolkit.drawing.spi.view.NodeViewFactory;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.util.ObservableLists;
import naga.toolkit.util.Properties;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class DrawingImpl implements Drawing {

    protected final DrawingNode drawingNode;
    private NodeViewFactory nodeViewFactory;
    private final Map<Node, NodeView> nodeViews = new HashMap<>();
    private final Property<Node> rootNodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Node> rootNodeProperty() {
        return rootNodeProperty;
    }

    private final DrawingRequester drawingRequester = new DrawingRequester() {

        @Override
        public void requestParentAndChildrenViewsUpdate(Parent parent) {
            drawingThreadLocal.set(DrawingImpl.this);
            updateParentAndChildrenViews(parent);
            drawingThreadLocal.set(null);
        }

        @Override
        public void requestViewPropertyUpdate(Node node, Property changedProperty) {
            drawingThreadLocal.set(DrawingImpl.this);
            updateViewProperty(node, changedProperty);
            drawingThreadLocal.set(null);
        }

        @Override
        public void requestViewListUpdate(Node node, ObservableList changedList) {
            drawingThreadLocal.set(DrawingImpl.this);
            updateViewList(node, changedList);
            drawingThreadLocal.set(null);
        }
    };

    protected DrawingImpl(DrawingNode drawingNode, NodeViewFactory nodeViewFactory) {
        this.drawingNode = drawingNode;
        this.nodeViewFactory = nodeViewFactory;
        Properties.runOnPropertiesChange(nodeProperty -> createAndBindRootNodeViewAndChildren(getRootNode()), rootNodeProperty());
    }

    private final static ThreadLocal<DrawingImpl> drawingThreadLocal = new ThreadLocal<>();
    public static DrawingImpl getThreadLocalDrawing() {
        return drawingThreadLocal.get();
    }

    public void setNodeViewFactory(NodeViewFactory nodeViewFactory) {
        if (this.nodeViewFactory != null) {
            Collections.forEach(nodeViews.values(), NodeView::unbind);
            nodeViews.clear();
        }
        this.nodeViewFactory = nodeViewFactory;
    }

    protected void keepParentAndChildrenViewsUpdated(Parent parent) {
        ObservableLists.runNowAndOnListChange(() -> updateParentAndChildrenViews(parent), parent.getChildren());
    }

    protected void updateParentAndChildrenViews(Parent parent) {
        updateChildrenViews(parent.getChildren());
    }

    protected boolean updateViewProperty(Node node, Property changedProperty) {
        return updateViewProperty(getOrCreateAndBindNodeView(node), changedProperty);
    }

    private boolean updateViewProperty(NodeView nodeView, Property changedProperty) {
        return nodeView.updateProperty(changedProperty);
    }

    private boolean updateViewList(Node node, ObservableList changedList) {
        return updateViewList(getOrCreateAndBindNodeView(node), changedList);
    }

    private boolean updateViewList(NodeView nodeView, ObservableList changedList) {
        return nodeView.updateList(changedList);
    }

    private void updateChildrenViews(Collection<Node> nodes) {
        Collections.forEach(nodes, this::createAndBindNodeViewAndChildren);
    }

    protected void createAndBindRootNodeViewAndChildren(Node rootNode) {
        drawingThreadLocal.set(DrawingImpl.this);
        createAndBindNodeViewAndChildren(rootNode);
        drawingThreadLocal.set(null);
    }

    private void createAndBindNodeViewAndChildren(Node node) {
        NodeView nodeView = getOrCreateAndBindNodeView(node);
        if (nodeView instanceof Parent)
            updateChildrenViews(((Parent) nodeView).getChildren());
    }

    public NodeView getOrCreateAndBindNodeView(Node node) {
        NodeView nodeView = nodeViews.get(node);
        if (nodeView == null) {
            nodeViews.put(node, nodeView = nodeViewFactory.createNodeView(node));
            nodeView.bind(node, drawingRequester);
            if (node instanceof Parent) {
                Parent parent = (Parent) node;
                keepParentAndChildrenViewsUpdated(parent);
            }
            if (!isPulseRunning() && isPulseRequiredForNode(node))
                startPulse();
        }
        return nodeView;
    }

    protected boolean isRootNode(Node node) {
        return node == getRootNode();
    }

    private boolean isPulseRequiredForNode(Node node) {
        return node instanceof Parent;
    }

    protected boolean isPulseRunning() {
        return pulseScheduled != null;
    }

    private Scheduled pulseScheduled;
    protected void startPulse() {
        if (pulseScheduled == null)
            pulseScheduled = Toolkit.get().scheduler().schedulePeriodicAnimationFrame(this::pulse);
    }

    protected void pulse() {
        Node rootNode = getRootNode();
        if (rootNode instanceof Parent)
            ((Parent) rootNode).layout();
    }

    protected void stopPulse() {
        if (pulseScheduled != null) {
            pulseScheduled.cancel();
            pulseScheduled = null;
        }
    }
}
