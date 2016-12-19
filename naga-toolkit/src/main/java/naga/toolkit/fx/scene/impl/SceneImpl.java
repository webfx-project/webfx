package naga.toolkit.fx.scene.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import naga.commons.scheduler.Scheduled;
import naga.commons.scheduler.UiScheduler;
import naga.commons.util.Strings;
import naga.commons.util.collection.Collections;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.control.Control;
import naga.toolkit.fx.scene.Scene;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.spi.viewer.NodeViewer;
import naga.toolkit.fx.spi.viewer.NodeViewerFactory;
import naga.toolkit.properties.markers.HasHeightProperty;
import naga.toolkit.properties.markers.HasWidthProperty;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.util.ObservableLists;
import naga.toolkit.util.Properties;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public abstract class SceneImpl implements Scene {

    private final Property<Double> widthProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> widthProperty() {
        return widthProperty;
    }

    private final Property<Double> heightProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> heightProperty() {
        return heightProperty;
    }

    private NodeViewerFactory nodeViewerFactory;
    private final Property<Parent> rootProperty = new SimpleObjectProperty<Parent>() {
        // Temporary code to automatically assume the following behaviour:
        // - the root node width is bound to the drawing node width
        // - the drawing node height is bound to the root node height (which eventually is bound to the preferred height)
        @Override
        protected void invalidated() {
            ParentImpl root = (ParentImpl) getValue();
            root.setScene(SceneImpl.this);
            root.setSceneRoot(true);
            // Binding the root node width to the drawing node width
            if (root instanceof HasWidthProperty)
                ((HasWidthProperty) root).widthProperty().bind(widthProperty());
            // Binding the drawing node height to the root node height
            if (root instanceof HasHeightProperty) {
                ((HasHeightProperty) root).heightProperty().bind(heightProperty());
/*
                HasHeightProperty root = (HasHeightProperty) root;
                drawingNode.heightProperty().bind(root.heightProperty());
                if (root instanceof ParentImpl && root instanceof PreferenceResizableNode)
                    ((ParentImpl) root).setBindHeightToPrefHeight(true);
*/
            }
        }
    };
    @Override
    public Property<Parent> rootProperty() {
        return rootProperty;
    }

    private final SceneRequester sceneRequester = new SceneRequester() {

        @Override
        public void requestNodeViewerPropertyUpdate(Node node, ObservableValue changedProperty) {
            execute(() -> updateViewProperty(node, changedProperty));
        }

        @Override
        public void requestNodeViewerListUpdate(Node node, ObservableList changedList) {
            execute(() -> updateViewList(node, changedList));
        }

        private void execute(Runnable runnable) {
            UiScheduler uiScheduler = Toolkit.get().scheduler();
            if (uiScheduler.isAnimationFrame())
                runnable.run();
            else
                uiScheduler.scheduleAnimationFrame(runnable);
        }
    };

    protected SceneImpl(NodeViewerFactory nodeViewerFactory) {
        this.nodeViewerFactory = nodeViewerFactory;
        Properties.runOnPropertiesChange(nodeProperty -> createAndBindRootNodeViewerAndChildren(getRoot()), rootProperty());
    }

    public void setNodeViewerFactory(NodeViewerFactory nodeViewerFactory) {
/*
        if (this.nodeViewerFactory != null) {
            Collections.forEach(nodeViews.values(), NodeViewer::unbind);
            nodeViews.clear();
        }
*/
        this.nodeViewerFactory = nodeViewerFactory;
    }

    public SceneRequester getSceneRequester() {
        return sceneRequester;
    }

    private void keepParentAndChildrenViewersUpdated(Parent parent) {
        ObservableLists.runNowAndOnListChange(() -> {
            // Setting the parent to all children
            for (Node child : parent.getChildren())
                child.setParent(parent);
            updateParentAndChildrenViewers(parent);
        }, parent.getChildren());
    }

    protected void updateParentAndChildrenViewers(Parent parent) {
        updateChildrenViewers(parent.getChildren());
    }

    protected boolean updateViewProperty(Node node, ObservableValue changedProperty) {
        return updateViewProperty(getOrCreateAndBindNodeViewer(node), changedProperty);
    }

    private boolean updateViewProperty(NodeViewer nodeViewer, ObservableValue changedProperty) {
        return nodeViewer.updateProperty(changedProperty);
    }

    private boolean updateViewList(Node node, ObservableList changedList) {
        return updateViewList(getOrCreateAndBindNodeViewer(node), changedList);
    }

    private boolean updateViewList(NodeViewer nodeViewer, ObservableList changedList) {
        return nodeViewer.updateList(changedList);
    }

    private void updateChildrenViewers(Collection<Node> nodes) {
        Collections.forEach(nodes, this::createAndBindNodeViewerAndChildren);
    }

    protected void createAndBindRootNodeViewerAndChildren(Node rootNode) {
        createAndBindNodeViewerAndChildren(rootNode);
    }

    private void createAndBindNodeViewerAndChildren(Node node) {
        NodeViewer nodeViewer = getOrCreateAndBindNodeViewer(node);
        if (nodeViewer instanceof Parent)
            updateChildrenViewers(((Parent) nodeViewer).getChildren());
    }

    public NodeViewer getOrCreateAndBindNodeViewer(Node node) {
        NodeImpl nodeImpl = (NodeImpl) node;
        if (node.getScene() != this)
            nodeImpl.setScene(this);
        NodeViewer nodeViewer = node.getNodeViewer();
        if (nodeViewer == null) {
            nodeImpl.setNodeViewer(nodeViewer = createNodeViewer(node));
            if (nodeViewer == null) // The node view factory was unable to create a view for this node!
                nodeImpl.setNodeViewer(nodeViewer = createUnimplementedNodeViewer(node)); // Displaying a "Unimplemented..." button instead
            else { // Standard case (the node view was successfully created)
                nodeViewer.bind(node, sceneRequester);
                if (node instanceof Parent && !(node instanceof Control)) {
                    Parent parent = (Parent) node;
                    keepParentAndChildrenViewersUpdated(parent);
                }
            }
            if (!isPulseRunning() && isPulseRequiredForNode(node))
                startPulse();
        }
        return nodeViewer;
    }


    protected NodeViewer<Node> createNodeViewer(Node node) {
        return nodeViewerFactory.createNodeViewer(node);
    }

    private NodeViewer createUnimplementedNodeViewer(Node node) {
        // Creating a button as replacement (assuming the target toolkit at least implements a button view!)
        Button button = Button.create(Strings.removeSuffix(node.getClass().getSimpleName(), "Impl") + " viewer not provided");
        // Binding to allow the button to respond to the original node layout
        button.layoutXProperty().bind(node.layoutXProperty());
        button.layoutYProperty().bind(node.layoutYProperty());
        if (node instanceof HasWidthProperty)
            button.widthProperty().bind(((HasWidthProperty) node).widthProperty());
        if (node instanceof HasHeightProperty)
            button.heightProperty().bind(((HasHeightProperty) node).heightProperty());
        return getOrCreateAndBindNodeViewer(button); // Finally retuning the button view
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
            pulseScheduled = Toolkit.get().scheduler().schedulePeriodicAnimationFrame(this::pulse, true);
    }

    protected void pulse() {
        Parent root = getRoot();
        if (root != null)
            root.layout();
    }

    protected void stopPulse() {
        if (pulseScheduled != null) {
            pulseScheduled.cancel();
            pulseScheduled = null;
        }
    }
}
