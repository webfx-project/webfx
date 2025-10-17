package javafx.stage;

import com.sun.javafx.stage.WindowEventDispatcher;
import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.stage.WindowPeerListener;
import com.sun.javafx.tk.TKPulseListener;
import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.kit.mapper.WebFxKitMapper;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.WindowPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasHeightProperty;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasSceneProperty;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasWidthProperty;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.scheduler.Scheduled;
import dev.webfx.platform.uischeduler.AnimationFramePass;
import dev.webfx.platform.uischeduler.UiScheduler;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;

/**
 * @author Bruno Salmon
 */
public class Window implements EventTarget,
        HasSceneProperty,
        HasWidthProperty,
        HasHeightProperty {

    static {
        WindowHelper.setWindowAccessor(
                new WindowHelper.WindowAccessor() {
                    /**
                     * Allow window peer listeners to directly change reported
                     * window location and size without changing the xExplicit,
                     * yExplicit, widthExplicit and heightExplicit values.
                     */
                    @Override
                    public void notifyLocationChanged(
                            Window window, double x, double y) {
                        window.notifyLocationChanged(x, y);
                    }

                    @Override
                    public void notifySizeChanged(Window window,
                                                  double width,
                                                  double height) {
                        window.notifySizeChanged(width, height);
                    }

/*
                    @Override
                    public void notifyScreenChanged(Window window,
                                                    Object from,
                                                    Object to) {
                        window.notifyScreenChanged(from, to);
                    }

                    @Override
                    public float getUIScale(Window window) {
                        TKStage peer = window.impl_peer;
                        return peer == null ? 1.0f : peer.getUIScale();
                    }

                    @Override
                    public float getRenderScale(Window window) {
                        TKStage peer = window.impl_peer;
                        return peer == null ? 1.0f : peer.getRenderScale();
                    }

                    @Override
                    public ReadOnlyObjectProperty<Screen> screenProperty(Window window) {
                        return window.screenProperty();
                    }

                    @Override
                    public AccessControlContext getAccessControlContext(Window window) {
                        return window.acc;
                    }
*/
                });
    }

    public Window() {
        // necessary for WindowCloseRequestHandler
        initializeInternalEventDispatcher();
    }

    /**
     * The listener that gets called by peer. It's also responsible for
     * window size/location synchronization with the window peer, which
     * occurs on every pulse.
     */
    //@Deprecated
    protected WindowPeerListener peerListener;

    /**
     * The peer of this Stage. All external access should be
     * made though getPeer(). Implementors note: Please ensure that this
     * variable is defined *after* style and *before* the other variables so
     * that style has been initialized prior to this call, and so that
     * impl_peer is initialized prior to subsequent initialization.
     */
    //@Deprecated
    protected volatile WindowPeer impl_peer;

    private TKBoundsConfigurator peerBoundsConfigurator =
            new TKBoundsConfigurator();

    /**
     * Get Stage's peer
     */
    //@Deprecated
    public WindowPeer impl_getPeer() {
        if (impl_peer == null)
            impl_peer = createPeer();
        return impl_peer;
    }


    protected WindowPeer createPeer() {
        return WebFxKitMapper.createWindowPeer(this);
    }

    private final ObjectProperty<Scene> sceneProperty = new SimpleObjectProperty<Scene>() {
        @Override
        protected void invalidated() {
            onSceneChanged();
            //managePulse();
        }
    };
    @Override
    public ObjectProperty<Scene> sceneProperty() {
        return sceneProperty;
    }

    private boolean widthExplicit = false;

    /**
     * The width of this {@code Stage}. Changing this attribute will narrow or
     * widen the width of the {@code Stage}. Changing this
     * attribute will not visually affect a {@code Stage} while
     * {@code fullScreen} is true, but will be honored by the {@code Stage} once
     * {@code fullScreen} becomes false. This value includes any and all
     * decorations which may be added by the Operating System such as resizable
     * frame handles. Typical applications will set the {@link javafx.scene.Scene} width
     * instead.
     * <p>
     * The property is read only because it can be changed externally
     * by the underlying platform and therefore must not be bindable.
     * </p>
     */
    private DoubleProperty width =
            new SimpleDoubleProperty(this, "width", Double.NaN);

    public final void setWidth(double value) {
        width.setValue(value);
        peerBoundsConfigurator.setWindowWidth(value);
        widthExplicit = true;
    }
    //public final double getWidth() { return width.getValue(); }
    public final DoubleProperty widthProperty() { return width/*.getReadOnlyProperty()*/; }

    private boolean heightExplicit = false;
    /**
     * The height of this {@code Stage}. Changing this attribute will shrink
     * or heighten the height of the {@code Stage}. Changing this
     * attribute will not visually affect a {@code Stage} while
     * {@code fullScreen} is true, but will be honored by the {@code Stage} once
     * {@code fullScreen} becomes false. This value includes any and all
     * decorations which may be added by the Operating System such as the title
     * bar. Typical applications will set the {@link javafx.scene.Scene} height instead.
     * <p>
     * The property is read only because it can be changed externally
     * by the underlying platform and therefore must not be bindable.
     * </p>
     */
    private DoubleProperty height =
            new SimpleDoubleProperty(this, "height", Double.NaN);

    public final void setHeight(double value) {
        height.setValue(value);
        peerBoundsConfigurator.setWindowHeight(value);
        heightExplicit = true;
    }
    //public final double getHeight() { return height.get(); }
    public final DoubleProperty heightProperty() { return height/*.getReadOnlyProperty()*/; }

    /**
     * Notification from the windowing system that the window's size has
     * changed.
     *
     * @param newWidth the new window width
     * @param newHeight the new window height
     */
    public void notifySizeChanged(double newWidth, double newHeight) {
        width.setValue(newWidth);
        height.setValue(newHeight);
    }

    private boolean xExplicit = false;
    /**
     * The horizontal location of this {@code Stage} on the screen. Changing
     * this attribute will move the {@code Stage} horizontally. Changing this
     * attribute will not visually affect a {@code Stage} while
     * {@code fullScreen} is true, but will be honored by the {@code Stage} once
     * {@code fullScreen} becomes false.
     */
    private DoubleProperty x =
            new SimpleDoubleProperty(this, "x", Double.NaN);

    public final void setX(double value) {
        setXInternal(value);
    }
    public final double getX() { return x.getValue(); }
    public final ReadOnlyDoubleProperty xProperty() { return x/*.getReadOnlyProperty()*/; }

    void setXInternal(double value) {
        x.setValue(value);
        peerBoundsConfigurator.setX(value, 0);
        xExplicit = true;
    }

    private boolean yExplicit = false;
    /**
     * The vertical location of this {@code Stage} on the screen. Changing this
     * attribute will move the {@code Stage} vertically. Changing this
     * attribute will not visually affect a {@code Stage} while
     * {@code fullScreen} is true, but will be honored by the {@code Stage} once
     * {@code fullScreen} becomes false.
     */
    private DoubleProperty y =
            new SimpleDoubleProperty(this, "y", Double.NaN);

    public final void setY(double value) {
        setYInternal(value);
    }
    public final double getY() { return y.getValue(); }
    public final ReadOnlyDoubleProperty yProperty() { return y/*.getReadOnlyProperty()*/; }

    void setYInternal(double value) {
        y.setValue(value);
        peerBoundsConfigurator.setY(value, 0);
        yExplicit = true;
    }

    /**
     * Notification from the windowing system that the window's position has
     * changed.
     *
     * @param newX the new window x position
     * @param newY the new window y position
     */
    void notifyLocationChanged(double newX, double newY) {
        x.setValue(newX);
        y.setValue(newY);
    }

    /**
     * Whether or not this {@code Stage} is showing (that is, open on the
     * user's system). The Stage might be "showing", yet the user might not
     * be able to see it due to the Stage being rendered behind another window
     * or due to the Stage being positioned off the monitor.
     *
     * @defaultValue false
     */
    private BooleanProperty showing = new SimpleBooleanProperty(false) {
        private boolean oldVisible;
        private Scheduled pulseScheduled;
        //private boolean firstShowing = true;

        @Override protected void invalidated() {
            final boolean newVisible = get();
            if (oldVisible == newVisible) {
                return;
            }

            if (!oldVisible && newVisible) {
                fireEvent(new WindowEvent(Window.this, WindowEvent.WINDOW_SHOWING));
            } else {
                fireEvent(new WindowEvent(Window.this, WindowEvent.WINDOW_HIDING));
            }

            impl_visibleChanging(newVisible);
            if (newVisible) {
                hasBeenVisible = true;
                //windowQueue.add(Window.this);
            } else {
                //windowQueue.remove(Window.this);
            }
            //Toolkit tk = Toolkit.getToolkit();
            WindowPeer impl_peer = impl_getPeer();
            if (impl_peer != null) {
                if (newVisible) {
                    if (peerListener == null) {
                        peerListener = new WindowPeerListener(Window.this);
                    }

                    // Setup listener for changes coming back from peer
                    impl_peer.setTKStageListener(peerListener);
                    // Register pulse listener
                    // tk.addStageTkPulseListener(peerBoundsConfigurator);
                    if (pulseScheduled == null)
                        pulseScheduled = UiScheduler.schedulePeriodicInAnimationFrame(peerBoundsConfigurator::pulse, AnimationFramePass.SCENE_PULSE_LAYOUT_PASS);


                    if (getScene() != null) {
                        getScene().impl_initPeer();
                        impl_getPeer().onSceneRootChanged(); // This call is necessary if this is the second time the window is shown (ex: second time a context menu is shown) otherwise this (new) window peer has no content (children not inserted into the DOM)
                        //impl_peer.setScene(getScene().getPeer());
                        if (Window.this instanceof Stage && !WebFxKitLauncher.getProvider().isStageProgrammaticallyRelocatableAndResizable())
                            ((Stage) Window.this).resizeSceneToStage();
                        else
                            getScene().impl_preferredSize();
/*
                        // Ugly webfx workaround to fix a wrong window positioning that occurs on first showing while the node sizes are not yet correct
                        if (firstShowing && Window.this != FxKitLauncher.getPrimaryStage()) {
                            impl_peer.setBounds(100_000, 100_000, true, true, -1, -1, -1, -1, 0, 0);
                            impl_peer.setVisible(true);
                            UiScheduler.scheduleDelay(200, () -> {
                                x.setValue(Double.NaN); xExplicit = false;
                                y.setValue(Double.NaN); yExplicit = false;
                                peerBoundsConfigurator.setDirty();
                                invalidated();
                            });
                            firstShowing = false;
                            return;
                        }
*/
                    }

                    // Set peer bounds
                    if ((getScene() != null) && (!widthExplicit || !heightExplicit)) {
                        adjustSize(true);
                    } else {
                        peerBoundsConfigurator.setSize(getWidth(), getHeight(), -1, -1);
                    }

                    if (!xExplicit && !yExplicit) {
                        centerOnScreen();
                    } else {
                        peerBoundsConfigurator.setLocation(getX(), getY(), 0, 0);
                    }

                    // set peer bounds before the window is shown
                    applyBounds();

                    //impl_peer.setOpacity((float)getOpacity());

                    impl_peer.setVisible(true);
                    fireEvent(new WindowEvent(Window.this, WindowEvent.WINDOW_SHOWN));
                } else {
                    impl_peer.setVisible(false);

                    // Call listener
                    fireEvent(new WindowEvent(Window.this, WindowEvent.WINDOW_HIDDEN));

                    if (getScene() != null) {
                        //impl_peer.setScene(null);
                        getScene().impl_disposePeer();
                        //StyleManager.getInstance().forget(getScene());
                    }

                    // Remove toolkit pulse listener
                    // tk.removeStageTkPulseListener(peerBoundsConfigurator);
                    if (pulseScheduled != null) {
                        pulseScheduled.cancel();
                        pulseScheduled = null;
                    }
                    // Remove listener for changes coming back from peer
                    impl_peer.setTKStageListener(null);


                    // Notify peer
                    //impl_peer.close();
                }
            }
            if (newVisible) {
                UiScheduler.requestNextScenePulse();
            }
            impl_visibleChanged(newVisible);

            if (sizeToScene) {
                if (newVisible) {
                    // Now that the visibleChanged has completed, the insets of the window
                    // might have changed (e.g. due to setResizable(false)). Reapply the
                    // sizeToScene() request if needed to account for the new insets.
                    sizeToScene();
                }

                // Reset the flag unconditionally upon visibility changes
                sizeToScene = false;
            }

            oldVisible = newVisible;
        }

        @Override
        public Object getBean() {
            return Window.this;
        }

        @Override
        public String getName() {
            return "showing";
        }
    };
    private void setShowing(boolean value) {
        //Toolkit.getToolkit().checkFxUserThread();
        showing.setValue(value);
    }
    public final boolean isShowing() { return showing.getValue(); }
    public final ReadOnlyBooleanProperty showingProperty() { return showing/*.getReadOnlyProperty()*/; }

    // flag indicating whether this window has ever been made visible.
    boolean hasBeenVisible = false;

    /**
     * Attempts to show this Window by setting visibility to true
     *
     * @throws IllegalStateException if this method is called on a thread
     * other than the JavaFX Application Thread.
     */
    protected void show() {
        setShowing(true);
    }

    /**
     * Attempts to hide this Window by setting the visibility to false.
     *
     * @throws IllegalStateException if this method is called on a thread
     * other than the JavaFX Application Thread.
     */
    public void hide() {
        setShowing(false);
    }

    /**
     * This can be replaced by listening for the onShowing/onHiding events
     * @treatAsPrivate implementation detail
     */
    //@Deprecated
    protected void impl_visibleChanging(boolean visible) {
/*
        if (visible && (getScene() != null)) {
            getScene().getRoot().impl_reapplyCSS();
        }
*/
    }

    /**
     * This can be replaced by listening for the onShown/onHidden events
     */
    //@Deprecated
    protected void impl_visibleChanged(boolean visible) {
        assert impl_peer != null;
        if (!visible) {
            peerListener = null;
            impl_peer = null;
        }
    }

    /**
     * Whether or not this {@code Window} has the keyboard or input focus.
     * <p>
     * The property is read only because it can be changed externally
     * by the underlying platform and therefore must not be bindable.
     * </p>
     *
     * @profile common
     */
    private BooleanProperty focused = new SimpleBooleanProperty(false)/* {
        @Override protected void invalidated() {
            focusChanged(get());
        }

        @Override
        public Object getBean() {
            return Window.this;
        }

        @Override
        public String getName() {
            return "focused";
        }
    }*/;

    /**
     * @treatAsPrivate
     * @deprecated
     */
    @Deprecated
    public final void setFocused(boolean value) { focused.setValue(value); }

    /**
     * Requests that this {@code Window} get the input focus.
     */
    public final void requestFocus() {
/*
        if (impl_peer != null) {
            impl_peer.requestFocus();
        }
*/
    }
    public final boolean isFocused() { return focused.getValue(); }
    public final ReadOnlyBooleanProperty focusedProperty() { return focused/*.getReadOnlyProperty()*/; }

    private void onSceneChanged() {
        Scene scene = getScene();
        if (scene != null) {
            scene.impl_setWindow(this);
            // Notifying the peer about the change
            FXProperties.runNowAndOnPropertyChange(() -> {
                if (impl_peer != null)
                    impl_peer.onSceneRootChanged();
            }, scene.rootProperty());
        }
    }

    /**
     * Indicates if a user requested the window to be sized to match the scene
     * size.
     */
    private boolean sizeToScene = false;
    /**
     * Set the width and height of this Window to match the size of the content
     * of this Window's Scene.
     */
    public void sizeToScene() {
        if (getScene() != null && impl_getPeer() != null) {
            getScene().impl_preferredSize();
            adjustSize(false);
        } else {
            // Remember the request to reapply it later if needed
            sizeToScene = true;
        }
    }

    private void adjustSize(boolean selfSizePriority) {
        if (getScene() == null) {
            return;
        }
        if (impl_getPeer() != null) {
            double sceneWidth = getScene().getWidth();
            double cw = (sceneWidth > 0) ? sceneWidth : -1;
            double w = -1;
            if (selfSizePriority && widthExplicit) {
                w = getWidth();
            } else if (cw <= 0) {
                w = widthExplicit ? getWidth() : -1;
            } else {
                widthExplicit = false;
            }
            double sceneHeight = getScene().getHeight();
            double ch = (sceneHeight > 0) ? sceneHeight : -1;
            double h = -1;
            if (selfSizePriority && heightExplicit) {
                h = getHeight();
            } else if (ch <= 0) {
                h = heightExplicit ? getHeight() : -1;
            } else {
                heightExplicit = false;
            }

            peerBoundsConfigurator.setSize(w, h, cw, ch);
            applyBounds();
        }
    }

    private static final float CENTER_ON_SCREEN_X_FRACTION = 1.0f / 2;
    private static final float CENTER_ON_SCREEN_Y_FRACTION = 1.0f / 3;

    /**
     * Sets x and y properties on this Window so that it is centered on the
     * curent screen.
     * The current screen is determined from the intersection of current window bounds and
     * visual bounds of all screens.
     */
    public void centerOnScreen() {
        xExplicit = false;
        yExplicit = false;
        if (!Double.isNaN(getWidth()) && !Double.isNaN(getHeight())) {
            Rectangle2D bounds = getWindowScreen().getVisualBounds();
            double centerX = bounds.getMinX() + (bounds.getWidth() - getWidth()) * CENTER_ON_SCREEN_X_FRACTION;
            double centerY =  bounds.getMinY() + (bounds.getHeight() - getHeight()) * CENTER_ON_SCREEN_Y_FRACTION;

            x.setValue(centerX);
            y.setValue(centerY);
            peerBoundsConfigurator.setLocation(centerX, centerY, CENTER_ON_SCREEN_X_FRACTION, CENTER_ON_SCREEN_Y_FRACTION);
            applyBounds();
        }
    }

    final void applyBounds() {
        peerBoundsConfigurator.apply();
    }

    Window getWindowOwner() {
        return null;
    }

    private Screen getWindowScreen() {
        Window window = this;
        do {
            if (!Double.isNaN(window.getX())
                    && !Double.isNaN(window.getY())
                    && !Double.isNaN(window.getWidth())
                    && !Double.isNaN(window.getHeight())) {
                return Screen.from(
                        new Rectangle2D(window.getX(),
                                window.getY(),
                                window.getWidth(),
                                window.getHeight()));
            }

            window = window.getWindowOwner();
        } while (window != null);

        return Screen.getPrimary();
    }

    /**
     * Called when there is an external request to close this {@code Window}.
     * The installed event handler can prevent window closing by consuming the
     * received event.
     */
    private ObjectProperty<EventHandler<WindowEvent>> onCloseRequest;
    public final void setOnCloseRequest(EventHandler<WindowEvent> value) {
        onCloseRequestProperty().set(value);
    }
    public final EventHandler<WindowEvent> getOnCloseRequest() {
        return (onCloseRequest != null) ? onCloseRequest.get() : null;
    }
    public final ObjectProperty<EventHandler<WindowEvent>>
    onCloseRequestProperty() {
        if (onCloseRequest == null) {
            onCloseRequest = new ObjectPropertyBase<EventHandler<WindowEvent>>() {
                @Override protected void invalidated() {
                    setEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, get());
                }

                @Override
                public Object getBean() {
                    return Window.this;
                }

                @Override
                public String getName() {
                    return "onCloseRequest";
                }
            };
        }
        return onCloseRequest;
    }

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

    private WindowEventDispatcher internalEventDispatcher;

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
    public final <T extends Event> void addEventHandler(
            final EventType<T> eventType,
            final EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().getEventHandlerManager()
                .addEventHandler(eventType, eventHandler);
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
    public final <T extends Event> void removeEventHandler(
            final EventType<T> eventType,
            final EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().getEventHandlerManager()
                .removeEventHandler(eventType,
                        eventHandler);
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
    public final <T extends Event> void addEventFilter(
            final EventType<T> eventType,
            final EventHandler<? super T> eventFilter) {
        getInternalEventDispatcher().getEventHandlerManager()
                .addEventFilter(eventType, eventFilter);
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
    public final <T extends Event> void removeEventFilter(
            final EventType<T> eventType,
            final EventHandler<? super T> eventFilter) {
        getInternalEventDispatcher().getEventHandlerManager()
                .removeEventFilter(eventType, eventFilter);
    }

    /**
     * Sets the handler to use for this event type. There can only be one such handler
     * specified at a time. This handler is guaranteed to be called first. This is
     * used for registering the user-defined onFoo event handlers.
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

    WindowEventDispatcher getInternalEventDispatcher() {
        initializeInternalEventDispatcher();
        return internalEventDispatcher;
    }

    private void initializeInternalEventDispatcher() {
        if (internalEventDispatcher == null) {
            internalEventDispatcher = createInternalEventDispatcher();
            eventDispatcher = new SimpleObjectProperty<EventDispatcher>(
                    this,
                    "eventDispatcher",
                    internalEventDispatcher);
        }
    }

    WindowEventDispatcher createInternalEventDispatcher() {
        return new WindowEventDispatcher(this);
    }

    /**
     * Fires the specified event.
     * <p>
     * This method must be called on the FX user thread.
     *
     * @param event the event to fire
     */
    public final void fireEvent(Event event) {
        Event.fireEvent(this, event);
    }

    // PENDING_DOC_REVIEW
    /**
     * Construct an event dispatch chain for this window.
     *
     * @param tail the initial chain to build from
     * @return the resulting event dispatch chain for this window
     */
    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        if (eventDispatcher != null) {
            EventDispatcher eventDispatcherValue = eventDispatcher.get();
            if (eventDispatcherValue != null)
                tail = tail.prepend(eventDispatcherValue);
        }
        return tail;
    }

    /**
     * Caches all requested bounds settings and applies them at once during
     * the next pulse.
     */
    private final class TKBoundsConfigurator implements TKPulseListener {
        private double x;
        private double y;
        private float xGravity;
        private float yGravity;
        private double windowWidth;
        private double windowHeight;
        private double clientWidth;
        private double clientHeight;

        private boolean dirty;

        TKBoundsConfigurator() {
            reset();
        }

        public void setX(final double x, final float xGravity) {
            this.x = x;
            this.xGravity = xGravity;
            setDirty();
        }

        public void setY(final double y, final float yGravity) {
            this.y = y;
            this.yGravity = yGravity;
            setDirty();
        }

        void setWindowWidth(final double windowWidth) {
            this.windowWidth = windowWidth;
            setDirty();
        }

        void setWindowHeight(final double windowHeight) {
            this.windowHeight = windowHeight;
            setDirty();
        }

        public void setClientWidth(final double clientWidth) {
            this.clientWidth = clientWidth;
            setDirty();
        }

        public void setClientHeight(final double clientHeight) {
            this.clientHeight = clientHeight;
            setDirty();
        }

        void setLocation(final double x,
                                final double y,
                                final float xGravity,
                                final float yGravity) {
            this.x = x;
            this.y = y;
            this.xGravity = xGravity;
            this.yGravity = yGravity;
            setDirty();
        }

        void setSize(final double windowWidth,
                            final double windowHeight,
                            final double clientWidth,
                            final double clientHeight) {
            this.windowWidth = windowWidth;
            this.windowHeight = windowHeight;
            this.clientWidth = clientWidth;
            this.clientHeight = clientHeight;
            setDirty();
        }

        public void apply() {
            if (dirty) {
                impl_getPeer().setBounds((float) (Double.isNaN(x) ? 0 : x),
                        (float) (Double.isNaN(y) ? 0 : y),
                        !Double.isNaN(x),
                        !Double.isNaN(y),
                        (float) windowWidth,
                        (float) windowHeight,
                        (float) clientWidth,
                        (float) clientHeight,
                        xGravity, yGravity);

                reset();
            }
        }

        @Override
        public void pulse() {
            apply();
        }

        private void reset() {
            x = Double.NaN;
            y = Double.NaN;
            xGravity = 0;
            yGravity = 0;
            windowWidth = -1;
            windowHeight = -1;
            clientWidth = -1;
            clientHeight = -1;
            dirty = false;
        }

        private void setDirty() {
            if (!dirty) {
                UiScheduler.requestNextScenePulse();
                dirty = true;
            }
        }
    }

}
