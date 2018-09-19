package emul.javafx.scene;

import emul.com.sun.javafx.collections.SourceAdapterChange;
import emul.com.sun.javafx.event.EventQueue;
import emul.com.sun.javafx.scene.SceneEventDispatcher;
import emul.com.sun.javafx.tk.TKPulseListener;
import emul.com.sun.javafx.tk.TKSceneListener;
import emul.javafx.animation.KeyFrame;
import emul.javafx.animation.Timeline;
import emul.javafx.application.Platform;
import emul.javafx.beans.property.*;
import emul.javafx.beans.value.ObservableValue;
import emul.javafx.collections.FXCollections;
import emul.javafx.collections.ListChangeListener;
import emul.javafx.collections.ObservableList;
import emul.javafx.collections.ObservableMap;
import emul.javafx.event.Event;
import emul.javafx.event.EventDispatchChain;
import emul.javafx.event.EventDispatcher;
import emul.javafx.event.EventTarget;
import emul.javafx.geometry.Orientation;
import emul.javafx.scene.input.*;
import emul.javafx.scene.paint.Color;
import emul.javafx.scene.shape.Rectangle;
import emul.javafx.stage.Window;
import emul.javafx.util.Duration;
import webfx.fxkits.core.mapper.FxKitMapper;
import webfx.fxkits.core.mapper.spi.NodePeer;
import webfx.fxkits.core.mapper.spi.ScenePeer;
import webfx.fxkits.core.mapper.spi.SceneRequester;
import webfx.fxkits.core.mapper.spi.StagePeer;
import webfx.fxkits.core.mapper.spi.impl.peer.markers.HasHeightProperty;
import webfx.fxkits.core.mapper.spi.impl.peer.markers.HasRootProperty;
import webfx.fxkits.core.mapper.spi.impl.peer.markers.HasWidthProperty;
import webfx.fxkits.core.util.properties.ObservableLists;
import webfx.fxkits.core.util.properties.Properties;
import webfx.platforms.core.services.scheduler.Scheduled;
import webfx.platforms.core.services.uischeduler.AnimationFramePass;
import webfx.platforms.core.services.uischeduler.UiScheduler;
import webfx.platforms.core.util.collection.Collections;

import java.util.*;

/**
 * @author Bruno Salmon
 */
public class Scene implements EventTarget,
        HasRootProperty,
        HasWidthProperty,
        HasHeightProperty {

    private double widthSetByUser = -1.0;
    private double heightSetByUser = -1.0;

    public Scene(Parent root) {
        this(root, -1, -1);
    }

    public Scene(Parent root, double width, double height) {
        setRoot(root);
        init(width, height);
    }

    private void init(double width, double height) {
        if (width >= 0) {
            widthSetByUser = width;
            setWidth(width);
        }
        if (height >= 0) {
            heightSetByUser = height;
            setHeight(height);
        }
        mouseHandler = new MouseHandler();
        clickGenerator = new ClickGenerator();
    }

    private final Property<Double> widthProperty = new SimpleObjectProperty<Double>(0d) {
        @Override
        protected void invalidated() {
            Parent _root = getRoot();
            if (_root.isResizable()) {
                resizeRootOnSceneSizeChange(get() - _root.getLayoutX() /*- _root.getTranslateX()*/, _root.getLayoutBounds().getHeight());
            }
        }
    };

    @Override
    public Property<Double> widthProperty() {
        return widthProperty;
    }

    private final Property<Double> heightProperty = new SimpleObjectProperty<Double>(0d) {
        @Override
        protected void invalidated() {
            Parent _root = getRoot();
            if (_root.isResizable()) {
                resizeRootOnSceneSizeChange(_root.getLayoutBounds().getWidth(), get() - _root.getLayoutY() /*- _root.getTranslateY()*/);
            }
        }
    };
    @Override
    public Property<Double> heightProperty() {
        return heightProperty;
    }

    void resizeRootOnSceneSizeChange(double newWidth, double newHeight) {
        getRoot().resize(newWidth, newHeight);
    }

    private final Property<Parent> rootProperty = new SimpleObjectProperty<Parent>() {
        // Temporary code to automatically assume the following behaviour:
        // - the root node width is bound to the scene width
        // - the scene height is bound to the root node height (which eventually is bound to the preferred height)
        @Override
        protected void invalidated() {
            Parent root = getValue();
            root.setScene(Scene.this);
            root.setSceneRoot(true);
            createAndBindRootNodePeerAndChildren(getRoot());
        }
    };

    /**
     * The horizontal location of this {@code Scene} on the {@code Window}.
     */
    private Property<Double> x;

    private final void setX(double value) {
        xPropertyImpl().setValue(value);
    }

    public final double getX() {
        return x == null ? 0.0 : x.getValue();
    }

    public final ReadOnlyProperty<Double> xProperty() {
        return xPropertyImpl()/*.getReadOnlyProperty()*/;
    }

    private Property<Double> xPropertyImpl() {
        if (x == null) {
            x = new SimpleObjectProperty<>(this, "x", 0d);
        }
        return x;
    }

    /**
     * The vertical location of this {@code Scene} on the {@code Window}.
     */
    private Property<Double> y;

    private void setY(double value) {
        yPropertyImpl().setValue(value);
    }

    public final double getY() {
        return y == null ? 0.0 : y.getValue();
    }

    public final ReadOnlyProperty<Double> yProperty() {
        return yPropertyImpl()/*.getReadOnlyProperty()*/;
    }

    private Property<Double> yPropertyImpl() {
        if (y == null) {
            y = new SimpleObjectProperty<>(this, "y", 0d);
        }
        return y;
    }

    @Override
    public Property<Parent> rootProperty() {
        return rootProperty;
    }

    /**
     * The {@code Window} for this {@code Scene}
     */
    private Property<Window> window;

    private void setWindow(Window value) {
        windowPropertyImpl().setValue(value);
    }

    public final Window getWindow() {
        return window == null ? null : window.getValue();
    }

    public final ReadOnlyProperty<Window> windowProperty() {
        return windowPropertyImpl()/*.getReadOnlyProperty()*/;
    }

    private Property<Window> windowPropertyImpl() {
        if (window == null) {
            window = new SimpleObjectProperty<Window>() {
                private Window oldWindow;

                @Override protected void invalidated() {
                    Window newWindow = getValue();
                    ///getKeyHandler().windowForSceneChanged(oldWindow, newWindow);
                    if (oldWindow != null) {
                        impl_disposePeer();
                    }
                    if (newWindow != null) {
                        impl_initPeer();
                    }
                    //parentEffectiveOrientationInvalidated();

                    oldWindow = newWindow;
                }

                @Override
                public Object getBean() {
                    return Scene.this;
                }

                @Override
                public String getName() {
                    return "window";
                }
            };
        }
        return window;
    }

    //@Deprecated
    public void impl_setWindow(Window value) {
        setWindow(value);
    }


    private void preferredSize() {
        final Parent root = getRoot();

        // one or the other isn't initialized, need to perform layout in
        // order to ensure we can properly measure the preferred size of the
        // scene
        doCSSPass();

        resizeRootToPreferredSize(root);
        doLayoutPass();

        if (widthSetByUser < 0) {
            setWidth(root.isResizable()? root.getLayoutX() /*+ root.getTranslateX()*/ + root.getLayoutBounds().getWidth() :
                    root.getLayoutBounds().getMaxX());
        } else {
            setWidth(widthSetByUser);
        }

        if (heightSetByUser < 0) {
            setHeight(root.isResizable()? root.getLayoutY() /*+ root.getTranslateY()*/ + root.getLayoutBounds().getHeight() :
                    root.getLayoutBounds().getMaxY());
        } else {
            setHeight(heightSetByUser);
        }

        //PerformanceTracker.logEvent("Scene preferred bounds computation complete");
    }

    final void resizeRootToPreferredSize(Parent root) {
        final double preferredWidth;
        final double preferredHeight;

        final Orientation contentBias = root.getContentBias();
        if (contentBias == null) {
            preferredWidth = getPreferredWidth(root, widthSetByUser, -1);
            preferredHeight = getPreferredHeight(root, heightSetByUser, -1);
        } else if (contentBias == Orientation.HORIZONTAL) {
            // height depends on width
            preferredWidth = getPreferredWidth(root, widthSetByUser, -1);
            preferredHeight = getPreferredHeight(root, heightSetByUser,
                    preferredWidth);
        } else /* if (contentBias == Orientation.VERTICAL) */ {
            // width depends on height
            preferredHeight = getPreferredHeight(root, heightSetByUser, -1);
            preferredWidth = getPreferredWidth(root, widthSetByUser,
                    preferredHeight);
        }

        root.resize(preferredWidth, preferredHeight);
    }

    private static double getPreferredWidth(Parent root,
                                            double forcedWidth,
                                            double height) {
        if (forcedWidth >= 0) {
            return forcedWidth;
        }
        final double normalizedHeight = (height >= 0) ? height : -1;
        return root.boundedSize(root.prefWidth(normalizedHeight),
                root.minWidth(normalizedHeight),
                root.maxWidth(normalizedHeight));
    }

    private static double getPreferredHeight(Parent root,
                                             double forcedHeight,
                                             double width) {
        if (forcedHeight >= 0) {
            return forcedHeight;
        }
        final double normalizedWidth = (width >= 0) ? width : -1;
        return root.boundedSize(root.prefHeight(normalizedWidth),
                root.minHeight(normalizedWidth),
                root.maxHeight(normalizedWidth));
    }

    /**
     * @treatAsPrivate implementation detail
     */
    //@Deprecated
    public void impl_preferredSize() {
        preferredSize();
    }

    private Node oldFocusOwner;

    /**
     * The scene's current focus owner node. This node's "focused"
     * variable might be false if this scene has no window, or if the
     * window is inactive (window.focused == false).
     * @since JavaFX 2.2
     */
    private ReadOnlyObjectWrapper<Node> focusOwner = new ReadOnlyObjectWrapper<Node>(this, "focusOwner") {

        @Override
        protected void invalidated() {
            if (oldFocusOwner != null) {
                ((Node.FocusedProperty) oldFocusOwner.focusedProperty()).store(false);
            }
            Node value = get();
            if (value != null) {
                ((Node.FocusedProperty) value.focusedProperty()).store(true/*keyHandler.windowFocused*/);
                if (value != oldFocusOwner) {
                    value.requestPeerFocus();
/*
                    value.getScene().impl_enableInputMethodEvents(
                            value.getInputMethodRequests() != null
                                    && value.getOnInputMethodTextChanged() != null);
*/
                }
            }
            // for the rest of the method we need to update the oldFocusOwner
            // and use a local copy of it because the user handlers can cause
            // recurrent calls of requestFocus
            Node localOldOwner = oldFocusOwner;
            oldFocusOwner = value;
            if (localOldOwner != null) {
                ((Node.FocusedProperty) localOldOwner.focusedProperty()).notifyListeners();
            }
            if (value != null) {
                ((Node.FocusedProperty) value.focusedProperty()).notifyListeners();
            }
/*
            PlatformLogger logger = Logging.getFocusLogger();
            if (logger.isLoggable(Level.FINE)) {
                if (value == get()) {
                    logger.fine("Changed focus from "
                            + localOldOwner + " to " + value);
                } else {
                    logger.fine("Changing focus from "
                            + localOldOwner + " to " + value
                            + " canceled by nested requestFocus");
                }
            }
            if (accessible != null) {
                accessible.sendNotification(AccessibleAttribute.FOCUS_NODE);
            }
*/
        }
    };

    public final Node getFocusOwner() {
        return focusOwner.getValue();
    }

    public final Property<Node> focusOwnerProperty() {
        return focusOwner/*.getReadOnlyProperty()*/;
    }

    private Node retryingRequestFocusNode;
    void requestFocus(Node node) {
        //getKeyHandler().requestFocus(node); // No KeyHandler emulated, so was inlined below:
        if (getFocusOwner() == node || (node != null && !node.isCanReceiveFocus())) {
            // Retrying a second time later in case the reason the focus is refused is because it is not tree visible whereas it is but the peer is not yet added to the dom
            if (retryingRequestFocusNode != node) {
                retryingRequestFocusNode = node;
                Platform.runLater(() -> requestFocus(node));
            }
            return;
        }
        //setFocusOwner(node); // From KeyHandler, so was replaced with:
        focusOwner.setValue(node);
    }

    /**
     * Gets the list of accelerators for this {@code Scene}.
     *
     * @return the list of accelerators
     */
    public ObservableMap<KeyCombination, Runnable> getAccelerators() {
        return getInternalEventDispatcher().getKeyboardShortcutsHandler()
                .getAccelerators();
    }


    /***************************************************************************
     *                                                                         *
     *                         Event Dispatch                                  *
     *                                                                         *
     **************************************************************************/
    // PENDING_DOC_REVIEW
    /**
     * Specifies the event dispatcher for this scene. When replacing the value
     * with a new {@code EventDispatcher}, the new dispatcher should forward
     * events to the replaced dispatcher to keep the scene's default event
     * handling behavior.
     */
    private ObjectProperty<EventDispatcher> eventDispatcher;

    public final void setEventDispatcher(EventDispatcher value) {
        eventDispatcherProperty().set(value);
    }

    public final EventDispatcher getEventDispatcher() {
        return eventDispatcherProperty().get();
    }

    public final ObjectProperty<EventDispatcher> eventDispatcherProperty() {
        initializeInternalEventDispatcher();
        return eventDispatcher;
    }

    private SceneEventDispatcher internalEventDispatcher;

    private SceneEventDispatcher getInternalEventDispatcher() {
        initializeInternalEventDispatcher();
        return internalEventDispatcher;
    }


    final void initializeInternalEventDispatcher() {
        if (internalEventDispatcher == null) {
            internalEventDispatcher = createInternalEventDispatcher();
            eventDispatcher = new SimpleObjectProperty<EventDispatcher>(
                    this,
                    "eventDispatcher",
                    internalEventDispatcher);
        }
    }

    private SceneEventDispatcher createInternalEventDispatcher() {
        return new SceneEventDispatcher(this);
    }

    /**
     * Construct an event dispatch chain for this scene. The event dispatch
     * chain contains all event dispatchers from the stage to this scene.
     *
     * @param tail the initial chain to build from
     * @return the resulting event dispatch chain for this scene
     */
    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        if (eventDispatcher != null) {
            final EventDispatcher eventDispatcherValue = eventDispatcher.get();
            if (eventDispatcherValue != null) {
                tail = tail.prepend(eventDispatcherValue);
            }
        }

        if (getWindow() != null) {
            tail = getWindow().buildEventDispatchChain(tail);
        }

        return tail;
    }
    /*************************************************************************
     *                                                                        *
     *                                                                        *
     *                                                                        *
     *************************************************************************/

    private static final Object USER_DATA_KEY = new Object();
    // A map containing a set of properties for this scene
    private ObservableMap<Object, Object> properties;

    /**
     * Returns an observable map of properties on this node for use primarily
     * by application developers.
     *
     * @return an observable map of properties on this node for use primarily
     * by application developers
     *
     * @since JavaFX 8u40
     */
    public final ObservableMap<Object, Object> getProperties() {
        if (properties == null) {
            properties = FXCollections.observableMap(new HashMap<Object, Object>());
        }
        return properties;
    }

    /**
     * Tests if Scene has properties.
     * @return true if node has properties.
     *
     * @since JavaFX 8u40
     */
    public boolean hasProperties() {
        return properties != null && !properties.isEmpty();
    }

    /**
     * Convenience method for setting a single Object property that can be
     * retrieved at a later date. This is functionally equivalent to calling
     * the getProperties().put(Object key, Object value) method. This can later
     * be retrieved by calling {@link Scene#getUserData()}.
     *
     * @param value The value to be stored - this can later be retrieved by calling
     *          {@link Scene#getUserData()}.
     *
     * @since JavaFX 8u40
     */
    public void setUserData(Object value) {
        getProperties().put(USER_DATA_KEY, value);
    }

    /**
     * Returns a previously set Object property, or null if no such property
     * has been set using the {@link Scene#setUserData(java.lang.Object)} method.
     *
     * @return The Object that was previously set, or null if no property
     *          has been set or if null was set.
     *
     * @since JavaFX 8u40
     */
    public Object getUserData() {
        return getProperties().get(USER_DATA_KEY);
    }



    private final SceneRequester sceneRequester = new SceneRequester() {

        @Override
        public void requestNodePeerPropertyUpdate(Node node, ObservableValue changedProperty) {
            executePropertyChange(() -> updateViewProperty(node, changedProperty));
        }

        @Override
        public void requestNodePeerListUpdate(Node node, ObservableList changedList, ListChangeListener.Change change) {
            if (change != null && !UiScheduler.isAnimationFrameNow())
                change = new SnapshotChange(change);
            ListChangeListener.Change finalChange = change;
            executePropertyChange(() -> updateViewList(node, changedList, finalChange));
        }

        private void executePropertyChange(Runnable runnable) {
            if (UiScheduler.isAnimationFrameNow())
                runnable.run();
            else
                UiScheduler.schedulePropertyChangeInNextAnimationFrame(runnable);
        }
    };

    public SceneRequester getSceneRequester() {
        return sceneRequester;
    }

    private void keepParentAndChildrenPeersUpdated(Parent parent) {
        ObservableLists.runNowAndOnListChange(c -> {
            // Setting the parent to all children
            for (Node child : parent.getChildren())
                child.setParent(parent);
            updateParentAndChildrenPeers(parent, (ListChangeListener.Change<Node>) c);
        }, parent.getChildren());
    }

    private void updateParentAndChildrenPeers(Parent parent, ListChangeListener.Change<Node> childrenChange) {
        impl_getPeer().updateParentAndChildrenPeers(parent, childrenChange);
    }

    private boolean updateViewProperty(Node node, ObservableValue changedProperty) {
        boolean hitChangedProperty = updateViewProperty(getOrCreateAndBindNodePeer(node), changedProperty);
        if (hitChangedProperty || changedProperty == null)
            impl_getPeer().onPropertyHit();
        return hitChangedProperty;
    }

    private boolean updateViewProperty(NodePeer nodePeer, ObservableValue changedProperty) {
        return nodePeer.updateProperty(changedProperty);
    }

    private boolean updateViewList(Node node, ObservableList changedList, ListChangeListener.Change change) {
        return updateViewList(getOrCreateAndBindNodePeer(node), changedList, change);
    }

    private boolean updateViewList(NodePeer nodePeer, ObservableList changedList, ListChangeListener.Change change) {
        return nodePeer.updateList(changedList, change);
    }

    public void updateChildrenPeers(Collection<Node> nodes) {
        Collections.forEach(nodes, this::createAndBindNodePeerAndChildren);
    }

    private void createAndBindRootNodePeerAndChildren(Node rootNode) {
        createAndBindNodePeerAndChildren(rootNode);
        impl_getPeer().onRootBound();
    }

    void createAndBindNodePeerAndChildren(Node node) {
        NodePeer nodePeer = getOrCreateAndBindNodePeer(node);
        if (nodePeer instanceof Parent)
            updateChildrenPeers(((Parent) nodePeer).getChildren());
    }

    public NodePeer getOrCreateAndBindNodePeer(Node node) {
        if (node.getScene() != this)
            node.setScene(this);
        NodePeer nodePeer = node.getNodePeer();
        if (nodePeer == null) {
            node.setNodePeer(nodePeer = createNodePeer(node));
            if (nodePeer == null) // The node view factory was unable to create a view for this node!
                node.setNodePeer(nodePeer = createUnimplementedNodePeer(node)); // Displaying a "Unimplemented..." button instead
            else { // Standard case (the node view was successfully created)
                nodePeer.bind(node, sceneRequester);
                if (node instanceof Parent)
                    Properties.onPropertySet((ObservableValue) node.getProperties().get("skinProperty"),
                            skin -> keepParentAndChildrenPeersUpdated((Parent) node), true);
            }
        }
        return nodePeer;
    }


    private NodePeer<Node> createNodePeer(Node node) {
        ScenePeer scenePeer = impl_getPeer();
        NodePeer<Node> nodePeer = FxKitMapper.createNodePeer(node);
        scenePeer.onNodePeerCreated(nodePeer);
        return nodePeer;
    }

    private NodePeer createUnimplementedNodePeer(Node node) {
        return getOrCreateAndBindNodePeer(createUnimplementedNodeReplacer(node));
    }

    private Node createUnimplementedNodeReplacer(Node node) {
        // Creating a rectangle as replacement (assuming the node peer factory at least implements a rectangle peer!)
        Rectangle nodeReplacer = new Rectangle(10, 10, Color.LIGHTGRAY);
        // TODO: add a text within the rectangle with the following message
        //String message = Strings.removeSuffix(node.getClass().getSimpleName(), "Impl") + " peer not provided";
        // Binding to allow the button to respond to the original node layout
        nodeReplacer.layoutXProperty().bind(node.layoutXProperty());
        nodeReplacer.layoutYProperty().bind(node.layoutYProperty());
        if (node instanceof HasWidthProperty)
            nodeReplacer.widthProperty().bind(((HasWidthProperty) node).widthProperty());
        if (node instanceof HasHeightProperty)
            nodeReplacer.heightProperty().bind(((HasHeightProperty) node).heightProperty());
        return nodeReplacer;
    }

    private Scheduled pulseScheduled;

    public boolean isPulseRunning() {
        return pulseScheduled != null;
    }

    public void startPulse() {
        if (pulseScheduled == null)
            pulseScheduled = UiScheduler.schedulePeriodicInAnimationFrame(scenePulseListener::pulse, AnimationFramePass.SCENE_PULSE_LAYOUT_PASS);
    }

    public void stopPulse() {
        if (pulseScheduled != null) {
            pulseScheduled.cancel();
            pulseScheduled = null;
        }
    }

    private void doCSSPass() {
    }

    private void doLayoutPass() {
        Parent root = getRoot();
        if (root != null)
            root.layout();
    }

    /**
     * The peer of this scene
     *
     * @treatAsPrivate implementation detail
     */
    //@Deprecated
    private ScenePeer impl_peer;

    /**
     * Get Scene's peer
     *
     * @treatAsPrivate implementation detail
     */
    //@Deprecated
    public ScenePeer impl_getPeer() {
        if (impl_peer == null)
            impl_peer = FxKitMapper.getProvider().createScenePeer(this);
        return impl_peer;
    }

    /**
     * @treatAsPrivate implementation detail
     */
    //@Deprecated
    public void impl_initPeer() {
        //assert impl_peer == null;

        Window window = getWindow();
        // impl_initPeer() is only called from Window, either when the window
        // is being shown, or the window scene is being changed. In any case
        // this scene's window cannot be null.
        assert window != null;

        StagePeer windowPeer = window.impl_getPeer();
        if (windowPeer == null) {
            // This is fine, the window is not visible. impl_initPeer() will
            // be called again later, when the window is being shown.
            return;
        }

/*
        final boolean isTransparentWindowsSupported = Platform.isSupported(ConditionalFeature.TRANSPARENT_WINDOW);
        if (!isTransparentWindowsSupported) {
            PlatformImpl.addNoTransparencyStylesheetToScene(this);
        }

        PerformanceTracker.logEvent("Scene.initPeer started");

        impl_setAllowPGAccess(true);

        Toolkit tk = Toolkit.getToolkit();
*/
        //impl_peer = windowPeer.createTKScene(isDepthBufferInternal(), getAntiAliasingInternal(), acc);
        //PerformanceTracker.logEvent("Scene.initPeer TKScene created");
        impl_peer.setTKSceneListener(new ScenePeerListener());
        //impl_peer.setTKScenePaintListener(new ScenePeerPaintListener());
        //PerformanceTracker.logEvent("Scene.initPeer TKScene set");
        //impl_peer.setRoot(getRoot().impl_getPeer());
        //impl_peer.setFillPaint(getFill() == null ? null : tk.getPaint(getFill()));
        //getEffectiveCamera().impl_updatePeer();
        //impl_peer.setCamera((NGCamera) getEffectiveCamera().impl_getPeer());
        //impl_peer.markDirty();
        //PerformanceTracker.logEvent("Scene.initPeer TKScene initialized");

        //impl_setAllowPGAccess(false);

        if (getRoot() != null)
            getRoot().requestLayout();
        startPulse(); // tk.addSceneTkPulseListener(scenePulseListener);
/*
        // listen to dnd gestures coming from the platform
        if (PLATFORM_DRAG_GESTURE_INITIATION) {
            if (dragGestureListener == null) {
                dragGestureListener = new DragGestureListener();
            }
            tk.registerDragGestureListener(impl_peer, EnumSet.allOf(TransferMode.class), dragGestureListener);
        }
        tk.enableDrop(impl_peer, new DropTargetListener());
        tk.installInputMethodRequests(impl_peer, new InputMethodRequestsDelegate());

        PerformanceTracker.logEvent("Scene.initPeer finished");
*/
    }

    /**
     * @treatAsPrivate implementation detail
     */
    //@Deprecated
    public void impl_disposePeer() {
        if (impl_peer == null) {
            // This is fine, the window is either not shown yet and there is no
            // need in disposing scene peer, or is hidden and impl_disposePeer()
            // has already been called.
            return;
        }

/*
        PerformanceTracker.logEvent("Scene.disposePeer started");

        Toolkit tk = Toolkit.getToolkit();
*/
        stopPulse(); //tk.removeSceneTkPulseListener(scenePulseListener);
/*
        if (accessible != null) {
            disposeAccessibles();
            Node root = getRoot();
            if (root != null) root.releaseAccessible();
            accessible.dispose();
            accessible = null;
        }
        impl_peer.dispose();
        impl_peer = null;

        PerformanceTracker.logEvent("Scene.disposePeer finished");
*/
    }


    /**
     * The scene pulse listener that gets called on toolkit pulses
     */
    ScenePulseListener scenePulseListener = new ScenePulseListener();

    /*******************************************************************************
     *                                                                             *
     * Scene Pulse Listener                                                        *
     *                                                                             *
     ******************************************************************************/

    class ScenePulseListener implements TKPulseListener {

        //private boolean firstPulse = true;

        /**
         * PG synchronizer. Called once per frame from the pulse listener.
         * This function calls the synchronizePGNode method on each node in
         * the dirty list.
         */
/*
        private void synchronizeSceneNodes() {
            Toolkit.getToolkit().checkFxUserThread();

            Scene.inSynchronizer = true;

            // if dirtyNodes is null then that means this Scene has not yet been
            // synchronized, and so we will simply synchronize every node in the
            // scene and then create the dirty nodes array list
            if (Scene.this.dirtyNodes == null) {
                // must do this recursively
                syncAll(getRoot());
                dirtyNodes = new Node[MIN_DIRTY_CAPACITY];

            } else {
                // This is not the first time this scene has been synchronized,
                // so we will only synchronize those nodes that need it
                for (int i = 0 ; i < dirtyNodesSize; ++i) {
                    Node node = dirtyNodes[i];
                    dirtyNodes[i] = null;
                    if (node.getScene() == Scene.this) {
                        node.impl_syncPeer();
                    }
                }
                dirtyNodesSize = 0;
            }

            Scene.inSynchronizer = false;
        }
*/

        /**
         * Recursive function for synchronizing every node in the scenegraph.
         * The return value is the number of nodes in the graph.
         */
/*
        private int syncAll(Node node) {
            node.impl_syncPeer();
            int size = 1;
            if (node instanceof Parent) {
                Parent p = (Parent) node;
                final int childrenCount = p.getChildren().size();

                for (int i = 0; i < childrenCount; i++) {
                    Node n = p.getChildren().get(i);
                    if (n != null) {
                        size += syncAll(n);
                    }
                }
            } else if (node instanceof SubScene) {
                SubScene subScene = (SubScene)node;
                size += syncAll(subScene.getRoot());
            }
            if (node.getClip() != null) {
                size += syncAll(node.getClip());
            }

            return size;
        }
*/

/*
        private void synchronizeSceneProperties() {
            inSynchronizer = true;
            if (isDirty(DirtyBits.ROOT_DIRTY)) {
                impl_peer.setRoot(getRoot().impl_getPeer());
            }

            if (isDirty(DirtyBits.FILL_DIRTY)) {
                Toolkit tk = Toolkit.getToolkit();
                impl_peer.setFillPaint(getFill() == null ? null : tk.getPaint(getFill()));
            }

            // new camera was set on the scene or old camera changed
            final Camera cam = getEffectiveCamera();
            if (isDirty(DirtyBits.CAMERA_DIRTY)) {
                cam.impl_updatePeer();
                impl_peer.setCamera((NGCamera) cam.impl_getPeer());
            }

            if (isDirty(DirtyBits.CURSOR_DIRTY)) {
                mouseHandler.updateCursor(getCursor());
            }

            clearDirty();
            inSynchronizer = false;
        }
*/

        /**
         * The focus is considered dirty if something happened to
         * the scene graph that may require the focus to be moved.
         * This must handle cases where (a) the focus owner may have
         * become ineligible to have the focus, and (b) where the focus
         * owner is null and a node may have become traversable and eligible.
         */
/*
        private void focusCleanup() {
            if (Scene.this.isFocusDirty()) {
                final Node oldOwner = Scene.this.getFocusOwner();
                if (oldOwner == null) {
                    Scene.this.focusInitial();
                } else if (oldOwner.getScene() != Scene.this) {
                    Scene.this.requestFocus(null);
                    Scene.this.focusInitial();
                } else if (!oldOwner.isCanReceiveFocus()) {
                    Scene.this.requestFocus(null);
                    Scene.this.focusIneligible(oldOwner);
                }
                Scene.this.setFocusDirty(false);
            }
        }
*/

        @Override
        public void pulse() {
            impl_getPeer().onBeforePulse();

/*
            if (Scene.this.tracker != null) {
                Scene.this.tracker.pulse();
            }
            if (firstPulse) {
                PerformanceTracker.logEvent("Scene - first repaint");
            }

            focusCleanup();

            disposeAccessibles();

            if (PULSE_LOGGING_ENABLED) {
                PulseLogger.newPhase("CSS Pass");
            }
*/
            Scene.this.doCSSPass();

/*
            if (PULSE_LOGGING_ENABLED) {
                PulseLogger.newPhase("Layout Pass");
            }
*/
            Scene.this.doLayoutPass();

/*
            boolean dirty = dirtyNodes == null || dirtyNodesSize != 0 || !isDirtyEmpty();
            if (dirty) {
                if (PULSE_LOGGING_ENABLED) {
                    PulseLogger.newPhase("Update bounds");
                }
                getRoot().updateBounds();
                if (impl_peer != null) {
                    try {
                        if (PULSE_LOGGING_ENABLED) {
                            PulseLogger.newPhase("Waiting for previous rendering");
                        }
                        impl_peer.waitForRenderingToComplete();
                        impl_peer.waitForSynchronization();
                        // synchronize scene properties
                        if (PULSE_LOGGING_ENABLED) {
                            PulseLogger.newPhase("Copy state to render graph");
                        }
                        syncLights();
                        synchronizeSceneProperties();
                        // Run the synchronizer
                        synchronizeSceneNodes();
                        Scene.this.mouseHandler.pulse();
                        // Tell the scene peer that it needs to repaint
                        impl_peer.markDirty();
                    } finally {
                        impl_peer.releaseSynchronization(true);
                    }
                } else {
                    if (PULSE_LOGGING_ENABLED) {
                        PulseLogger.newPhase("Synchronize with null peer");
                    }
                    synchronizeSceneNodes();
                    Scene.this.mouseHandler.pulse();
                }

                if (Scene.this.getRoot().cssFlag != CssFlags.CLEAN) {
                    Scene.this.getRoot().impl_markDirty(com.sun.javafx.scene.DirtyBits.NODE_CSS);
                }
            }
*/

/*
            // required for image cursor created from animated image
            Scene.this.mouseHandler.updateCursorFrame();

            if (firstPulse) {
                if (PerformanceTracker.isLoggingEnabled()) {
                    PerformanceTracker.logEvent("Scene - first repaint - layout complete");
                    if (PrismSettings.perfLogFirstPaintFlush) {
                        PerformanceTracker.outputLog();
                    }
                    if (PrismSettings.perfLogFirstPaintExit) {
                        System.exit(0);
                    }
                }
                firstPulse = false;
            }

            if (testPulseListener != null) {
                testPulseListener.run();
            }
*/
            impl_getPeer().onAfterPulse();
        }
    }


    /*******************************************************************************
     *                                                                             *
     * Scene Peer Listener                                                         *
     *                                                                             *
     ******************************************************************************/

    class ScenePeerListener implements TKSceneListener {
        @Override
        public void changedLocation(float x, float y) {
            if (x != Scene.this.getX()) {
                Scene.this.setX(x);
            }
            if (y != Scene.this.getY()) {
                Scene.this.setY(y);
            }
        }

        @Override
        public void changedSize(float w, float h) {
            if (w != Scene.this.getWidth()) Scene.this.setWidth((double)w);
            if (h != Scene.this.getHeight()) Scene.this.setHeight((double)h);
        }

/*
        @Override
        public void mouseEvent(EventType<MouseEvent> type, double x, double y, double screenX, double screenY,
                               MouseButton button, boolean popupTrigger, boolean synthesized,
                               boolean shiftDown, boolean controlDown, boolean altDown, boolean metaDown,
                               boolean primaryDown, boolean middleDown, boolean secondaryDown)
        {
            MouseEvent mouseEvent = new MouseEvent(type, x, y, screenX, screenY, button,
                    0, // click count will be adjusted by clickGenerator later anyway
                    shiftDown, controlDown, altDown, metaDown,
                    primaryDown, middleDown, secondaryDown, synthesized, popupTrigger, false, null);
            impl_processMouseEvent(mouseEvent);
        }

        @Override
        public void keyEvent(KeyEvent keyEvent)
        {
            impl_processKeyEvent(keyEvent);
        }

        @Override
        public void inputMethodEvent(EventType<InputMethodEvent> type,
                                     ObservableList<InputMethodTextRun> composed, String committed,
                                     int caretPosition)
        {
            InputMethodEvent inputMethodEvent = new InputMethodEvent(
                    type, composed, committed, caretPosition);
            processInputMethodEvent(inputMethodEvent);
        }

        public void menuEvent(double x, double y, double xAbs, double yAbs,
                              boolean isKeyboardTrigger) {
            Scene.this.processMenuEvent(x, y, xAbs,yAbs, isKeyboardTrigger);
        }

        @Override
        public void scrollEvent(
                EventType<ScrollEvent> eventType,
                double scrollX, double scrollY,
                double totalScrollX, double totalScrollY,
                double xMultiplier, double yMultiplier,
                int touchCount,
                int scrollTextX, int scrollTextY,
                int defaultTextX, int defaultTextY,
                double x, double y, double screenX, double screenY,
                boolean _shiftDown, boolean _controlDown,
                boolean _altDown, boolean _metaDown,
                boolean _direct, boolean _inertia) {

            ScrollEvent.HorizontalTextScrollUnits xUnits = scrollTextX > 0 ?
                    ScrollEvent.HorizontalTextScrollUnits.CHARACTERS :
                    ScrollEvent.HorizontalTextScrollUnits.NONE;

            double xText = scrollTextX < 0 ? 0 : scrollTextX * scrollX;

            ScrollEvent.VerticalTextScrollUnits yUnits = scrollTextY > 0 ?
                    ScrollEvent.VerticalTextScrollUnits.LINES :
                    (scrollTextY < 0 ?
                            ScrollEvent.VerticalTextScrollUnits.PAGES :
                            ScrollEvent.VerticalTextScrollUnits.NONE);

            double yText = scrollTextY < 0 ? scrollY : scrollTextY * scrollY;

            xMultiplier = defaultTextX > 0 && scrollTextX >= 0
                    ? Math.round(xMultiplier * scrollTextX / defaultTextX)
                    : xMultiplier;

            yMultiplier = defaultTextY > 0 && scrollTextY >= 0
                    ? Math.round(yMultiplier * scrollTextY / defaultTextY)
                    : yMultiplier;

            if (eventType == ScrollEvent.SCROLL_FINISHED) {
                x = scrollGesture.sceneCoords.getX();
                y = scrollGesture.sceneCoords.getY();
                screenX = scrollGesture.screenCoords.getX();
                screenY = scrollGesture.screenCoords.getY();
            } else if (Double.isNaN(x) || Double.isNaN(y) ||
                    Double.isNaN(screenX) || Double.isNaN(screenY)) {
                if (cursorScenePos == null || cursorScreenPos == null) {
                    return;
                }
                x = cursorScenePos.getX();
                y = cursorScenePos.getY();
                screenX = cursorScreenPos.getX();
                screenY = cursorScreenPos.getY();
            }

            inMousePick = true;
            Scene.this.processGestureEvent(new ScrollEvent(
                            eventType,
                            x, y, screenX, screenY,
                            _shiftDown, _controlDown, _altDown, _metaDown,
                            _direct, _inertia,
                            scrollX * xMultiplier, scrollY * yMultiplier,
                            totalScrollX * xMultiplier, totalScrollY * yMultiplier,
                            xMultiplier, yMultiplier,
                            xUnits, xText, yUnits, yText, touchCount, pick(x, y)),
                    scrollGesture);
            inMousePick = false;
        }

        @Override
        public void zoomEvent(
                EventType<ZoomEvent> eventType,
                double zoomFactor, double totalZoomFactor,
                double x, double y, double screenX, double screenY,
                boolean _shiftDown, boolean _controlDown,
                boolean _altDown, boolean _metaDown,
                boolean _direct, boolean _inertia) {

            if (eventType == ZoomEvent.ZOOM_FINISHED) {
                x = zoomGesture.sceneCoords.getX();
                y = zoomGesture.sceneCoords.getY();
                screenX = zoomGesture.screenCoords.getX();
                screenY = zoomGesture.screenCoords.getY();
            } else if (Double.isNaN(x) || Double.isNaN(y) ||
                    Double.isNaN(screenX) || Double.isNaN(screenY)) {
                if (cursorScenePos == null || cursorScreenPos == null) {
                    return;
                }
                x = cursorScenePos.getX();
                y = cursorScenePos.getY();
                screenX = cursorScreenPos.getX();
                screenY = cursorScreenPos.getY();
            }

            inMousePick = true;
            Scene.this.processGestureEvent(new ZoomEvent(eventType,
                            x, y, screenX, screenY,
                            _shiftDown, _controlDown, _altDown, _metaDown,
                            _direct, _inertia,
                            zoomFactor, totalZoomFactor, pick(x, y)),
                    zoomGesture);
            inMousePick = false;
        }

        @Override
        public void rotateEvent(
                EventType<RotateEvent> eventType, double angle, double totalAngle,
                double x, double y, double screenX, double screenY,
                boolean _shiftDown, boolean _controlDown,
                boolean _altDown, boolean _metaDown,
                boolean _direct, boolean _inertia) {

            if (eventType == RotateEvent.ROTATION_FINISHED) {
                x = rotateGesture.sceneCoords.getX();
                y = rotateGesture.sceneCoords.getY();
                screenX = rotateGesture.screenCoords.getX();
                screenY = rotateGesture.screenCoords.getY();
            } else if (Double.isNaN(x) || Double.isNaN(y) ||
                    Double.isNaN(screenX) || Double.isNaN(screenY)) {
                if (cursorScenePos == null || cursorScreenPos == null) {
                    return;
                }
                x = cursorScenePos.getX();
                y = cursorScenePos.getY();
                screenX = cursorScreenPos.getX();
                screenY = cursorScreenPos.getY();
            }

            inMousePick = true;
            Scene.this.processGestureEvent(new RotateEvent(
                            eventType, x, y, screenX, screenY,
                            _shiftDown, _controlDown, _altDown, _metaDown,
                            _direct, _inertia, angle, totalAngle, pick(x, y)),
                    rotateGesture);
            inMousePick = false;

        }

        @Override
        public void swipeEvent(
                EventType<SwipeEvent> eventType, int touchCount,
                double x, double y, double screenX, double screenY,
                boolean _shiftDown, boolean _controlDown,
                boolean _altDown, boolean _metaDown, boolean _direct) {

            if (Double.isNaN(x) || Double.isNaN(y) ||
                    Double.isNaN(screenX) || Double.isNaN(screenY)) {
                if (cursorScenePos == null || cursorScreenPos == null) {
                    return;
                }
                x = cursorScenePos.getX();
                y = cursorScenePos.getY();
                screenX = cursorScreenPos.getX();
                screenY = cursorScreenPos.getY();
            }

            inMousePick = true;
            Scene.this.processGestureEvent(new SwipeEvent(
                            eventType, x, y, screenX, screenY,
                            _shiftDown, _controlDown, _altDown, _metaDown, _direct,
                            touchCount, pick(x, y)),
                    swipeGesture);
            inMousePick = false;
        }

        @Override
        public void touchEventBegin(
                long time, int touchCount, boolean isDirect,
                boolean _shiftDown, boolean _controlDown,
                boolean _altDown, boolean _metaDown) {

            if (!isDirect) {
                nextTouchEvent = null;
                return;
            }
            nextTouchEvent = new TouchEvent(
                    TouchEvent.ANY, null, null, 0,
                    _shiftDown, _controlDown, _altDown, _metaDown);
            if (touchPoints == null || touchPoints.length != touchCount) {
                touchPoints = new TouchPoint[touchCount];
            }
            touchPointIndex = 0;
        }

        @Override
        public void touchEventNext(
                TouchPoint.State state, long touchId,
                double x, double y, double screenX, double screenY) {

            inMousePick = true;
            if (nextTouchEvent == null) {
                // ignore indirect touch events
                return;
            }
            touchPointIndex++;
            int id = (state == TouchPoint.State.PRESSED
                    ? touchMap.add(touchId) :  touchMap.get(touchId));
            if (state == TouchPoint.State.RELEASED) {
                touchMap.remove(touchId);
            }
            int order = touchMap.getOrder(id);

            if (order >= touchPoints.length) {
                throw new RuntimeException("Too many touch points reported");
            }

            // pick target
            boolean isGrabbed = false;
            PickResult pickRes = pick(x, y);
            EventTarget pickedTarget = touchTargets.get(id);
            if (pickedTarget == null) {
                pickedTarget = pickRes.getIntersectedNode();
                if (pickedTarget == null) {
                    pickedTarget = Scene.this;
                }
            } else {
                isGrabbed = true;
            }

            TouchPoint tp = new TouchPoint(id, state,
                    x, y, screenX, screenY, pickedTarget, pickRes);

            touchPoints[order] = tp;

            if (isGrabbed) {
                tp.grab(pickedTarget);
            }
            if (tp.getState() == TouchPoint.State.PRESSED) {
                tp.grab(pickedTarget);
                touchTargets.put(tp.getId(), pickedTarget);
            } else if (tp.getState() == TouchPoint.State.RELEASED) {
                touchTargets.remove(tp.getId());
            }
            inMousePick = false;
        }

        @Override
        public void touchEventEnd() {
            if (nextTouchEvent == null) {
                // ignore indirect touch events
                return;
            }

            if (touchPointIndex != touchPoints.length) {
                throw new RuntimeException("Wrong number of touch points reported");
            }

            Scene.this.processTouchEvent(nextTouchEvent, touchPoints);

            if (touchMap.cleanup()) {
                // gesture finished
                touchEventSetId = 0;
            }
        }

        @Override
        public Accessible getSceneAccessible() {
            return getAccessible();
        }
*/
    }


    /*******************************************************************************
     *                                                                             *
     * Mouse Event Handling                                                        *
     *                                                                             *
     ******************************************************************************/

    // mouse events handling
    private MouseHandler mouseHandler;
    private ClickGenerator clickGenerator;

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    // SB-dependency: RT-22747 has been filed to track this
    @Deprecated
    public void impl_processMouseEvent(MouseEvent e) {
        mouseHandler.process(e, false);
    }


    static class ClickCounter {
        //Toolkit toolkit = Toolkit.getToolkit();
        private int count;
        private boolean out;
        private boolean still;
        private Timeline timeout;
        private double pressedX, pressedY;

        private void inc() { count++; }
        private int get() { return count; }
        private boolean isStill() { return still; }

        private void clear() {
            count = 0;
            stopTimeout();
        }

        private void out() {
            out = true;
            stopTimeout();
        }

        private void applyOut() {
            if (out) clear();
            out = false;
        }

        private void moved(double x, double y) {
            if (Math.abs(x - pressedX) > 10 /*toolkit.getMultiClickMaxX()*/ ||
                    Math.abs(y - pressedY) > 10 /*toolkit.getMultiClickMaxY()*/) {
                out();
                still = false;
            }
        }

        private void start(double x, double y) {
            pressedX = x;
            pressedY = y;
            out = false;

            if (timeout != null) {
                timeout.stop();
            }
            timeout = new Timeline();
            timeout.getKeyFrames().add(
                    new KeyFrame(new Duration(500/*toolkit.getMultiClickTime()*/),
                            event -> {
                                out = true;
                                timeout = null;
                            }
                    ));
            timeout.play();
            still = true;
        }

        private void stopTimeout() {
            if (timeout != null) {
                timeout.stop();
                timeout = null;
            }
        }
    }

    static class ClickGenerator {
        private ClickCounter lastPress = null;

        private Map<MouseButton, ClickCounter> counters =
                new EnumMap<MouseButton, ClickCounter>(MouseButton.class);
        private List<EventTarget> pressedTargets = new ArrayList<EventTarget>();
        private List<EventTarget> releasedTargets = new ArrayList<EventTarget>();

        public ClickGenerator() {
            for (MouseButton mb : MouseButton.values()) {
                if (mb != MouseButton.NONE) {
                    counters.put(mb, new ClickCounter());
                }
            }
        }

        private MouseEvent preProcess(MouseEvent e) {
            for (ClickCounter cc : counters.values()) {
                cc.moved(e.getSceneX(), e.getSceneY());
            }

            ClickCounter cc = counters.get(e.getButton());
            boolean still = lastPress != null ? lastPress.isStill() : false;

            if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {

                if (! e.isPrimaryButtonDown()) { counters.get(MouseButton.PRIMARY).clear(); }
                if (! e.isSecondaryButtonDown()) { counters.get(MouseButton.SECONDARY).clear(); }
                if (! e.isMiddleButtonDown()) { counters.get(MouseButton.MIDDLE).clear(); }

                cc.applyOut();
                cc.inc();
                cc.start(e.getSceneX(), e.getSceneY());
                lastPress = cc;
            }

            return new MouseEvent(e.getEventType(), e.getSceneX(), e.getSceneY(),
                    e.getScreenX(), e.getScreenY(), e.getButton(),
                    cc != null && e.getEventType() != MouseEvent.MOUSE_MOVED ? cc.get() : 0,
                    e.isShiftDown(), e.isControlDown(), e.isAltDown(), e.isMetaDown(),
                    e.isPrimaryButtonDown(), e.isMiddleButtonDown(), e.isSecondaryButtonDown(),
                    e.isSynthesized(), e.isPopupTrigger(), still, e.getPickResult());
        }

        private void postProcess(MouseEvent e, TargetWrapper target, TargetWrapper pickedTarget) {

            if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
                ClickCounter cc = counters.get(e.getButton());

                target.fillHierarchy(pressedTargets);
                pickedTarget.fillHierarchy(releasedTargets);
                int i = pressedTargets.size() - 1;
                int j = releasedTargets.size() - 1;

                EventTarget clickedTarget = null;
                while (i >= 0 && j >= 0 && pressedTargets.get(i) == releasedTargets.get(j)) {
                    clickedTarget = pressedTargets.get(i);
                    i--;
                    j--;
                }

                pressedTargets.clear();
                releasedTargets.clear();

                if (clickedTarget != null && lastPress != null) {
                    MouseEvent click = new MouseEvent(null, clickedTarget,
                            MouseEvent.MOUSE_CLICKED, e.getSceneX(), e.getSceneY(),
                            e.getScreenX(), e.getScreenY(), e.getButton(),
                            cc.get(),
                            e.isShiftDown(), e.isControlDown(), e.isAltDown(), e.isMetaDown(),
                            e.isPrimaryButtonDown(), e.isMiddleButtonDown(), e.isSecondaryButtonDown(),
                            e.isSynthesized(), e.isPopupTrigger(), lastPress.isStill(), e.getPickResult());
                    Event.fireEvent(clickedTarget, click);
                }
            }
        }
    }

    /**
     * Generates mouse exited event for a node which is going to be removed
     * and its children, where appropriate.
     * @param removing Node which is going to be removed
     */
    void generateMouseExited(Node removing) {
        mouseHandler.handleNodeRemoval(removing);
    }

    // Reusable target wrapper (to avoid creating new one for each picking)
    private TargetWrapper tmpTargetWrapper = new TargetWrapper();

    class MouseHandler {
        private TargetWrapper pdrEventTarget = new TargetWrapper(); // pdr - press-drag-release
        private boolean pdrInProgress = false;
        private boolean fullPDREntered = false;

        private EventTarget currentEventTarget = null;
        private MouseEvent lastEvent;
        private boolean hover = false;

        private boolean primaryButtonDown = false;
        private boolean secondaryButtonDown = false;
        private boolean middleButtonDown = false;

        private EventTarget fullPDRSource = null;
        private TargetWrapper fullPDRTmpTargetWrapper = new TargetWrapper();

        /* lists needed for enter/exit events generation */
        private final List<EventTarget> pdrEventTargets = new ArrayList<EventTarget>();
        private final List<EventTarget> currentEventTargets = new ArrayList<EventTarget>();
        private final List<EventTarget> newEventTargets = new ArrayList<EventTarget>();

        private final List<EventTarget> fullPDRCurrentEventTargets = new ArrayList<EventTarget>();
        private final List<EventTarget> fullPDRNewEventTargets = new ArrayList<EventTarget>();
        private EventTarget fullPDRCurrentTarget = null;

        //private Cursor currCursor;
        //private CursorFrame currCursorFrame;
        private EventQueue queue = new EventQueue();

        private Runnable pickProcess = new Runnable() {

            @Override
            public void run() {
                // Make sure this is run only if the peer is still alive
                // and there is an event to deliver
                if (Scene.this.impl_peer != null && lastEvent != null) {
                    process(lastEvent, true);
                }
            }
        };

        private void pulse() {
            if (hover && lastEvent != null) {
                //Shouldn't run user code directly. User can call stage.showAndWait() and block the pulse.
                Platform.runLater(pickProcess);
            }
        }


        private void clearPDREventTargets() {
            pdrInProgress = false;
            currentEventTarget = currentEventTargets.size() > 0
                    ? currentEventTargets.get(0) : null;
            pdrEventTarget.clear();
        }

        public void enterFullPDR(EventTarget gestureSource) {
            fullPDREntered = true;
            fullPDRSource = gestureSource;
            fullPDRCurrentTarget = null;
            fullPDRCurrentEventTargets.clear();
        }

        public void exitFullPDR(MouseEvent e) {
            if (!fullPDREntered) {
                return;
            }
            fullPDREntered = false;
            for (int i = fullPDRCurrentEventTargets.size() - 1; i >= 0; i--) {
                EventTarget entered = fullPDRCurrentEventTargets.get(i);
                Event.fireEvent(entered, MouseEvent.copyForMouseDragEvent(e,
                        entered, entered,
                        MouseDragEvent.MOUSE_DRAG_EXITED_TARGET,
                        fullPDRSource, e.getPickResult()));
            }
            fullPDRSource = null;
            fullPDRCurrentEventTargets.clear();
            fullPDRCurrentTarget = null;
        }

        private void handleNodeRemoval(Node removing) {
            if (lastEvent == null) {
                // this can happen only if everything has been exited anyway
                return;
            }


            if (currentEventTargets.contains(removing)) {
                int i = 0;
                EventTarget trg = null;
                while (trg != removing) {
                    trg = currentEventTargets.get(i++);

                    queue.postEvent(lastEvent.copyFor(trg, trg,
                            MouseEvent.MOUSE_EXITED_TARGET));
                }
                currentEventTargets.subList(0, i).clear();
            }

            if (fullPDREntered && fullPDRCurrentEventTargets.contains(removing)) {
                int i = 0;
                EventTarget trg = null;
                while (trg != removing) {
                    trg = fullPDRCurrentEventTargets.get(i++);

                    queue.postEvent(
                            MouseEvent.copyForMouseDragEvent(lastEvent, trg, trg,
                                    MouseDragEvent.MOUSE_DRAG_EXITED_TARGET,
                                    fullPDRSource, lastEvent.getPickResult()));
                }

                fullPDRCurrentEventTargets.subList(0, i).clear();
            }

            queue.fire();

            if (pdrInProgress && pdrEventTargets.contains(removing)) {
                int i = 0;
                EventTarget trg = null;
                while (trg != removing) {
                    trg = pdrEventTargets.get(i++);

                    // trg.setHover(false) - already taken care of
                    // by the code above which sent a mouse exited event
                    ((Node) trg).setPressed(false);
                }
                pdrEventTargets.subList(0, i).clear();

                trg = pdrEventTargets.get(0);
                final PickResult res = pdrEventTarget.getResult();
                if (trg instanceof Node) {
                    pdrEventTarget.setNodeResult(new PickResult((Node) trg,
                            res.getIntersectedPoint()/*, res.getIntersectedDistance()*/));
                } else {
                    pdrEventTarget.setSceneResult(new PickResult(null,
                                    res.getIntersectedPoint()/*, res.getIntersectedDistance()*/),
                            (Scene) trg);
                }
            }
        }

        private void handleEnterExit(MouseEvent e, TargetWrapper pickedTarget) {
            if (pickedTarget.getEventTarget() != currentEventTarget ||
                    e.getEventType() == MouseEvent.MOUSE_EXITED) {

                if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
                    newEventTargets.clear();
                } else {
                    pickedTarget.fillHierarchy(newEventTargets);
                }

                int newTargetsSize = newEventTargets.size();
                int i = currentEventTargets.size() - 1;
                int j = newTargetsSize - 1;
                int k = pdrEventTargets.size() - 1;

                while (i >= 0 && j >= 0 && currentEventTargets.get(i) == newEventTargets.get(j)) {
                    i--;
                    j--;
                    k--;
                }

                final int memk = k;
                for (; i >= 0; i--, k--) {
                    final EventTarget exitedEventTarget = currentEventTargets.get(i);
                    if (pdrInProgress &&
                            (k < 0 || exitedEventTarget != pdrEventTargets.get(k))) {
                        break;
                    }
                    queue.postEvent(e.copyFor(
                            exitedEventTarget, exitedEventTarget,
                            MouseEvent.MOUSE_EXITED_TARGET));
                }

                k = memk;
                for (; j >= 0; j--, k--) {
                    final EventTarget enteredEventTarget = newEventTargets.get(j);
                    if (pdrInProgress &&
                            (k < 0 || enteredEventTarget != pdrEventTargets.get(k))) {
                        break;
                    }
                    queue.postEvent(e.copyFor(
                            enteredEventTarget, enteredEventTarget,
                            MouseEvent.MOUSE_ENTERED_TARGET));
                }

                currentEventTarget = pickedTarget.getEventTarget();
                currentEventTargets.clear();
                for (j++; j < newTargetsSize; j++) {
                    currentEventTargets.add(newEventTargets.get(j));
                }
            }
            queue.fire();
        }

        private void process(MouseEvent e, boolean onPulse) {
            //Toolkit.getToolkit().checkFxUserThread();
            //Scene.inMousePick = true;

            //cursorScreenPos = new Point2D(e.getScreenX(), e.getScreenY());
            //cursorScenePos = new Point2D(e.getSceneX(), e.getSceneY());

            boolean gestureStarted = false;
            if (!onPulse) {
                if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    if (!(primaryButtonDown || secondaryButtonDown || middleButtonDown)) {
                        //old gesture ended and new one started
                        gestureStarted = true;
/*
                        if (!PLATFORM_DRAG_GESTURE_INITIATION) {
                            Scene.this.dndGesture = new DnDGesture();
                        }
*/
                        clearPDREventTargets();
                    }
                } else if (e.getEventType() == MouseEvent.MOUSE_MOVED) {
                    // gesture ended
                    clearPDREventTargets();
                } else if (e.getEventType() == MouseEvent.MOUSE_ENTERED) {
                    hover = true;
                } else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
                    hover = false;
                }

                primaryButtonDown = e.isPrimaryButtonDown();
                secondaryButtonDown = e.isSecondaryButtonDown();
                middleButtonDown = e.isMiddleButtonDown();
            }


            pick(tmpTargetWrapper, e.getSceneX(), e.getSceneY());
            PickResult res = tmpTargetWrapper.getResult();
            if (res != null) {
                e = new MouseEvent(e.getEventType(), e.getSceneX(), e.getSceneY(),
                        e.getScreenX(), e.getScreenY(), e.getButton(), e.getClickCount(),
                        e.isShiftDown(), e.isControlDown(), e.isAltDown(), e.isMetaDown(),
                        e.isPrimaryButtonDown(), e.isMiddleButtonDown(), e.isSecondaryButtonDown(),
                        e.isSynthesized(), e.isPopupTrigger(), e.isStillSincePress(), res);
            }

            if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
                tmpTargetWrapper.clear();
            }

            TargetWrapper target;
            if (pdrInProgress) {
                target = pdrEventTarget;
            } else {
                target = tmpTargetWrapper;
            }

            if (gestureStarted) {
                pdrEventTarget.copy(target);
                pdrEventTarget.fillHierarchy(pdrEventTargets);
            }

            if (!onPulse) {
                e = clickGenerator.preProcess(e);
            }

            // enter/exit handling
            handleEnterExit(e, tmpTargetWrapper);

/*
            //deliver event to the target node
            if (Scene.this.dndGesture != null) {
                Scene.this.dndGesture.processDragDetection(e);
            }
*/

            if (fullPDREntered && e.getEventType() == MouseEvent.MOUSE_RELEASED) {
                processFullPDR(e, onPulse);
            }

            if (target.getEventTarget() != null) {
                if (e.getEventType() != MouseEvent.MOUSE_ENTERED
                        && e.getEventType() != MouseEvent.MOUSE_EXITED
                        && !onPulse) {
                    Event.fireEvent(target.getEventTarget(), e);
                }
            }

            if (fullPDREntered && e.getEventType() != MouseEvent.MOUSE_RELEASED) {
                processFullPDR(e, onPulse);
            }

            if (!onPulse) {
                clickGenerator.postProcess(e, target, tmpTargetWrapper);
            }

            // handle drag and drop

/*
            if (!PLATFORM_DRAG_GESTURE_INITIATION && !onPulse) {
                if (Scene.this.dndGesture != null) {
                    if (!Scene.this.dndGesture.process(e, target.getEventTarget())) {
                        dndGesture = null;
                    }
                }
            }
*/
/*
            Cursor cursor = target.getCursor();
            if (e.getEventType() != MouseEvent.MOUSE_EXITED) {
                if (cursor == null && hover) {
                    cursor = Scene.this.getCursor();
                }

                updateCursor(cursor);
                updateCursorFrame();
            }

*/

            if (gestureStarted) {
                pdrInProgress = true;
            }

            if (pdrInProgress &&
                    !(primaryButtonDown || secondaryButtonDown || middleButtonDown)) {
                clearPDREventTargets();
                exitFullPDR(e);
                // we need to do new picking in case the originally picked node
                // was moved or removed by the event handlers
                pick(tmpTargetWrapper, e.getSceneX(), e.getSceneY());
                handleEnterExit(e, tmpTargetWrapper);
            }

            lastEvent = e.getEventType() == MouseEvent.MOUSE_EXITED ? null : e;
            //Scene.inMousePick = false;
        }

        private void processFullPDR(MouseEvent e, boolean onPulse) {

            pick(fullPDRTmpTargetWrapper, e.getSceneX(), e.getSceneY());
            final PickResult result = fullPDRTmpTargetWrapper.getResult();

            final EventTarget eventTarget = fullPDRTmpTargetWrapper.getEventTarget();

            // enter/exit handling
            if (eventTarget != fullPDRCurrentTarget) {

                fullPDRTmpTargetWrapper.fillHierarchy(fullPDRNewEventTargets);

                int newTargetsSize = fullPDRNewEventTargets.size();
                int i = fullPDRCurrentEventTargets.size() - 1;
                int j = newTargetsSize - 1;

                while (i >= 0 && j >= 0 &&
                        fullPDRCurrentEventTargets.get(i) == fullPDRNewEventTargets.get(j)) {
                    i--;
                    j--;
                }

                for (; i >= 0; i--) {
                    final EventTarget exitedEventTarget = fullPDRCurrentEventTargets.get(i);
                    Event.fireEvent(exitedEventTarget, MouseEvent.copyForMouseDragEvent(e,
                            exitedEventTarget, exitedEventTarget,
                            MouseDragEvent.MOUSE_DRAG_EXITED_TARGET,
                            fullPDRSource, result));
                }

                for (; j >= 0; j--) {
                    final EventTarget enteredEventTarget = fullPDRNewEventTargets.get(j);
                    Event.fireEvent(enteredEventTarget, MouseEvent.copyForMouseDragEvent(e,
                            enteredEventTarget, enteredEventTarget,
                            MouseDragEvent.MOUSE_DRAG_ENTERED_TARGET,
                            fullPDRSource, result));
                }

                fullPDRCurrentTarget = eventTarget;
                fullPDRCurrentEventTargets.clear();
                fullPDRCurrentEventTargets.addAll(fullPDRNewEventTargets);
                fullPDRNewEventTargets.clear();
            }
            // done enter/exit handling

            // event delivery
            if (eventTarget != null && !onPulse) {
                if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    Event.fireEvent(eventTarget, MouseEvent.copyForMouseDragEvent(e,
                            eventTarget, eventTarget,
                            MouseDragEvent.MOUSE_DRAG_OVER,
                            fullPDRSource, result));
                }
                if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    Event.fireEvent(eventTarget, MouseEvent.copyForMouseDragEvent(e,
                            eventTarget, eventTarget,
                            MouseDragEvent.MOUSE_DRAG_RELEASED,
                            fullPDRSource, result));
                }
            }
        }
    }

    /*
     * This class represents a picked target - either node, or scne, or null.
     * It provides functionality needed for the targets and covers the fact
     * that they are different kinds of animals.
     */
    private static class TargetWrapper {
        private Scene scene;
        private Node node;
        private PickResult result;

        /**
         * Fills the list with the target and all its parents (including scene)
         */
        public void fillHierarchy(final List<EventTarget> list) {
            list.clear();
            Node n = node;
            while(n != null) {
                list.add(n);
                final Parent p = n.getParent();
                n = p != null ? p : null; //n.getSubScene();
            }

            if (scene != null) {
                list.add(scene);
            }
        }

        public EventTarget getEventTarget() {
            return node != null ? node : scene;
        }

        public Cursor getCursor() {
            Cursor cursor = null;
            if (node != null) {
                cursor = node.getCursor();
                Node n = node.getParent();
                while (cursor == null && n != null) {
                    cursor = n.getCursor();

                    final Parent p = n.getParent();
                    n = p != null ? p : null; //n.getSubScene();
                }
            }
            return cursor;
        }

        public void clear() {
            set(null, null);
            result = null;
        }

        public void setNodeResult(PickResult result) {
            if (result != null) {
                this.result = result;
                final Node n = result.getIntersectedNode();
                set(n, n.getScene());
            }
        }

        // Pass null scene if the mouse is outside of the window content
        public void setSceneResult(PickResult result, Scene scene) {
            if (result != null) {
                this.result = result;
                set(null, scene);
            }
        }

        public PickResult getResult() {
            return result;
        }

        public void copy(TargetWrapper tw) {
            node = tw.node;
            scene = tw.scene;
            result = tw.result;
        }

        private void set(Node n, Scene s) {
            node = n;
            scene = s;
        }
    }

    private void pick(TargetWrapper target, final double x, final double y) {
        NodePeer nodePeer = impl_getPeer().pickPeer(x, y);
        PickResult pickResult = nodePeer == null ? null : new PickResult(nodePeer.getNode(), x, y);
        target.setNodeResult(pickResult);
/*
        final PickRay pickRay = getEffectiveCamera().computePickRay(
                x, y, null);

        final double mag = pickRay.getDirectionNoClone().length();
        pickRay.getDirectionNoClone().normalize();
        final PickResult res = mouseHandler.pickNode(pickRay);
        if (res != null) {
            target.setNodeResult(res);
        } else {
            //TODO: is this the intersection with projection plane?
            Vec3d o = pickRay.getOriginNoClone();
            Vec3d d = pickRay.getDirectionNoClone();
            target.setSceneResult(new PickResult(
                            null, new Point3D(
                            o.x + mag * d.x,
                            o.y + mag * d.y,
                            o.z + mag * d.z),
                            mag),
                    isInScene(x, y) ? this : null);
        }
*/
    }

    /**
     * A ObservableList of string URLs linking to the stylesheets to use with this scene's
     * contents. For additional information about using CSS with the
     * scene graph, see the <a href="doc-files/cssref.html">CSS Reference
     * Guide</a>.
     */
    private final ObservableList<String> stylesheets = FXCollections.observableArrayList();

    /**
     * Gets an observable list of string URLs linking to the stylesheets to use
     * with this scene's contents.
     * <p>
     * The URL is a hierarchical URI of the form [scheme:][//authority][path]. If the URL
     * does not have a [scheme:] component, the URL is considered to be the [path] component only.
     * Any leading '/' character of the [path] is ignored and the [path] is treated as a path relative to
     * the root of the application's classpath.
     * </p>
     * <code><pre>
     *
     * package com.example.javafx.app;
     *
     * import javafx.application.Application;
     * import javafx.scene.Group;
     * import javafx.scene.Scene;
     * import javafx.stage.Stage;
     *
     * public class MyApp extends Application {
     *
     *     {@literal @}Override public void start(Stage stage) {
     *         Scene scene = new Scene(new Group());
     *         scene.getStylesheets().add("/com/example/javafx/app/mystyles.css");
     *         stage.setScene(scene);
     *         stage.show();
     *     }
     *
     *     public static void main(String[] args) {
     *         launch(args);
     *     }
     * }
     * </pre></code>
     * For additional information about using CSS with the scene graph,
     * see the <a href="doc-files/cssref.html">CSS Reference Guide</a>.
     *
     * @return the list of stylesheets to use with this scene
     */
    public final ObservableList<String> getStylesheets() { return stylesheets; }

    private static class SnapshotChange<E> extends SourceAdapterChange<E> {

        private final List<E> snapshotList;

        SnapshotChange(ListChangeListener.Change<E> change) {
            super(change.getList(), change);
            snapshotList = new ArrayList<>(change.getList());
        }

        @Override
        public List<E> getAddedSubList() {
            return wasAdded()? snapshotList.subList(getFrom(), getTo()) : java.util.Collections.emptyList();
        }
    }

}
