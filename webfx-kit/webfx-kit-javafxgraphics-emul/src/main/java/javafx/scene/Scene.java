package javafx.scene;

import com.sun.javafx.collections.SourceAdapterChange;
import com.sun.javafx.event.EventQueue;
import com.sun.javafx.scene.SceneEventDispatcher;
import com.sun.javafx.scene.input.InputEventUtils;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.SceneTraversalEngine;
import com.sun.javafx.scene.traversal.TopMostTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalMethod;
import com.sun.javafx.tk.TKPulseListener;
import com.sun.javafx.tk.TKSceneListener;
import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.kit.mapper.WebFxKitMapper;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.ScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.WindowPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.*;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.scheduler.Scheduled;
import dev.webfx.platform.uischeduler.AnimationFramePass;
import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.util.collection.Collections;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.*;
import javafx.geometry.Orientation;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.*;

/**
 * @author Bruno Salmon
 */
public class Scene implements EventTarget,
        HasRootProperty,
        HasWidthProperty,
        HasHeightProperty,
        HasFillProperty {

    private double widthSetByUser = -1.0;
    private double heightSetByUser = -1.0;

    public Scene(Parent root) {
        this(root, -1, -1);
    }

    public Scene(Parent root, double width, double height) {
        this(root, width, height, Color.WHITE);
    }

    public Scene(Parent root, Paint fill) {
        this(root, -1, -1, fill);
    }

    public Scene(Parent root, double width, double height, Paint fill) {
        setRoot(root);
        init(width, height);
        setFill(fill);
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

    private final DoubleProperty widthProperty = new SimpleDoubleProperty(0d) {
        @Override
        protected void invalidated() {
            Parent _root = getRoot();
            if (_root.isResizable()) {
                resizeRootOnSceneSizeChange(get() - _root.getLayoutX() /*- _root.getTranslateX()*/, _root.getLayoutBounds().getHeight());
            }
        }
    };

    @Override
    public DoubleProperty widthProperty() {
        return widthProperty;
    }

    private final DoubleProperty heightProperty = new SimpleDoubleProperty(0d) {
        @Override
        protected void invalidated() {
            Parent _root = getRoot();
            if (_root.isResizable()) {
                resizeRootOnSceneSizeChange(_root.getLayoutBounds().getWidth(), get() - _root.getLayoutY() /*- _root.getTranslateY()*/);
            }
        }
    };
    @Override
    public DoubleProperty heightProperty() {
        return heightProperty;
    }

    void resizeRootOnSceneSizeChange(double newWidth, double newHeight) {
        getRoot().resize(newWidth, newHeight);
    }

    /**
     * Defines the background fill of this {@code Scene}. Both a {@code null}
     * value meaning 'paint no background' and a {@link javafx.scene.paint.Paint}
     * with transparency are supported. The default fill of the Scene is
     * {@link Color#WHITE}, but it is more commonly the case that the initial
     * color shown to users is the background fill of the
     * {@link #rootProperty() root node} of the {@code Scene}, as it typically is
     * stretched to take up all available space in the {@code Scene}. The
     * root node of the {@code Scene} is given the CSS style class 'root', and
     * the default user agent stylesheets that ship with JavaFX (presently
     * Caspian and Modena) apply styling on to this root style class. In the
     * case of Caspian this does not impact the background fill color of the
     * root node, but in the case of Modena the default fill is set to be a
     * light gray color.
     *
     * @defaultValue WHITE
     */
    private ObjectProperty<Paint> fill;

    public final void setFill(Paint value) {
        fillProperty().set(value);
    }

    public final Paint getFill() {
        return fill == null ? Color.WHITE : fill.get();
    }

    public final ObjectProperty<Paint> fillProperty() {
        if (fill == null) {
            fill = new ObjectPropertyBase<Paint>(Color.WHITE) {

                /*@Override
                protected void invalidated() {
                    markDirty(DirtyBits.FILL_DIRTY);
                }*/

                @Override
                public Object getBean() {
                    return Scene.this;
                }

                @Override
                public String getName() {
                    return "fill";
                }
            };
        }
        return fill;
    }

    private final Property<Parent> rootProperty = new SimpleObjectProperty<Parent>() {
        // Temporary code to automatically assume the following behavior:
        // - the root node width is bound to the scene width
        // - the scene height is bound to the root node height (which eventually is bound to the preferred height)
        private Parent oldRoot;
        @Override
        protected void invalidated() {
            Parent root = getValue();
            root.setScene(Scene.this);
            root.setSceneRoot(true);
            resizeRootOnSceneSizeChange(getWidth(), getHeight()); // Necessary when root changed without scene being resized
            createAndBindRootNodePeerAndChildren(root);
            // Resetting the scene to null for the old root and its children
            if (oldRoot != null && oldRoot.getScene() == Scene.this)
                oldRoot.setScene(null);
            oldRoot = root;
        }
    };

    /**
     * The horizontal location of this {@code Scene} on the {@code Window}.
     */
    private DoubleProperty x;

    private final void setX(double value) {
        xPropertyImpl().setValue(value);
    }

    public final double getX() {
        return x == null ? 0.0 : x.getValue();
    }

    public final ReadOnlyDoubleProperty xProperty() {
        return xPropertyImpl()/*.getReadOnlyProperty()*/;
    }

    private DoubleProperty xPropertyImpl() {
        if (x == null) {
            x = new SimpleDoubleProperty(this, "x", 0d);
        }
        return x;
    }

    /**
     * The vertical location of this {@code Scene} on the {@code Window}.
     */
    private DoubleProperty y;

    private void setY(double value) {
        yPropertyImpl().setValue(value);
    }

    public final double getY() {
        return y == null ? 0.0 : y.getValue();
    }

    public final ReadOnlyDoubleProperty yProperty() {
        return yPropertyImpl()/*.getReadOnlyProperty()*/;
    }

    private DoubleProperty yPropertyImpl() {
        if (y == null) {
            y = new SimpleDoubleProperty(this, "y", 0d);
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
    private ObjectProperty<Window> window;

    private void setWindow(Window value) {
        windowPropertyImpl().setValue(value);
    }

    public final Window getWindow() {
        return window == null ? null : window.getValue();
    }

    public final ReadOnlyObjectProperty<Window> windowProperty() {
        return windowPropertyImpl()/*.getReadOnlyProperty()*/;
    }

    private ObjectProperty<Window> windowPropertyImpl() {
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

    public final void resizeRootToPreferredSize(Parent root) {
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
            //Console.log("JavaFX focusOwner " + oldFocusOwner + " -> " + get());
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
            // Retrying a second time later in case the reason the focus is refused is that it is not tree-visible whereas it is but the peer is not yet added to the dom
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

    /**
     * Defines the mouse cursor for this {@code Scene}.
     */
    private ObjectProperty<Cursor> cursor;

    public final void setCursor(Cursor value) {
        cursorProperty().set(value);
    }

    public final Cursor getCursor() {
        return cursor == null ? null : cursor.get();
    }

    public final ObjectProperty<Cursor> cursorProperty() {
        if (cursor == null) {
            cursor = new ObjectPropertyBase<Cursor>() {
                @Override
                protected void invalidated() {
                    //markCursorDirty();
                }

                @Override
                public Object getBean() {
                    return Scene.this;
                }

                @Override
                public String getName() {
                    return "cursor";
                }
            };
        }
        return cursor;
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

        private Scene getScene(Node node) {
            // Using preferably the scene finally set on the node in case the peer has been created and bound with a
            // different scene (ex: a temporary phantom scene - see Node.getOrCreateAndBindNodePeer()). In that case,
            // we redirect all calls to the final scene.
            Scene scene = node.getScene(); // The same as this scene for most of the case (except case explained above).
            // But if the node is still not inserted in the scene graph, we use the present scene instance for handling
            // the node properties changes
            return scene != null ? scene : Scene.this;
        }

        @Override
        public void requestNodePeerPropertyUpdate(Node node, ObservableValue changedProperty) {
            executePropertyChange(() -> getScene(node).updateViewProperty(node, changedProperty));
        }

        @Override
        public void requestNodePeerListUpdate(Node node, ObservableList changedList, ListChangeListener.Change change) {
            if (change != null && !UiScheduler.isAnimationFrameNow())
                change = new SnapshotChange(change);
            ListChangeListener.Change finalChange = change;
            executePropertyChange(() -> getScene(node).updateViewList(node, changedList, finalChange));
        }

        private void executePropertyChange(Runnable runnable) {
            if (UiScheduler.isAnimationFrameNow())
                runnable.run();
            else
                UiScheduler.schedulePropertyChangeInAnimationFrame(runnable);
        }
    };

    public SceneRequester getSceneRequester() {
        return sceneRequester;
    }

    void updateParentAndChildrenPeers(Parent parent, ListChangeListener.Change<? extends Node> childrenChange) {
        ScenePeer scenePeer = impl_getPeer();
        if (scenePeer != null && parent.getNodePeer() != null) {
            scenePeer.updateParentAndChildrenPeers(parent, childrenChange);
        }
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
        getOrCreateAndBindNodePeer(node);
        if (node instanceof Parent)
            updateChildrenPeers(((Parent) node).getChildren());
    }

    public NodePeer getOrCreateAndBindNodePeer(Node node) {
        NodePeer nodePeer = node.getNodePeer();
        if (nodePeer == null) {
            node.setNodePeer(nodePeer = createNodePeer(node));
            if (nodePeer == null) // The node view factory was unable to create a view for this node!
                node.setNodePeer(nodePeer = createUnimplementedNodePeer(node)); // Displaying an "Unimplemented..." button instead
            else { // Standard case (the node view was successfully created)
                nodePeer.bind(node, sceneRequester);
                node.onNodePeerBound();
            }
            node.callNodePeerHandlers();
        }
        return nodePeer;
    }


    private NodePeer<Node> createNodePeer(Node node) {
        ScenePeer scenePeer = impl_getPeer();
        NodePeer<Node> nodePeer = WebFxKitMapper.createNodePeer(node);
        scenePeer.onNodePeerCreated(nodePeer);
        return nodePeer;
    }

    private NodePeer createUnimplementedNodePeer(Node node) {
        return getOrCreateAndBindNodePeer(createUnimplementedNodeReplacer(node));
    }

    private Node createUnimplementedNodeReplacer(Node node) {
        // Creating a region as replacement (assuming the node peer factory at least implements a region peer!)
        Region nodeReplacer = new Region();
        nodeReplacer.setPrefSize(10, 10);
        nodeReplacer.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
        // TODO: add a text within the rectangle with the following message
        //String message = Strings.removeSuffix(node.getClass().getSimpleName(), "Impl") + " peer not provided";
        // Binding to allow the button to respond to the original node layout
        nodeReplacer.layoutXProperty().bind(node.layoutXProperty());
        nodeReplacer.layoutYProperty().bind(node.layoutYProperty());
        if (node instanceof HasReadOnlyWidthProperty wn)
            nodeReplacer.widthPropertyImpl().bind(wn.widthProperty());
        if (node instanceof HasHeightProperty hn)
            nodeReplacer.heightPropertyImpl().bind(hn.heightProperty());
        return nodeReplacer;
    }

    private Scheduled pulseScheduled;

    public boolean isPulseRunning() {
        return pulseScheduled != null;
    }

    public void startPulse() {
        if (pulseScheduled == null) {
            pulseScheduled = UiScheduler.schedulePeriodicInAnimationFrame(scenePulseListener::pulse, AnimationFramePass.SCENE_PULSE_LAYOUT_PASS);
        }
    }

    public void stopPulse() {
        if (pulseScheduled != null) {
            pulseScheduled.cancel();
            pulseScheduled = null;
        }
    }

    private void doCSSPass() {
    }

    protected void doLayoutPass() {
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
            impl_peer = WebFxKitMapper.createScenePeer(this);
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

        WindowPeer windowPeer = window.impl_getPeer();
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
     * Set to true if something has happened to the focused node that makes
     * it no longer eligible to have the focus.
     *
     */
    private boolean focusDirty = true;

    final void setFocusDirty(boolean value) {
        if (!focusDirty) {
            //Toolkit.getToolkit().requestNextPulse();
            UiScheduler.requestNextScenePulse();
        }
        focusDirty = value;
    }

    final boolean isFocusDirty() {
        return focusDirty;
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
                    //Scene.this.focusIneligible(oldOwner);
                }
                Scene.this.setFocusDirty(false);
            }
        }

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
*/
            focusCleanup();
/*
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

    private void processMenuEvent(double x2, double y2, double xAbs, double yAbs, boolean isKeyboardTrigger) {
        EventTarget eventTarget = null;
/*
        Scene.inMousePick = true;
        if (isKeyboardTrigger) {
            Node sceneFocusOwner = getFocusOwner();

            // for keyboard triggers set coordinates inside focus owner
            final double xOffset = xAbs - x2;
            final double yOffset = yAbs - y2;
            if (sceneFocusOwner != null) {
                final Bounds bounds = sceneFocusOwner.localToScene(
                        sceneFocusOwner.getBoundsInLocal());
                x2 = bounds.getMinX() + bounds.getWidth() / 4;
                y2 = bounds.getMinY() + bounds.getHeight() / 2;
                eventTarget = sceneFocusOwner;
            } else {
                x2 = Scene.this.getWidth() / 4;
                y2 = Scene.this.getWidth() / 2;
                eventTarget = Scene.this;
            }

            xAbs = x2 + xOffset;
            yAbs = y2 + yOffset;
        }
*/

        final PickResult res = pick(x2, y2);

        if (!isKeyboardTrigger) {
            eventTarget = res == null ? null : res.getIntersectedNode();
            if (eventTarget == null) {
                eventTarget = this;
            }
        }

        if (eventTarget != null) {
            ContextMenuEvent context = new ContextMenuEvent(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
                    x2, y2, xAbs, yAbs, isKeyboardTrigger, res);
            Event.fireEvent(eventTarget, context);
        }
        //Scene.inMousePick = false;
    }

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
                */
        public void menuEvent(double x, double y, double xAbs, double yAbs,
                              boolean isKeyboardTrigger) {
            Scene.this.processMenuEvent(x, y, xAbs,yAbs, isKeyboardTrigger);
        }
        /*
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

    private DnDGesture dndGesture;

    public DnDGesture getOrCreateDndGesture() {  // WebFX addition
        return dndGesture != null ? dndGesture : (dndGesture = new DnDGesture());
    }

    public void clearDndGesture() {  // WebFX addition
        dndGesture = null;
    }

    Dragboard startDragAndDrop(EventTarget source, TransferMode... transferModes) {
        //Toolkit.getToolkit().checkFxUserThread();
        getOrCreateDndGesture().processingDragDetected(); // WebFX addition
        if (dndGesture == null ||
                (dndGesture.dragDetected != DragDetectedState.PROCESSING))
        {
            throw new IllegalStateException("Cannot start drag and drop " +
                    "outside of DRAG_DETECTED event handler");
        }

        Set<TransferMode> set = EnumSet.noneOf(TransferMode.class);
        for (TransferMode tm : InputEventUtils.safeTransferModes(transferModes)) {
            set.add(tm);
        }
        return dndGesture.startDrag(source, set);
    }


    /* *************************************************************************
     *                                                                         *
     *                             Mouse Handling                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Defines a function to be called when a mouse button has been clicked
     * (pressed and released) on this {@code Scene}.
     */
    private ObjectProperty<EventHandler<? super MouseEvent>> onMouseClicked;

    public final void setOnMouseClicked(EventHandler<? super MouseEvent> value) {
        onMouseClickedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseClicked() {
        return onMouseClicked == null ? null : onMouseClicked.get();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMouseClickedProperty() {
        if (onMouseClicked == null) {
            onMouseClicked = new ObjectPropertyBase<EventHandler<? super MouseEvent>>() {

                @Override
                protected void invalidated() {
                    setEventHandler(MouseEvent.MOUSE_CLICKED, get());
                }

                @Override
                public Object getBean() {
                    return Scene.this;
                }

                @Override
                public String getName() {
                    return "onMouseClicked";
                }
            };
        }
        return onMouseClicked;
    }

    /**
     * Defines a function to be called when a mouse button
     * has been pressed on this {@code Scene}.
     */
    private ObjectProperty<EventHandler<? super MouseEvent>> onMousePressed;

    public final void setOnMousePressed(EventHandler<? super MouseEvent> value) {
        onMousePressedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMousePressed() {
        return onMousePressed == null ? null : onMousePressed.get();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMousePressedProperty() {
        if (onMousePressed == null) {
            onMousePressed = new ObjectPropertyBase<EventHandler<? super MouseEvent>>() {

                @Override
                protected void invalidated() {
                    setEventHandler(MouseEvent.MOUSE_PRESSED, get());
                }

                @Override
                public Object getBean() {
                    return Scene.this;
                }

                @Override
                public String getName() {
                    return "onMousePressed";
                }
            };
        }
        return onMousePressed;
    }

    /**
     * Defines a function to be called when a mouse button
     * has been released on this {@code Scene}.
     */
    private ObjectProperty<EventHandler<? super MouseEvent>> onMouseReleased;

    public final void setOnMouseReleased(EventHandler<? super MouseEvent> value) {
        onMouseReleasedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseReleased() {
        return onMouseReleased == null ? null : onMouseReleased.get();
    }

    public final ObjectProperty<EventHandler<? super MouseEvent>> onMouseReleasedProperty() {
        if (onMouseReleased == null) {
            onMouseReleased = new ObjectPropertyBase<EventHandler<? super MouseEvent>>() {

                @Override
                protected void invalidated() {
                    setEventHandler(MouseEvent.MOUSE_RELEASED, get());
                }

                @Override
                public Object getBean() {
                    return Scene.this;
                }

                @Override
                public String getName() {
                    return "onMouseReleased";
                }
            };
        }
        return onMouseReleased;
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
                //if (mb != MouseButton.NONE) { // Commented for WebFX to prevent NPE (because WebFX may generate some MOUSE_RELEASED events with MouseButton.NONE!) TODO: investigate if this case can be prevented in first place
                    counters.put(mb, new ClickCounter());
                //}
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

    private PickResult pick(final double x, final double y) {
        NodePeer nodePeer = impl_getPeer().pickPeer(x, y);
        PickResult pickResult = null;
        if (nodePeer != null) {
            Node node = nodePeer.getNode();
            if (node == null) // Happens for any reason with HtmlPathNodePeer
                Console.log("⚠️ Scene.pick() detected that nodePeer.getNode() is null for nodePeer " + nodePeer);
            else
                pickResult = new PickResult(node, x, y);
        }
        return pickResult;
    }

    private void pick(TargetWrapper target, final double x, final double y) {
        target.setNodeResult(pick(x, y));
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

    /**
     * A Drag and Drop gesture has a lifespan that lasts from mouse
     * PRESSED event to mouse RELEASED event.
     */
    public class DnDGesture {
        /*private final double hysteresisSizeX =
                Toolkit.getToolkit().getMultiClickMaxX();
        private final double hysteresisSizeY =
                Toolkit.getToolkit().getMultiClickMaxY();*/

        public EventTarget source = null;
        public Set<TransferMode> sourceTransferModes = null;
        public TransferMode acceptedTransferMode = null;
        private Dragboard dragboard = null;
        private EventTarget potentialTarget = null;
        public EventTarget target = null;
        private DragDetectedState dragDetected = DragDetectedState.NOT_YET;
        //private double pressedX;
        //private double pressedY;
        //private List<EventTarget> currentTargets = new ArrayList<EventTarget>();
        //private List<EventTarget> newTargets = new ArrayList<EventTarget>();
        //private EventTarget fullPDRSource = null;

        /**
         * Fires event on a given target or on scene if the node is null
         */
        /*private void fireEvent(EventTarget target, Event e) {
            if (target != null) {
                Event.fireEvent(target, e);
            }
        }*/

        /**
         * Called when DRAG_DETECTED event is going to be processed by
         * application
         */
        private void processingDragDetected() {
            dragDetected = DragDetectedState.PROCESSING;
        }

        /**
         * Called after DRAG_DETECTED event has been processed by application
         */
        /*private void dragDetectedProcessed() {
            dragDetected = DragDetectedState.DONE;
            final boolean hasContent = (dragboard != null) && (ClipboardHelper.contentPut(dragboard));
            if (hasContent) {
                *//* start DnD *//*
                Toolkit.getToolkit().startDrag(Scene.this.peer,
                        sourceTransferModes,
                        new DragSourceListener(),
                        dragboard);
            } else if (fullPDRSource != null) {
                *//* start PDR *//*
                Scene.this.mouseHandler.enterFullPDR(fullPDRSource);
            }

            fullPDRSource = null;
        }*/

        /**
         * Sets the default dragDetect value
         */
        /*private void processDragDetection(MouseEvent mouseEvent) {

            if (dragDetected != DragDetectedState.NOT_YET) {
                mouseEvent.setDragDetect(false);
                return;
            }

            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                pressedX = mouseEvent.getSceneX();
                pressedY = mouseEvent.getSceneY();

                mouseEvent.setDragDetect(false);

            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {

                double deltaX = Math.abs(mouseEvent.getSceneX() - pressedX);
                double deltaY = Math.abs(mouseEvent.getSceneY() - pressedY);
                mouseEvent.setDragDetect(deltaX > hysteresisSizeX ||
                        deltaY > hysteresisSizeY);

            }
        }*/

        /**
         * This function is useful for drag gesture recognition from
         * within this Scene (as opposed to in the TK implementation... by the platform)
         */
        /*private boolean process(MouseEvent mouseEvent, EventTarget target) {
            boolean continueProcessing = true;
            if (!PLATFORM_DRAG_GESTURE_INITIATION) {

                if (dragDetected != DragDetectedState.DONE &&
                        (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED ||
                                mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) &&
                        mouseEvent.isDragDetect()) {

                    processingDragDetected();

                    if (target != null) {
                        final MouseEvent detectedEvent = mouseEvent.copyFor(
                                mouseEvent.getSource(), target,
                                MouseEvent.DRAG_DETECTED);

                        try {
                            fireEvent(target, detectedEvent);
                        } finally {
                            // Putting data to dragboard finished, restrict access to them
                            if (dragboard != null) {
                                DragboardHelper.setDataAccessRestriction(
                                        dragboard, true);
                            }
                        }
                    }

                    dragDetectedProcessed();
                }

                if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    continueProcessing = false;
                }
            }
            return continueProcessing;
        }*/

        /*
         * Called when a drag source is recognized. This occurs at the very start of
         * the publicly visible drag and drop API, as it is responsible for calling
         * the Node.onDragSourceRecognized function.
         */
        /*private boolean processRecognized(DragEvent de) {
            MouseEvent me = new MouseEvent(
                    MouseEvent.DRAG_DETECTED, de.getX(), de.getY(),
                    de.getSceneX(), de.getScreenY(), MouseButton.PRIMARY, 1,
                    false, false, false, false, false, true, false, false, false,
                    false, de.getPickResult());

            processingDragDetected();

            final EventTarget target = de.getPickResult().getIntersectedNode();
            try {
                fireEvent(target != null ? target : Scene.this, me);
            } finally {
                // Putting data to dragboard finished, restrict access to them
                if (dragboard != null) {
                    DragboardHelper.setDataAccessRestriction(
                            dragboard, true);
                }
            }

            dragDetectedProcessed();

            final boolean hasContent = dragboard != null
                    && !dragboard.getContentTypes().isEmpty();
            return hasContent;
        }*/

        /*private void processDropEnd(DragEvent de) {
            if (source == null) {
                System.out.println("Scene.DnDGesture.processDropEnd() - UNEXPECTD - source is NULL");
                return;
            }

            de = new DragEvent(de.getSource(), source, DragEvent.DRAG_DONE,
                    de.getDragboard(), de.getSceneX(), de.getSceneY(),
                    de.getScreenX(), de.getScreenY(),
                    de.getTransferMode(), source, target, de.getPickResult());

            Event.fireEvent(source, de);

            tmpTargetWrapper.clear();
            handleExitEnter(de, tmpTargetWrapper);

            // at this point the drag and drop operation is completely over, so we
            // can tell the toolkit that it can clean up if needs be.
            Toolkit.getToolkit().stopDrag(dragboard);
        }*/

        /*private TransferMode processTargetEnterOver(DragEvent de) {
            pick(tmpTargetWrapper, de.getSceneX(), de.getSceneY());
            final EventTarget pickedTarget = tmpTargetWrapper.getEventTarget();

            if (dragboard == null) {
                dragboard = createDragboard(de, false);
            }

            de = new DragEvent(de.getSource(), pickedTarget, de.getEventType(),
                    dragboard, de.getSceneX(), de.getSceneY(),
                    de.getScreenX(), de.getScreenY(),
                    de.getTransferMode(), source, potentialTarget, de.getPickResult());

            handleExitEnter(de, tmpTargetWrapper);

            de = new DragEvent(de.getSource(), pickedTarget, DragEvent.DRAG_OVER,
                    de.getDragboard(), de.getSceneX(), de.getSceneY(),
                    de.getScreenX(), de.getScreenY(),
                    de.getTransferMode(), source, potentialTarget, de.getPickResult());

            fireEvent(pickedTarget, de);

            Object acceptingObject = de.getAcceptingObject();
            potentialTarget = acceptingObject instanceof EventTarget
                    ? (EventTarget) acceptingObject : null;
            acceptedTransferMode = de.getAcceptedTransferMode();
            return acceptedTransferMode;
        }*/

        /*private void processTargetActionChanged(DragEvent de) {
            // Do we want DRAG_TRANSFER_MODE_CHANGED event?
//            final Node pickedNode = Scene.this.mouseHandler.pickNode(de.getX(), de.getY());
//            if (pickedNode != null && pickedNode.isTreeVisible()) {
//                de = DragEvent.copy(de.getSource(), pickedNode, source,
//                        pickedNode, de, DragEvent.DRAG_TRANSFER_MODE_CHANGED);
//
//                if (dragboard == null) {
//                    dragboard = createDragboard(de);
//                }
//                dragboard = de.getPlatformDragboard();
//
//                fireEvent(pickedNode, de);
//            }
        }*/

        /*private void processTargetExit(DragEvent de) {
            if (dragboard == null) {
                // dragboard should have been created in processTargetEnterOver()
                throw new NullPointerException("dragboard is null in processTargetExit()");
            }

            if (currentTargets.size() > 0) {
                potentialTarget = null;
                tmpTargetWrapper.clear();
                handleExitEnter(de, tmpTargetWrapper);
            }
        }*/

        /*private TransferMode processTargetDrop(DragEvent de) {
            pick(tmpTargetWrapper, de.getSceneX(), de.getSceneY());
            final EventTarget pickedTarget = tmpTargetWrapper.getEventTarget();

            de = new DragEvent(de.getSource(), pickedTarget, DragEvent.DRAG_DROPPED,
                    de.getDragboard(), de.getSceneX(), de.getSceneY(),
                    de.getScreenX(), de.getScreenY(),
                    acceptedTransferMode, source, potentialTarget, de.getPickResult());

            if (dragboard == null) {
                // dragboard should have been created in processTargetEnterOver()
                throw new NullPointerException("dragboard is null in processTargetDrop()");
            }

            handleExitEnter(de, tmpTargetWrapper);

            fireEvent(pickedTarget, de);

            Object acceptingObject = de.getAcceptingObject();
            potentialTarget = acceptingObject instanceof EventTarget
                    ? (EventTarget) acceptingObject : null;
            target = potentialTarget;

            TransferMode result = de.isDropCompleted() ?
                    de.getAcceptedTransferMode() : null;

            tmpTargetWrapper.clear();
            handleExitEnter(de, tmpTargetWrapper);

            return result;
        }*/

        /*private void handleExitEnter(DragEvent e, TargetWrapper target) {
            EventTarget currentTarget =
                    currentTargets.size() > 0 ? currentTargets.get(0) : null;

            if (target.getEventTarget() != currentTarget) {

                target.fillHierarchy(newTargets);

                int i = currentTargets.size() - 1;
                int j = newTargets.size() - 1;

                while (i >= 0 && j >= 0 && currentTargets.get(i) == newTargets.get(j)) {
                    i--;
                    j--;
                }

                for (; i >= 0; i--) {
                    EventTarget t = currentTargets.get(i);
                    if (potentialTarget == t) {
                        potentialTarget = null;
                    }
                    e = e.copyFor(e.getSource(), t, source,
                            potentialTarget, DragEvent.DRAG_EXITED_TARGET);
                    Event.fireEvent(t, e);
                }

                potentialTarget = null;
                for (; j >= 0; j--) {
                    EventTarget t = newTargets.get(j);
                    e = e.copyFor(e.getSource(), t, source,
                            potentialTarget, DragEvent.DRAG_ENTERED_TARGET);
                    Object acceptingObject = e.getAcceptingObject();
                    if (acceptingObject instanceof EventTarget) {
                        potentialTarget = (EventTarget) acceptingObject;
                    }
                    Event.fireEvent(t, e);
                }

                currentTargets.clear();
                currentTargets.addAll(newTargets);
                newTargets.clear();
            }
        }*/

//        function getIntendedTransferMode(e:MouseEvent):TransferMode {
//            return if (e.altDown) TransferMode.COPY else TransferMode.MOVE;
//        }

        /*
         * Function that hooks into the key processing code in Scene to handle the
         * situation where a drag and drop event is taking place and the user presses
         * the escape key to cancel the drag and drop operation.
         */
        /*private boolean processKey(KeyEvent e) {
            //note: this seems not to be called, the DnD cancelation is provided by platform
            if ((e.getEventType() == KeyEvent.KEY_PRESSED) && (e.getCode() == KeyCode.ESCAPE)) {

                // cancel drag and drop
                DragEvent de = new DragEvent(
                        source, source, DragEvent.DRAG_DONE, dragboard, 0, 0, 0, 0,
                        null, source, null, null);
                if (source != null) {
                    Event.fireEvent(source, de);
                }

                tmpTargetWrapper.clear();
                handleExitEnter(de, tmpTargetWrapper);

                return false;
            }
            return true;
        }*/

        /*
         * This starts the drag gesture running, creating the dragboard used for
         * the remainder of this drag and drop operation.
         */
        private Dragboard startDrag(EventTarget source, Set<TransferMode> t) {
            if (dragDetected != DragDetectedState.PROCESSING) {
                throw new IllegalStateException("Cannot start drag and drop "
                        + "outside of DRAG_DETECTED event handler");
            }

            if (t.isEmpty()) {
                dragboard = null;
            } else if (dragboard == null) {
                dragboard = createDragboard(null, true);
            }

            // The app can see what it puts to dragboard without restriction
            //DragboardHelper.setDataAccessRestriction(dragboard, false);

            this.source = source;
            potentialTarget = source;
            sourceTransferModes = t;
            return dragboard;
        }

        /*
         * This starts the full PDR gesture.
         */
        /*private void startFullPDR(EventTarget source) {
            fullPDRSource = source;
        }*/

        private Dragboard createDragboard(final DragEvent de, boolean isDragSource) {
            Dragboard dragboard = null;
            if (de != null) {
                dragboard = de.getDragboard();
                if (dragboard != null) {
                    return dragboard;
                }
            }
            //TKClipboard dragboardPeer = peer.createDragboard(isDragSource);
            // DragboardHelper.createDragboard(dragboardPeer);
            return WebFxKitLauncher.getProvider().createDragboard(Scene.this);
        }

        public Dragboard getOrCreateDragboard() { // WebFX addition
            if (dragboard == null) {
                dragboard = createDragboard(null, false);
                sourceTransferModes = new HashSet<>(Collections.listOf(TransferMode.ANY));
            }
            return dragboard;
        }
    }

    /**
     * State of a drag gesture with regards to DRAG_DETECTED event.
     */
    private enum DragDetectedState {
        NOT_YET,
        PROCESSING,
        DONE
    }

    /***************************************************************************
     *                                                                         *
     *                           Keyboard Handling                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Defines a function to be called when some {@code Node} of this
     * {@code Scene} has input focus and a key has been pressed. The function
     * is called only if the event hasn't been already consumed during its
     * capturing or bubbling phase.
     */
    private ObjectProperty<EventHandler<? super KeyEvent>> onKeyPressed;

    public final void setOnKeyPressed(EventHandler<? super KeyEvent> value) {
        onKeyPressedProperty().set(value);
    }

    public final EventHandler<? super KeyEvent> getOnKeyPressed() {
        return onKeyPressed == null ? null : onKeyPressed.get();
    }

    public final ObjectProperty<EventHandler<? super KeyEvent>> onKeyPressedProperty() {
        if (onKeyPressed == null) {
            onKeyPressed = new ObjectPropertyBase<EventHandler<? super KeyEvent>>() {

                @Override
                protected void invalidated() {
                    setEventHandler(KeyEvent.KEY_PRESSED, get());
                }

                @Override
                public Object getBean() {
                    return Scene.this;
                }

                @Override
                public String getName() {
                    return "onKeyPressed";
                }
            };
        }
        return onKeyPressed;
    }

    /**
     * Defines a function to be called when some {@code Node} of this
     * {@code Scene} has input focus and a key has been released. The function
     * is called only if the event hasn't been already consumed during its
     * capturing or bubbling phase.
     */
    private ObjectProperty<EventHandler<? super KeyEvent>> onKeyReleased;

    public final void setOnKeyReleased(EventHandler<? super KeyEvent> value) {
        onKeyReleasedProperty().set(value);
    }

    public final EventHandler<? super KeyEvent> getOnKeyReleased() {
        return onKeyReleased == null ? null : onKeyReleased.get();
    }

    public final ObjectProperty<EventHandler<? super KeyEvent>> onKeyReleasedProperty() {
        if (onKeyReleased == null) {
            onKeyReleased = new ObjectPropertyBase<EventHandler<? super KeyEvent>>() {

                @Override
                protected void invalidated() {
                    setEventHandler(KeyEvent.KEY_RELEASED, get());
                }

                @Override
                public Object getBean() {
                    return Scene.this;
                }

                @Override
                public String getName() {
                    return "onKeyReleased";
                }
            };
        }
        return onKeyReleased;
    }

    /**
     * Defines a function to be called when some {@code Node} of this
     * {@code Scene} has input focus and a key has been typed. The function
     * is called only if the event hasn't been already consumed during its
     * capturing or bubbling phase.
     */
    private ObjectProperty<EventHandler<? super KeyEvent>> onKeyTyped;

    public final void setOnKeyTyped(
            EventHandler<? super KeyEvent> value) {
        onKeyTypedProperty().set( value);

    }

    public final EventHandler<? super KeyEvent> getOnKeyTyped(
    ) {
        return onKeyTyped == null ? null : onKeyTyped.get();
    }

    public final ObjectProperty<EventHandler<? super KeyEvent>> onKeyTypedProperty(
    ) {
        if (onKeyTyped == null) {
            onKeyTyped = new ObjectPropertyBase<EventHandler<? super KeyEvent>>() {

                @Override
                protected void invalidated() {
                    setEventHandler(KeyEvent.KEY_TYPED, get());
                }

                @Override
                public Object getBean() {
                    return Scene.this;
                }

                @Override
                public String getName() {
                    return "onKeyTyped";
                }
            };
        }
        return onKeyTyped;
    }

    // PENDING_DOC_REVIEW
    /**
     * Registers an event handler to this scene. The handler is called when the
     * scene receives an {@code Event} of the specified type during the bubbling
     * phase of event delivery.
     *
     * @param <T> the specific event class of the handler
     * @param eventType the type of the events to receive by the handler
     * @param eventHandler the handler to register
     * @throws NullPointerException if the event type or handler is null
     */
    public final <T extends Event> void addEventHandler(
            final EventType<T> eventType,
            final EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().getEventHandlerManager()
                .addEventHandler(eventType, eventHandler);
    }

    // PENDING_DOC_REVIEW
    /**
     * Unregisters a previously registered event handler from this scene. One
     * handler might have been registered for different event types, so the
     * caller needs to specify the particular event type from which to
     * unregister the handler.
     *
     * @param <T> the specific event class of the handler
     * @param eventType the event type from which to unregister
     * @param eventHandler the handler to unregister
     * @throws NullPointerException if the event type or handler is null
     */
    public final <T extends Event> void removeEventHandler(
            final EventType<T> eventType,
            final EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().getEventHandlerManager()
                .removeEventHandler(eventType,
                        eventHandler);
    }

    // PENDING_DOC_REVIEW
    /**
     * Registers an event filter to this scene. The filter is called when the
     * scene receives an {@code Event} of the specified type during the
     * capturing phase of event delivery.
     *
     * @param <T> the specific event class of the filter
     * @param eventType the type of the events to receive by the filter
     * @param eventFilter the filter to register
     * @throws NullPointerException if the event type or filter is null
     */
    public final <T extends Event> void addEventFilter(
            final EventType<T> eventType,
            final EventHandler<? super T> eventFilter) {
        getInternalEventDispatcher().getEventHandlerManager()
                .addEventFilter(eventType, eventFilter);
    }

    // PENDING_DOC_REVIEW
    /**
     * Unregisters a previously registered event filter from this scene. One
     * filter might have been registered for different event types, so the
     * caller needs to specify the particular event type from which to
     * unregister the filter.
     *
     * @param <T> the specific event class of the filter
     * @param eventType the event type from which to unregister
     * @param eventFilter the filter to unregister
     * @throws NullPointerException if the event type or filter is null
     */
    public final <T extends Event> void removeEventFilter(
            final EventType<T> eventType,
            final EventHandler<? super T> eventFilter) {
        getInternalEventDispatcher().getEventHandlerManager()
                .removeEventFilter(eventType, eventFilter);
    }

    /**
     * Sets the handler to use for this event type. There can only be one such
     * handler specified at a time. This handler is guaranteed to be called
     * first. This is used for registering the user-defined onFoo event
     * handlers.
     *
     * @param <T> the specific event class of the handler
     * @param eventType the event type to associate with the given eventHandler
     * @param eventHandler the handler to register, or null to unregister
     * @throws NullPointerException if the event type is null
     */
    protected final <T extends Event> void setEventHandler(
            final EventType<T> eventType,
            final EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().getEventHandlerManager()
                .setEventHandler(eventType, eventHandler);
    }

    private TopMostTraversalEngine traversalEngine = new SceneTraversalEngine(this);

    /**
     * Traverses focus from the given node in the given direction.
     */
    boolean traverse(Node node, Direction dir, TraversalMethod method) {
        /*if (node.getSubScene() != null) {
            return node.getSubScene().traverse(node, dir, method);
        }*/
        return traversalEngine.trav(node, dir, method) != null;
    }

    /**
     * Moves the focus to a reasonable initial location. Called when a scene's
     * focus is dirty and there's no current owner, or if the owner has been
     * removed from the scene.
     */
    public void focusInitial() {
        traversalEngine.traverseToFirst();
    }

}
