package naga.toolkit.fx.spi.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import naga.commons.scheduler.Scheduled;
import naga.commons.util.collection.Collections;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.scene.control.Control;
import naga.toolkit.fx.scene.impl.NodeImpl;
import naga.toolkit.fx.scene.impl.ParentImpl;
import naga.toolkit.fx.scene.layout.PreferenceResizableNode;
import naga.toolkit.fx.spi.Drawing;
import naga.toolkit.fx.spi.DrawingNode;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.view.NodeView;
import naga.toolkit.fx.spi.view.NodeViewFactory;
import naga.toolkit.properties.markers.HasHeightProperty;
import naga.toolkit.properties.markers.HasWidthProperty;
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
    private final Property<Node> rootNodeProperty = new SimpleObjectProperty<Node>() {
        // Temporary code to automatically assume the following behaviour:
        // - the root node width is bound to the drawing node width
        // - the drawing node height is bound to the root node height (which eventually is bound to the preferred height)
        @Override
        protected void invalidated() {
            Node rootNode = getValue();
            // Binding the root node width to the drawing node width
            if (rootNode instanceof HasWidthProperty)
                ((HasWidthProperty) rootNode).widthProperty().bind(drawingNode.widthProperty());
            // Binding the drawing node height to the root node height
            if (rootNode instanceof HasHeightProperty) {
                HasHeightProperty root = (HasHeightProperty) rootNode;
                drawingNode.heightProperty().bind(root.heightProperty());
                if (root instanceof ParentImpl && root instanceof PreferenceResizableNode)
                    ((ParentImpl) root).setBindHeightToPrefHeight(true);
            }
        }
    };
    @Override
    public Property<Node> rootNodeProperty() {
        return rootNodeProperty;
    }

    private final DrawingRequester drawingRequester = new DrawingRequester() {

        @Override
        public void requestParentAndChildrenViewsUpdate(Parent parent) {
            updateParentAndChildrenViews(parent);
        }

        @Override
        public void requestViewPropertyUpdate(Node node, Property changedProperty) {
            updateViewProperty(node, changedProperty);
        }

        @Override
        public void requestViewListUpdate(Node node, ObservableList changedList) {
            updateViewList(node, changedList);
        }
    };

    protected DrawingImpl(DrawingNode drawingNode, NodeViewFactory nodeViewFactory) {
        this.drawingNode = drawingNode;
        this.nodeViewFactory = nodeViewFactory;
        Properties.runOnPropertiesChange(nodeProperty -> createAndBindRootNodeViewAndChildren(getRootNode()), rootNodeProperty());
    }

    public void setNodeViewFactory(NodeViewFactory nodeViewFactory) {
        if (this.nodeViewFactory != null) {
            Collections.forEach(nodeViews.values(), NodeView::unbind);
            nodeViews.clear();
        }
        this.nodeViewFactory = nodeViewFactory;
    }

    protected void keepParentAndChildrenViewsUpdated(Parent parent) {
        ObservableLists.runNowAndOnListChange(() -> {
            // Setting the parent to all children
            for (Node child : parent.getChildren())
                child.setParent(parent);
            updateParentAndChildrenViews(parent);
        }, parent.getChildren());
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
        createAndBindNodeViewAndChildren(rootNode);
    }

    private void createAndBindNodeViewAndChildren(Node node) {
        NodeView nodeView = getOrCreateAndBindNodeView(node);
        if (nodeView instanceof Parent)
            updateChildrenViews(((Parent) nodeView).getChildren());
    }

    public NodeView getOrCreateAndBindNodeView(Node node) {
        NodeView nodeView = nodeViews.get(node);
        if (nodeView == null) {
            nodeViews.put(node, nodeView = createNodeView(node));
            NodeImpl nodeImpl = (NodeImpl) node;
            nodeImpl.setNodeView(nodeView);
            nodeImpl.setDrawing(this);
            nodeView.bind(node, drawingRequester);
            if (node instanceof Parent && !(node instanceof Control)) {
                Parent parent = (Parent) node;
                keepParentAndChildrenViewsUpdated(parent);
            }
            if (!isPulseRunning() && isPulseRequiredForNode(node))
                startPulse();
        }
        return nodeView;
    }

    protected NodeView<Node> createNodeView(Node node) {
        return nodeViewFactory.createNodeView(node);
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
