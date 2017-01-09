package naga.fx.scene;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import naga.fx.event.Event;
import naga.fx.event.EventHandler;
import naga.fx.event.EventTarget;
import naga.fx.event.EventType;
import naga.fx.spi.peer.NodePeer;
import naga.fx.sun.geom.BaseBounds;
import naga.fx.sun.geom.RectBounds;
import naga.fx.sun.geom.TempState;
import naga.fx.sun.geom.transform.BaseTransform;
import naga.fx.geometry.BoundingBox;
import naga.fx.geometry.Bounds;
import naga.fx.geometry.Orientation;
import naga.fx.properties.markers.*;
import naga.fx.scene.effect.BlendMode;
import naga.fx.scene.effect.Effect;
import naga.fx.scene.input.MouseEvent;
import naga.fx.scene.layout.LayoutFlags;
import naga.fx.scene.transform.Transform;
import naga.fx.scene.transform.Translate;
import naga.fx.spi.Toolkit;
import naga.fx.sun.event.EventDispatchChain;
import naga.fx.sun.event.EventDispatcher;
import naga.fx.sun.event.EventHandlerProperties;
import naga.fx.sun.scene.NodeEventDispatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static naga.fx.scene.layout.PreferenceResizableNode.USE_COMPUTED_SIZE;
import static naga.fx.scene.layout.PreferenceResizableNode.USE_PREF_SIZE;

/**
 * @author Bruno Salmon
 */
public abstract class Node implements INode, EventTarget {

    private final Property<Parent> parentProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Parent> parentProperty() {
        return parentProperty;
    }

    private final Property<Boolean> managedProperty = new SimpleObjectProperty<Boolean>(true) {
        @Override
        protected void invalidated() {
            Parent parent = getParent();
            if (parent != null)
                parent.managedChildChanged();
            notifyManagedChanged();
        }
    };
    @Override
    public Property<Boolean> managedProperty() {
        return managedProperty;
    }

    private final Property<Boolean> mouseTransparentProperty = new SimpleObjectProperty<>(false);
    @Override
    public Property<Boolean> mouseTransparentProperty() {
        return mouseTransparentProperty;
    }

    /**
     * Called whenever the "managed" flag has changed. This is only
     * used by Parent as an optimization to keep track of whether a
     * Parent node is a layout root or not.
     */
    void notifyManagedChanged() { }

    /**
     * Defines a function to be called when a mouse button has been clicked
     * (pressed and released) on this {@code Node}.
     */
    public final ObjectProperty<EventHandler<? super MouseEvent>>
    onMouseClickedProperty() {
        return getEventHandlerProperties().onMouseClickedProperty();
    }

    private final Property<Boolean> visibleProperty = new SimpleObjectProperty<>(true);
    @Override
    public Property<Boolean> visibleProperty() {
        return visibleProperty;
    }

    private final Property<Double> opacityProperty = new SimpleObjectProperty<>(1d);
    @Override
    public Property<Double> opacityProperty() {
        return opacityProperty;
    }

    private final Property<Node> clipProperty = new SimpleObjectProperty<Node>() {
        @Override
        protected void invalidated() {
            setScene(scene); // This will propagate the scene into the clip
        }
    };
    @Override
    public Property<Node> clipProperty() {
        return clipProperty;
    }

    private final Property<BlendMode> blendModeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<BlendMode> blendModeProperty() {
        return blendModeProperty;
    }

    private final Property<Effect> effectProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Effect> effectProperty() {
        return effectProperty;
    }

    private final Property<Double> layoutXProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> layoutXProperty() {
        return layoutXProperty;
    }

    private final Property<Double> layoutYProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> layoutYProperty() {
        return layoutYProperty;
    }

    /**
     * Indicates whether or not this {@code Node} is disabled.  A {@code Node}
     * will become disabled if {@link #disableProperty disable} is set to {@code true} on either
     * itself or one of its ancestors in the scene graph.
     * <p>
     * A disabled {@code Node} should render itself differently to indicate its
     * disabled state to the user.
     * Such disabled rendering is dependent on the implementation of the
     * {@code Node}. The shape classes contained in {@code javafx.scene.shape}
     * do not implement such rendering by default, therefore applications using
     * shapes for handling input must implement appropriate disabled rendering
     * themselves. The user-interface controls defined in
     * {@code javafx.scene.control} will implement disabled-sensitive rendering,
     * however.
     * <p>
     * A disabled {@code Node} does not receive mouse or key events.
     *
     * @defaultValue false
     */
    private Property<Boolean> disabled;

    protected final void setDisabled(boolean value) {
        disabledPropertyImpl().setValue(value);
    }

    public final boolean isDisabled() {
        return disabled == null ? false : disabled.getValue();
    }

    public final Property<Boolean> disabledProperty() {
        return disabledPropertyImpl()/*.getReadOnlyProperty()*/;
    }

    private Property<Boolean> disabledPropertyImpl() {
        if (disabled == null) {
            disabled = new SimpleObjectProperty<>(false)/* {

                @Override
                protected void invalidated() {
                    pseudoClassStateChanged(DISABLED_PSEUDOCLASS_STATE, get());
                    updateCanReceiveFocus();
                    focusSetDirty(getScene());
                }

                @Override
                public Object getBean() {
                    return Node.this;
                }

                @Override
                public String getName() {
                    return "disabled";
                }
            }*/;
        }
        return disabled;
    }

    private void updateDisabled() {
        boolean isDisabled = false; //isDisable();
        if (!isDisabled) {
            isDisabled = getParent() != null ? getParent().isDisabled() :
                    false ; //getSubScene() != null && getSubScene().isDisabled();
        }
        setDisabled(isDisabled);
/*
        if (this instanceof SubScene) {
            ((SubScene)this).getRoot().setDisabled(isDisabled);
        }
*/
    }

    /**
     * Whether or not this {@code Node} is being hovered over. Typically this is
     * due to the mouse being over the node, though it could be due to a pen
     * hovering on a graphics tablet or other form of input.
     *
     * <p>Note that current implementation of hover relies on mouse enter and
     * exit events to determine whether this Node is in the hover state; this
     * means that this feature is currently supported only on systems that
     * have a mouse. Future implementations may provide alternative means of
     * supporting hover.
     *
     * @defaultValue false
     */
    private Property<Boolean> hover;

    protected final void setHover(boolean value) {
        hoverPropertyImpl().setValue(value);
    }

    public final boolean isHover() {
        return hover == null ? false : hover.getValue();
    }

    public final ReadOnlyProperty<Boolean> hoverProperty() {
        return hoverPropertyImpl()/*.getReadOnlyProperty()*/;
    }

    private Property<Boolean> hoverPropertyImpl() {
        if (hover == null) {
            hover = new SimpleObjectProperty<>(false)/* {

                @Override
                protected void invalidated() {
                    PlatformLogger logger = Logging.getInputLogger();
                    if (logger.isLoggable(Level.FINER)) {
                        logger.finer(this + " hover=" + get());
                    }
                    pseudoClassStateChanged(HOVER_PSEUDOCLASS_STATE, get());
                }

                @Override
                public Object getBean() {
                    return Node.this;
                }

                @Override
                public String getName() {
                    return "hover";
                }
            }*/;
        }
        return hover;
    }

    /**
     * Whether or not the {@code Node} is pressed. Typically this is true when
     * the primary mouse button is down, though subclasses may define other
     * mouse button state or key state to cause the node to be "pressed".
     *
     * @defaultValue false
     */
    private Property<Boolean> pressed;

    protected final void setPressed(boolean value) {
        pressedPropertyImpl().setValue(value);
    }

    public final boolean isPressed() {
        return pressed == null ? false : pressed.getValue();
    }

    public final ReadOnlyProperty<Boolean> pressedProperty() {
        return pressedPropertyImpl()/*.getReadOnlyProperty()*/;
    }

    private Property<Boolean> pressedPropertyImpl() {
        if (pressed == null) {
            pressed = new SimpleObjectProperty<>(false)/* {

                @Override
                protected void invalidated() {
                    PlatformLogger logger = Logging.getInputLogger();
                    if (logger.isLoggable(Level.FINER)) {
                        logger.finer(this + " pressed=" + get());
                    }
                    pseudoClassStateChanged(PRESSED_PSEUDOCLASS_STATE, get());
                }

                @Override
                public Object getBean() {
                    return Node.this;
                }

                @Override
                public String getName() {
                    return "pressed";
                }
            }*/;
        }
        return pressed;
    }

    private final ObservableList<Transform> transforms = FXCollections.observableArrayList();
    @Override
    public ObservableList<Transform> getTransforms() {
        return transforms;
    }

    private Translate layoutTransform;
    @Override
    public Collection<Transform> localToParentTransforms() {
        if (getLayoutX() == 0 && getLayoutY() == 0)
            return getTransforms();
        if (layoutTransform == null)
            layoutTransform = Translate.create();
        layoutTransform.setX(getLayoutX());
        layoutTransform.setY(getLayoutY());
        List<Transform> allTransforms = new ArrayList<>(transforms.size() + 1);
        allTransforms.add(layoutTransform);
        allTransforms.addAll(getTransforms());
        return allTransforms;
    }

    @Override
    public void autosize() {
        if (isResizable()) {
            Orientation contentBias = getContentBias();
            double w, h;
            if (contentBias == null) {
                w = boundedSize(prefWidth(-1), minWidth(-1), maxWidth(-1));
                h = boundedSize(prefHeight(-1), minHeight(-1), maxHeight(-1));
            } else if (contentBias == Orientation.HORIZONTAL) {
                w = boundedSize(prefWidth(-1), minWidth(-1), maxWidth(-1));
                h = boundedSize(prefHeight(w), minHeight(w), maxHeight(w));
            } else { // bias == VERTICAL
                h = boundedSize(prefHeight(-1), minHeight(-1), maxHeight(-1));
                w = boundedSize(prefWidth(h), minWidth(h), maxWidth(h));
            }
            resize(w, h);
        }
    }

    static double boundedSize(double value, double min, double max) {
        // if max < value, return max
        // if min > value, return min
        // if min > max, return min
        return Math.min(Math.max(value, min), Math.max(min,max));
    }

    private ObservableMap<Object, Object> properties;

    /**
     * Returns an observable map of properties on this node for use primarily
     * by application developers.
     *
     * @return an observable map of properties on this node for use primarily
     * by application developers
     */
    public final ObservableMap<Object, Object> getProperties() {
        if (properties == null)
            properties = FXCollections.observableMap(new HashMap<>());
        return properties;
    }

    /**
     * Tests if Node has properties.
     * @return true if node has properties.
     */
    public boolean hasProperties() {
        return properties != null && !properties.isEmpty();
    }

    void markDirtyLayoutBranch() {
        Parent p = getParent();
        while (p != null && p.layoutFlag == LayoutFlags.CLEAN) {
            p.setLayoutFlag(LayoutFlags.DIRTY_BRANCH);
            if (p.isSceneRoot())
                requestNextPulse();
            p = p.getParent();
        }
    }

    void requestNextPulse() {
        Toolkit.get().scheduler().requestNextPulse();
/*
        if (getSubScene() != null) {
            getSubScene().setDirtyLayout(p);
        }
*/
    }

    /***************************************************************************
     *                                                                         *
     *                         Event Dispatch                                  *
     *                                                                         *
     **************************************************************************/

    // PENDING_DOC_REVIEW
    /**
     * Specifies the event dispatcher for this node. The default event
     * dispatcher sends the received events to the registered event handlers and
     * filters. When replacing the value with a new {@code EventDispatcher},
     * the new dispatcher should forward events to the replaced dispatcher
     * to maintain the node's default event handling behavior.
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

    private NodeEventDispatcher internalEventDispatcher;

    // PENDING_DOC_REVIEW
    /**
     * Registers an event handler to this node. The handler is called when the
     * node receives an {@code Event} of the specified type during the bubbling
     * phase of event delivery.
     *
     * @param <T> the specific event class of the handler
     * @param eventType the type of the events to receive by the handler
     * @param eventHandler the handler to register
     * @throws NullPointerException if the event type or handler is null
     */
    public final <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().getEventHandlerManager().addEventHandler(eventType, eventHandler);
    }

    // PENDING_DOC_REVIEW
    /**
     * Unregisters a previously registered event handler from this node. One
     * handler might have been registered for different event types, so the
     * caller needs to specify the particular event type from which to
     * unregister the handler.
     *
     * @param <T> the specific event class of the handler
     * @param eventType the event type from which to unregister
     * @param eventHandler the handler to unregister
     * @throws NullPointerException if the event type or handler is null
     */
    public final <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().getEventHandlerManager().removeEventHandler(eventType, eventHandler);
    }

    // PENDING_DOC_REVIEW
    /**
     * Registers an event filter to this node. The filter is called when the
     * node receives an {@code Event} of the specified type during the capturing
     * phase of event delivery.
     *
     * @param <T> the specific event class of the filter
     * @param eventType the type of the events to receive by the filter
     * @param eventFilter the filter to register
     * @throws NullPointerException if the event type or filter is null
     */
    public final <T extends Event> void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        getInternalEventDispatcher().getEventHandlerManager().addEventFilter(eventType, eventFilter);
    }

    // PENDING_DOC_REVIEW
    /**
     * Unregisters a previously registered event filter from this node. One
     * filter might have been registered for different event types, so the
     * caller needs to specify the particular event type from which to
     * unregister the filter.
     *
     * @param <T> the specific event class of the filter
     * @param eventType the event type from which to unregister
     * @param eventFilter the filter to unregister
     * @throws NullPointerException if the event type or filter is null
     */
    public final <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        getInternalEventDispatcher().getEventHandlerManager().removeEventFilter(eventType, eventFilter);
    }

    /**
     * Sets the handler to use for this event type. There can only be one such handler
     * specified at a time. This handler is guaranteed to be called as the last, after
     * handlers added using {@link #addEventHandler(EventType, EventHandler)}.
     * This is used for registering the user-defined onFoo event handlers.
     *
     * @param <T> the specific event class of the handler
     * @param eventType the event type to associate with the given eventHandler
     * @param eventHandler the handler to register, or null to unregister
     * @throws NullPointerException if the event type is null
     */
    protected final <T extends Event> void setEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().getEventHandlerManager().setEventHandler(eventType, eventHandler);
    }

    private NodeEventDispatcher getInternalEventDispatcher() {
        initializeInternalEventDispatcher();
        return internalEventDispatcher;
    }

    private void initializeInternalEventDispatcher() {
        if (internalEventDispatcher == null) {
            internalEventDispatcher = createInternalEventDispatcher();
            eventDispatcher = new SimpleObjectProperty<>(
                    Node.this,
                    "eventDispatcher",
                    internalEventDispatcher);
        }
    }

    private NodeEventDispatcher createInternalEventDispatcher() {
        return new NodeEventDispatcher(this);
    }

    /**
     * Event dispatcher for invoking preprocessing of mouse events
     */
    private EventDispatcher preprocessMouseEventDispatcher;

    // PENDING_DOC_REVIEW
    /**
     * Construct an event dispatch chain for this node. The event dispatch chain
     * contains all event dispatchers from the stage to this node.
     *
     * @param tail the initial chain to build from
     * @return the resulting event dispatch chain for this node
     */
    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {

        if (preprocessMouseEventDispatcher == null) {
            preprocessMouseEventDispatcher = (event, tail1) -> {
                event = tail1.dispatchEvent(event);
                if (event instanceof MouseEvent)
                    preprocessMouseEvent((MouseEvent) event);

                return event;
            };
        }

        tail = tail.prepend(preprocessMouseEventDispatcher);

        // prepend all event dispatchers from this node to the root
        Node curNode = this;
        do {
            if (curNode.eventDispatcher != null) {
                final EventDispatcher eventDispatcherValue =
                        curNode.eventDispatcher.get();
                if (eventDispatcherValue != null) {
                    tail = tail.prepend(eventDispatcherValue);
                }
            }
            Node curParent = curNode.getParent();
            curNode = curParent != null ? curParent : null; //curNode.getSubScene();
        } while (curNode != null);

        if (getScene() != null) {
            // prepend scene's dispatch chain
            tail = getScene().buildEventDispatchChain(tail);
        }

        return tail;
    }

    private void preprocessMouseEvent(MouseEvent e) {
        EventType<?> eventType = e.getEventType();
        if (eventType == MouseEvent.MOUSE_PRESSED) {
            for (Node n = this; n != null; n = n.getParent())
                n.setPressed(e.isPrimaryButtonDown());
            return;
        }
        if (eventType == MouseEvent.MOUSE_RELEASED) {
            for (Node n = this; n != null; n = n.getParent())
                n.setPressed(e.isPrimaryButtonDown());
            return;
        }

        if (e.getTarget() == this) {
            // the mouse event types are translated only when the node uses
            // its internal event dispatcher, so both entered / exited variants
            // are possible here

            if ((eventType == MouseEvent.MOUSE_ENTERED) || (eventType == MouseEvent.MOUSE_ENTERED_TARGET)) {
                setHover(true);
                return;
            }

            if ((eventType == MouseEvent.MOUSE_EXITED) || (eventType == MouseEvent.MOUSE_EXITED_TARGET)) {
                setHover(false);
                return;
            }
        }
    }


    // PENDING_DOC_REVIEW
    /**
     * Fires the specified event. By default the event will travel through the
     * hierarchy from the stage to this node. Any event filter encountered will
     * be notified and can consume the event. If not consumed by the filters,
     * the event handlers on this node are notified. If these don't consume the
     * event eighter, the event will travel back the same path it arrived to
     * this node. All event handlers encountered are called and can consume the
     * event.
     * <p>
     * This method must be called on the FX user thread.
     *
     * @param event the event to fire
     */
    public final void fireEvent(Event event) {

        /* Log input events.  We do a coarse filter for at least the FINE
         * level and then granularize from there.
         */
/*
        if (event instanceof InputEvent) {
            PlatformLogger logger = Logging.getInputLogger();
            if (logger.isLoggable(Level.FINE)) {
                EventType eventType = event.getEventType();
                if (eventType == MouseEvent.MOUSE_ENTERED ||
                        eventType == MouseEvent.MOUSE_EXITED) {
                    logger.finer(event.toString());
                } else if (eventType == MouseEvent.MOUSE_MOVED ||
                        eventType == MouseEvent.MOUSE_DRAGGED) {
                    logger.finest(event.toString());
                } else {
                    logger.fine(event.toString());
                }
            }
        }
*/

        Event.fireEvent(this, event);
    }

    private NodePeer nodePeer;

    public NodePeer getNodePeer() {
        return nodePeer;
    }

    public NodePeer getOrCreateAndBindNodePeer() {
        return scene.getOrCreateAndBindNodePeer(this);
    }

    public void setNodePeer(NodePeer nodePeer) {
        this.nodePeer = nodePeer;
        createLayoutMeasurable(nodePeer);
    }

    private Scene scene;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        Node clip = getClip();
        if (clip != null)
            clip.setScene(scene);
    }

    private LayoutMeasurable layoutMeasurable;

    public LayoutMeasurable getLayoutMeasurable() {
        if (layoutMeasurable == null) // Happens when the node view is not yet created
            createLayoutMeasurable(null); // Temporary using the implementation based on the model
        return layoutMeasurable;
    }

    protected void createLayoutMeasurable(Object proposedLayoutMeasurable) {
        // Always creating a new LayoutMeasurable (even when proposedLayoutMeasurable is valid) so that min/pref/max
        // width/height user values are returned in priority whenever they have been set.
        layoutMeasurable = new LayoutMeasurable() {
            private LayoutMeasurable acceptedLayoutMeasurable = proposedLayoutMeasurable instanceof LayoutMeasurable ? (LayoutMeasurable) proposedLayoutMeasurable : null;
            {
                if (acceptedLayoutMeasurable != null)
                    Toolkit.get().scheduler().scheduleDeferred(() -> {
                        acceptedLayoutMeasurable.setSizeChangedCallback(() -> {
                            markDirtyLayoutBranch();
                        });
                    });
            }

            public Bounds getLayoutBounds() { return acceptedLayoutMeasurable != null ? acceptedLayoutMeasurable.getLayoutBounds() : impl_getLayoutBounds(); }

            public double minWidth(double height) {
                if (Node.this instanceof HasMinWidthProperty) {
                    double minWidth = ((HasMinWidthProperty) Node.this).getMinWidth();
                    if (minWidth == USE_PREF_SIZE)
                        return prefWidth(height);
                    if (minWidth != USE_COMPUTED_SIZE)
                        return minWidth;
                }
                return acceptedLayoutMeasurable != null ? acceptedLayoutMeasurable.minWidth(height) : impl_minWidth(height);
            }

            public double maxWidth(double height) {
                if (Node.this instanceof HasMaxWidthProperty) {
                    double maxWidth = ((HasMaxWidthProperty) Node.this).getMaxWidth();
                    if (maxWidth == USE_PREF_SIZE)
                        return prefWidth(height);
                    if (maxWidth != USE_COMPUTED_SIZE)
                        return maxWidth;
                }
                return acceptedLayoutMeasurable != null ? acceptedLayoutMeasurable.maxWidth(height) : impl_maxWidth(height);
            }

            public double minHeight(double width) {
                if (Node.this instanceof HasMinHeightProperty) {
                    double minHeight = ((HasMinHeightProperty) Node.this).getMinHeight();
                    if (minHeight == USE_PREF_SIZE)
                        return prefHeight(width);
                    if (minHeight != USE_COMPUTED_SIZE)
                        return minHeight;
                }
                return acceptedLayoutMeasurable != null ? acceptedLayoutMeasurable.minHeight(width) : impl_minHeight(width);
            }

            public double maxHeight(double width) {
                if (Node.this instanceof HasMaxHeightProperty) {
                    double maxHeight = ((HasMaxHeightProperty) Node.this).getMaxHeight();
                    if (maxHeight == USE_PREF_SIZE)
                        return prefHeight(width);
                    if (maxHeight != USE_COMPUTED_SIZE)
                        return maxHeight;
                }
                return acceptedLayoutMeasurable != null ? acceptedLayoutMeasurable.maxHeight(width) : impl_maxHeight(width);
            }

            public double prefWidth(double height) {
                if (Node.this instanceof HasPrefWidthProperty) {
                    double prefWidth = ((HasPrefWidthProperty) Node.this).getPrefWidth();
                    if (prefWidth != USE_COMPUTED_SIZE)
                        return prefWidth;
                }
                return acceptedLayoutMeasurable != null ? acceptedLayoutMeasurable.prefWidth(height) : impl_prefWidth(height);
            }

            public double prefHeight(double width) {
                if (Node.this instanceof HasPrefHeightProperty) {
                    double prefHeight = ((HasPrefHeightProperty) Node.this).getPrefHeight();
                    if (prefHeight != USE_COMPUTED_SIZE)
                        return prefHeight;
                }
                return acceptedLayoutMeasurable != null ? acceptedLayoutMeasurable.prefHeight(width) : impl_prefHeight(width);
            }
        };
    }

    /**
     * This special flag is used only by Parent to flag whether or not
     * the *parent* has processed the fact that bounds have changed for this
     * child Node. We need some way of flagging this on a per-node basis to
     * enable the significant performance optimizations and fast paths that
     * are in the Parent code.
     * <p>
     * To reduce confusion, although this variable is defined on Node, it
     * really belongs to the Parent of the node and should *only* be modified
     * by the parent.
     */
    boolean boundsChanged;

    protected Bounds impl_getLayoutBounds() {
        return impl_computeLayoutBounds();
    }

    protected double impl_minWidth(double height) {
        return impl_prefWidth(height);
    }

    protected double impl_maxWidth(double height) {
        return impl_prefWidth(height);
    }

    protected double impl_minHeight(double width) {
        return impl_prefHeight(width);
    }

    protected double impl_maxHeight(double width) {
        return impl_prefHeight(width);
    }

    protected double impl_prefWidth(double height) {
        double result = getLayoutBounds().getWidth();
        return Double.isNaN(result) || result < 0 ? 0 : result;
    }

    protected double impl_prefHeight(double width) {
        double result = getLayoutBounds().getHeight();
        return Double.isNaN(result) || result < 0 ? 0 : result;
    }

    /**
     * Returns geometric bounds, but may be over-ridden by a subclass.
     */
    protected
    Bounds impl_computeLayoutBounds() {
        BaseBounds tempBounds = TempState.getInstance().bounds;
        tempBounds = getGeomBounds(tempBounds,
                BaseTransform.IDENTITY_TRANSFORM);
        return new BoundingBox(tempBounds.getMinX(),
                tempBounds.getMinY(),
                tempBounds.getMinZ(),
                tempBounds.getWidth(),
                tempBounds.getHeight(),
                tempBounds.getDepth());
    }

    /**
     * Loads the given bounds object with the geometric bounds relative to,
     * and based on, the given transform.
     *
     * We *never* pass null in as a bounds. This method will
     * NOT take a null bounds object. The returned value may be
     * the same bounds object passed in, or it may be a new object.
     * The reason for this object promotion is in the case of needing
     * to promote from a RectBounds to a BoxBounds (3D).
     */
    BaseBounds getGeomBounds(BaseBounds bounds, BaseTransform tx) {
        if (tx.isTranslateOrIdentity()) {
            // we can take a fast path since we know tx is either a simple
            // translation or is identity
            updateGeomBounds();
            bounds = bounds.deriveWithNewBounds(geomBounds);
            if (!tx.isIdentity()) {
                throw new UnsupportedOperationException("Translation not implemented in getGeomBounds()");
/*
                double translateX = tx.getMxt();
                double translateY = tx.getMyt();
                double translateZ = tx.getMzt();
                bounds = bounds.deriveWithNewBounds((float) (bounds.getMinX() + translateX),
                        (float) (bounds.getMinY() + translateY),
                        (float) (bounds.getMinZ() + translateZ),
                        (float) (bounds.getMaxX() + translateX),
                        (float) (bounds.getMaxY() + translateY),
                        (float) (bounds.getMaxZ() + translateZ));
*/
            }
            return bounds;
        }
        throw new UnsupportedOperationException("Translation not implemented in getGeomBounds()");
/*
        if (tx.is2D()
                && (tx.getType()
                & ~(BaseTransform.TYPE_UNIFORM_SCALE | BaseTransform.TYPE_TRANSLATION
                | BaseTransform.TYPE_FLIP | BaseTransform.TYPE_QUADRANT_ROTATION)) != 0) {
            // this is a non-uniform scale / non-quadrant rotate / skew transform
            return impl_computeGeomBounds(bounds, tx);
        // 3D transformations and
        // selected 2D transformations (unifrom transform, flip, quadrant rotation).
        // These 2D transformation will yield tight bounds when applied on the pre-computed
        // geomBounds
        // Note: Transforming the local geomBounds into a 3D space will yield a bounds
        // that isn't as tight as transforming its geometry and compute it bounds.
        updateGeomBounds();
        return tx.transform(geomBounds, bounds);
*/
    }

    /**
     * If necessary, recomputes the cached geom bounds. If the bounds are not
     * invalid, then this method is a no-op.
     */
    void updateGeomBounds() {
        //if (geomBoundsInvalid) {
        geomBounds = impl_computeGeomBounds(geomBounds, BaseTransform.IDENTITY_TRANSFORM);
        //    geomBoundsInvalid = false;
        //}
    }

    /**
     * Computes the geometric bounds for this Node. This method is abstract
     * and must be implemented by each Node subclass.
     */
    public abstract BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx);

    /**
     * The cached bounds. This is never null, but is frequently set to be
     * invalid whenever the bounds for the node have changed. These are the
     * "content" bounds, that is, without transforms or effects applied.
     */
    private BaseBounds geomBounds = new RectBounds();

    /**
     * Loads the given bounds object with the transformed bounds relative to,
     * and based on, the given transform. That is, this is the local bounds
     * with the local-to-parent transform applied.
     *
     * We *never* pass null in as a bounds. This method will
     * NOT take a null bounds object. The returned value may be
     * the same bounds object passed in, or it may be a new object.
     * The reason for this object promotion is in the case of needing
     * to promote from a RectBounds to a BoxBounds (3D).
     */
    BaseBounds getTransformedBounds(BaseBounds bounds, BaseTransform tx) {
        if (!tx.isIdentity())
            throw new UnsupportedOperationException("Transform other then Identity not implemented in getGeomBounds()");
        return getLocalBounds(bounds, tx);
/*
        updateLocalToParentTransform();
        if (tx.isTranslateOrIdentity()) {
            updateTxBounds();
            bounds = bounds.deriveWithNewBounds(txBounds);
            if (!tx.isIdentity()) {
                final double translateX = tx.getMxt();
                final double translateY = tx.getMyt();
                final double translateZ = tx.getMzt();
                bounds = bounds.deriveWithNewBounds(
                        (float) (bounds.getMinX() + translateX),
                        (float) (bounds.getMinY() + translateY),
                        (float) (bounds.getMinZ() + translateZ),
                        (float) (bounds.getMaxX() + translateX),
                        (float) (bounds.getMaxY() + translateY),
                        (float) (bounds.getMaxZ() + translateZ));
            }
            return bounds;
        } else if (localToParentTx.isIdentity()) {
            return getLocalBounds(bounds, tx);
        } else {
            double mxx = tx.getMxx();
            double mxy = tx.getMxy();
            double mxz = tx.getMxz();
            double mxt = tx.getMxt();
            double myx = tx.getMyx();
            double myy = tx.getMyy();
            double myz = tx.getMyz();
            double myt = tx.getMyt();
            double mzx = tx.getMzx();
            double mzy = tx.getMzy();
            double mzz = tx.getMzz();
            double mzt = tx.getMzt();
            BaseTransform boundsTx = tx.deriveWithConcatenation(localToParentTx);
            bounds = getLocalBounds(bounds, boundsTx);
            if (boundsTx == tx) {
                tx.restoreTransform(mxx, mxy, mxz, mxt,
                        myx, myy, myz, myt,
                        mzx, mzy, mzz, mzt);
            }
            return bounds;
        }
*/
    }

    /**
     * Loads the given bounds object with the local bounds relative to,
     * and based on, the given transform. That is, these are the geometric
     * bounds + clip and effect.
     *
     * We *never* pass null in as a bounds. This method will
     * NOT take a null bounds object. The returned value may be
     * the same bounds object passed in, or it may be a new object.
     * The reason for this object promotion is in the case of needing
     * to promote from a RectBounds to a BoxBounds (3D).
     */
    BaseBounds getLocalBounds(BaseBounds bounds, BaseTransform tx) {
        if (getEffect() == null && getClip() == null)
            return getGeomBounds(bounds, tx);

        System.out.println("Warning: effect or clip not implemented in Node.getLocalBounds()");
        return getGeomBounds(bounds, tx);

        //throw new UnsupportedOperationException("Effect or clip not implemented in getLocalBounds()");
/*
        if (tx.isTranslateOrIdentity()) {
            // we can take a fast path since we know tx is either a simple
            // translation or is identity
            updateLocalBounds();
            bounds = bounds.deriveWithNewBounds(localBounds);
            if (!tx.isIdentity()) {
                double translateX = tx.getMxt();
                double translateY = tx.getMyt();
                double translateZ = tx.getMzt();
                bounds = bounds.deriveWithNewBounds((float) (bounds.getMinX() + translateX),
                        (float) (bounds.getMinY() + translateY),
                        (float) (bounds.getMinZ() + translateZ),
                        (float) (bounds.getMaxX() + translateX),
                        (float) (bounds.getMaxY() + translateY),
                        (float) (bounds.getMaxZ() + translateZ));
            }
            return bounds;
        } else if (tx.is2D()
                && (tx.getType()
                & ~(BaseTransform.TYPE_UNIFORM_SCALE | BaseTransform.TYPE_TRANSLATION
                | BaseTransform.TYPE_FLIP | BaseTransform.TYPE_QUADRANT_ROTATION)) != 0) {
            // this is a non-uniform scale / non-quadrant rotate / skew transform
            return computeLocalBounds(bounds, tx);
        } else {
            // 3D transformations and
            // selected 2D transformations (unifrom transform, flip, quadrant rotation).
            // These 2D transformation will yield tight bounds when applied on the pre-computed
            // geomBounds
            // Note: Transforming the local bounds into a 3D space will yield a bounds
            // that isn't as tight as transforming its geometry and compute it bounds.
            updateLocalBounds();
            return tx.transform(localBounds, bounds);
        }
*/
    }

    ////////////////////////////
    //  Private Implementation
    ////////////////////////////

    /***************************************************************************
     *                                                                         *
     *                        Event Handler Properties                         *
     *                                                                         *
     **************************************************************************/

    private EventHandlerProperties eventHandlerProperties;

    private EventHandlerProperties getEventHandlerProperties() {
        if (eventHandlerProperties == null) {
            eventHandlerProperties =
                    new EventHandlerProperties(
                            getInternalEventDispatcher().getEventHandlerManager(),
                            this);
        }

        return eventHandlerProperties;
    }

}
