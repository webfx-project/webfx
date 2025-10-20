package javafx.stage;

import com.sun.javafx.tk.StagePeerListener;
import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.kit.mapper.WebFxKitMapper;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.StagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasTitleProperty;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 * The JavaFX {@code Stage} class is the top level JavaFX container.
 * The primary Stage is constructed by the platform. Additional Stage
 * objects may be constructed by the application.
 *
 * <p>
 * Stage objects must be constructed and modified on the
 * JavaFX Application Thread.
 * </p>
 * <p>
 * Many of the {@code Stage} properties are read only because they can
 * be changed externally by the underlying platform and therefore must
 * not be bindable.
 * </p>
 *
 * <p><b>Style</b></p>
 * <p>
 * A stage has one of the following styles:
 * <ul>
 * <li>{@link StageStyle#DECORATED} - a stage with a solid white background and
 * platform decorations.</li>
 * <li>{@link StageStyle#UNDECORATED} - a stage with a solid white background
 * and no decorations.</li>
 * <li>{@link StageStyle#TRANSPARENT} - a stage with a transparent background
 * and no decorations.</li>
 * <li>{@link StageStyle#UTILITY} - a stage with a solid white background and
 * minimal platform decorations.</li>
 * </ul>
 * <p>The style must be initialized before the stage is made visible.</p>
 * <p>On some platforms decorations might not be available. For example, on
 * some mobile or embedded devices. In these cases a request for a DECORATED or
 * UTILITY window will be accepted, but no decorations will be shown. </p>
 *
 * <p><b>Owner</b></p>
 * <p>
 * A stage can optionally have an owner Window.
 * When a window is a stage's owner, it is said to be the parent of that stage.
 * <p>
 * Owned Stages are tied to the parent Window.
 * An owned stage will always be on top of its parent window.
 * When a parent window is closed or iconified, then all owned windows will be affected as well.
 * Owned Stages cannot be independantly iconified.
 * <p>
 * The owner must be initialized before the stage is made visible.
 *
 * <p><b>Modality</b></p>
 * <p>
 * A stage has one of the following modalities:
 * <ul>
 * <li>{@link Modality#NONE} - a stage that does not block any other window.</li>
 * <li>{@link Modality#WINDOW_MODAL} - a stage that blocks input events from
 * being delivered to all windows from its owner (parent) to its root.
 * Its root is the closest ancestor window without an owner.</li>
 * <li>{@link Modality#APPLICATION_MODAL} - a stage that blocks input events from
 * being delivered to all windows from the same application, except for those
 * from its child hierarchy.</li>
 * </ul>
 *
 * <p>When a window is blocked by a modal stage its Z-order relative to its ancestors
 * is preserved, and it receives no input events and no window activation events,
 * but continues to animate and render normally.
 * Note that showing a modal stage does not necessarily block the caller. The
 * {@link #show} method returns immediately regardless of the modality of the stage.
 * Use the {@link #showAndWait} method if you need to block the caller until
 * the modal stage is hidden (closed).
 * The modality must be initialized before the stage is made visible.</p>
 *
 * <p><b>Example:</b></p>
 *
 *
 <pre><code>
 import javafx.application.Application;
 import javafx.scene.Group;
 import javafx.scene.Scene;
 import javafx.scene.text.Font;
 import javafx.scene.text.Text;
 import javafx.stage.Stage;

 public class HelloWorld extends Application {

 &#64;Override public void start(Stage stage) {
 Text text = new Text(10, 40, "Hello World!");
 text.setFont(new Font(40));
 Scene scene = new Scene(new Group(text));

 stage.setTitle("Welcome to JavaFX!");
 stage.setScene(scene);
 stage.sizeToScene();
 stage.show();
 }

 public static void main(String[] args) {
 Application.launch(args);
 }
 }

 * </code></pre>
 * <p>produces the following on Windows:</p>
 * <p><img src="doc-files/Stage-win.png"/></p>
 *
 * <p>produces the following on Mac OSX:</p>
 * <p><img src="doc-files/Stage-mac.png"/></p>
 *
 * <p>produces the following on Linux:</p>
 * <p><img src="doc-files/Stage-linux.png"/></p>
 * @since JavaFX 2.0
 */
public class Stage extends Window implements HasTitleProperty {

    private static final StagePeerListener.StageAccessor STAGE_ACCESSOR = new StagePeerListener.StageAccessor() {

        @Override
        public void setIconified(Stage stage, boolean iconified) {
/*
            stage.iconifiedPropertyImpl().set(iconified);
*/
        }

        @Override
        public void setMaximized(Stage stage, boolean maximized) {
/*
            stage.maximizedPropertyImpl().set(maximized);
*/
        }

        @Override
        public void setResizable(Stage stage, boolean resizable) {
            ((ResizableProperty)stage.resizableProperty()).setNoInvalidate(resizable);
        }

        @Override
        public void setFullScreen(Stage stage, boolean fs) {
/*
            stage.fullScreenPropertyImpl().set(fs);
*/
        }

        @Override
        public void setAlwaysOnTop(Stage stage, boolean aot) {
/*
            stage.alwaysOnTopPropertyImpl().set(aot);
*/
        }
    };

    @Override
    protected StagePeer createPeer() {
        return WebFxKitMapper.createStagePeer(this);
    }

    private final StringProperty titleProperty = new SimpleStringProperty() {
        @Override
        protected void invalidated() {
            impl_getPeer().setTitle(getValue());
        }
    };

    @Override
    public StringProperty titleProperty() {
        return titleProperty;
    }

    /**
     * Creates a new instance of decorated {@code Stage}.
     *
     * @throws IllegalStateException if this constructor is called on a thread
     * other than the JavaFX Application Thread.
     */
    public Stage() {
        this(StageStyle.DECORATED);
    }

    /**
     * Creates a new instance of {@code Stage}.
     *
     * @param style The style of the {@code Stage}
     *
     * @throws IllegalStateException if this constructor is called on a thread
     * other than the JavaFX Application Thread.
     */
    public Stage(StageStyle style) {
        super();

        //Toolkit.getToolkit().checkFxUserThread();

        // Set the style
        initStyle(style);
    }

    /**
     * Specify the scene to be used on this stage.
     */
    @Override final public void setScene(Scene value) {
        //Toolkit.getToolkit().checkFxUserThread();
        super.setScene(value);
         /* Webfx addition: if the stage is not resizable (ex: browser window), we actually resize the scene to the
         stage right now so the user code can read back the scene width and height final values (ex: in application start
          method) before showing the stage. See ColorfulCircles demo as example. */
        if (!WebFxKitLauncher.getProvider().isStageProgrammaticallyRelocatableAndResizable())
            resizeSceneToStage();
    }

    void resizeSceneToStage() { // Webfx addition
        ((StagePeer) impl_getPeer()).changedWindowSize(); // Will do the job
    }

    /**
     * @inheritDoc
     */
    @Override public final void show() {
        super.show();
    }

    private boolean primary = false;

    /**
     * sets this stage to be the primary stage.
     * When run as an applet, this stage will appear in the broswer
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    public void impl_setPrimary(boolean primary) {
        this.primary = primary;
    }

    /**
     * Returns whether this stage is the primary stage.
     * When run as an applet, the primary stage will appear in the browser
     *
     * @return true if this stage is the primary stage for the application.
     */
    boolean isPrimary() {
        return primary;
    }


    /**
     * Shows this stage and waits for it to be hidden (closed) before returning
     * to the caller. This method temporarily blocks processing of the current
     * event, and starts a nested event loop to handle other events.
     * This method must be called on the FX Application thread.
     * <p>
     * A Stage is hidden (closed) by one of the following means:
     * <ul>
     * <li>the application calls the {@link #hide} or {@link #close} method on
     * this stage</li>
     * <li>this stage has a non-null owner window, and its owner is closed</li>
     * <li>the user closes the window via the window system (for example,
     * by pressing the close button in the window decoration)</li>
     * </ul>
     * </p>
     *
     * <p>
     * After the Stage is hidden, and the application has returned from the
     * event handler to the event loop, the nested event loop terminates
     * and this method returns to the caller.
     * </p>
     * <p>
     * For example, consider the following sequence of operations for different
     * event handlers, assumed to execute in the order shown below:
     * <pre>void evtHander1(...) {
     *     stage1.showAndWait();
     *     doSomethingAfterStage1Closed(...)
     * }
     *
     * void evtHander2(...) {
     *     stage1.hide();
     *     doSomethingElseHere(...)
     * }</pre>
     * evtHandler1 will block at the call to showAndWait. It will resume execution
     * after stage1 is hidden and the current event handler, in this case evtHandler2,
     * returns to the event loop. This means that doSomethingElseHere will
     * execute before doSomethingAfterStage1Closed.
     * </p>
     *
     * <p>
     * More than one stage may be shown with showAndWait. Each call
     * will start a new nested event loop. The stages may be hidden in any order,
     * but a particular nested event loop (and thus the showAndWait method
     * for the associated stage) will only terminate after all inner event loops
     * have also terminated.
     * </p>
     * <p>
     * For example, consider the following sequence of operations for different
     * event handlers, assumed to execute in the order shown below:
     * <ul>
     * <pre>void evtHander1() {
     *     stage1.showAndWait();
     *     doSomethingAfterStage1Closed(...)
     * }
     *
     * void evtHander2() {
     *     stage2.showAndWait();
     *     doSomethingAfterStage2Closed(...)
     * }
     *
     * void evtHander3() {
     *     stage1.hide();
     *     doSomethingElseHere(...)
     * }
     *
     * void evtHander4() {
     *     stage2.hide();
     *     doSomethingElseHereToo(...)
     * }</pre>
     * </ul>
     * evtHandler1 will block at the call to stage1.showAndWait, starting up
     * a nested event loop just like in the previous example. evtHandler2 will
     * then block at the call to stage2.showAndWait, starting up another (inner)
     * nested event loop. The first call to stage1.showAndWait will resume execution
     * after stage1 is hidden, but only after the inner nested event loop started
     * by stage2.showAndWait has terminated. This means that the call to
     * stage1.showAndWait won't return until after evtHandler2 has returned.
     * The order of execution is: stage1.showAndWait, stage2.showAndWait,
     * stage1.hide, doSomethingElseHere, stage2.hide, doSomethingElseHereToo,
     * doSomethingAfterStage2Closed, doSomethingAfterStage1Closed.
     * </p>
     *
     * <p>
     * This method must not be called on the primary stage or on a stage that
     * is already visible.
     * Additionally, it must either be called from an input event handler or
     * from the run method of a Runnable passed to
     * {@link javafx.application.Platform#runLater Platform.runLater}.
     * It must not be called during animation or layout processing.
     * </p>
     *
     * @throws IllegalStateException if this method is called on a thread
     *     other than the JavaFX Application Thread.
     * @throws IllegalStateException if this method is called during
     *     animation or layout processing.
     * @throws IllegalStateException if this method is called on the
     *     primary stage.
     * @throws IllegalStateException if this stage is already showing.
     * @since JavaFX 2.2
     */
    public void showAndWait() {

/*
        //Toolkit.getToolkit().checkFxUserThread();

        if (isPrimary()) {
            throw new IllegalStateException("Cannot call this method on primary stage");
        }

        if (isShowing()) {
            throw new IllegalStateException("Stage already visible");
        }

        if (!Toolkit.getToolkit().canStartNestedEventLoop()) {
            throw new IllegalStateException("showAndWait is not allowed during animation or layout processing");
        }

        // TODO: file a new bug; the following assertion can fail if this
        // method is called from an event handler that is listening to a
        // WindowEvent.WINDOW_HIDING event.
        assert !inNestedEventLoop;

        show();
        inNestedEventLoop = true;
        Toolkit.getToolkit().enterNestedEventLoop(this);
*/
    }

    private StageStyle style; // default is set in constructor

    /**
     * Specifies the style for this stage. This must be done prior to making
     * the stage visible. The style is one of: StageStyle.DECORATED,
     * StageStyle.UNDECORATED, StageStyle.TRANSPARENT, or StageStyle.UTILITY.
     *
     * @param style the style for this stage.
     *
     * @throws IllegalStateException if this property is set after the stage
     * has ever been made visible.
     *
     * @defaultValue StageStyle.DECORATED
     */
    public final void initStyle(StageStyle style) {
        if (hasBeenVisible) {
            throw new IllegalStateException("Cannot set style once stage has been set visible");
        }
        this.style = style;
    }

    /**
     * Retrieves the style attribute for this stage.
     *
     * @return the stage style.
     */
    public final StageStyle getStyle() {
        return style;
    }

    private Modality modality = Modality.NONE;

    /**
     * Specifies the modality for this stage. This must be done prior to making
     * the stage visible. The modality is one of: Modality.NONE,
     * Modality.WINDOW_MODAL, or Modality.APPLICATION_MODAL.
     *
     * @param modality the modality for this stage.
     *
     * @throws IllegalStateException if this property is set after the stage
     * has ever been made visible.
     *
     * @throws IllegalStateException if this stage is the primary stage.
     *
     * @defaultValue Modality.NONE
     */
    public final void initModality(Modality modality) {
        if (hasBeenVisible) {
            throw new IllegalStateException("Cannot set modality once stage has been set visible");
        }

        if (isPrimary()) {
            throw new IllegalStateException("Cannot set modality for the primary stage");
        }

        this.modality = modality;
    }

    /**
     * Retrieves the modality attribute for this stage.
     *
     * @return the modality.
     */
    public final Modality getModality() {
        return modality;
    }

    private Window owner = null;

    /**
     * Specifies the owner Window for this stage, or null for a top-level,
     * unowned stage. This must be done prior to making the stage visible.
     *
     * @param owner the owner for this stage.
     *
     * @throws IllegalStateException if this property is set after the stage
     * has ever been made visible.
     *
     * @throws IllegalStateException if this stage is the primary stage.
     *
     * @defaultValue null
     */
    public final void initOwner(Window owner) {
        if (hasBeenVisible) {
            throw new IllegalStateException("Cannot set owner once stage has been set visible");
        }

        if (isPrimary()) {
            throw new IllegalStateException("Cannot set owner for the primary stage");
        }

        this.owner = owner;

/*
        final Scene sceneValue = getScene();
        if (sceneValue != null) {
            SceneHelper.parentEffectiveOrientationInvalidated(sceneValue);
        }
*/
    }

    /**
     * Retrieves the owner Window for this stage, or null for an unowned stage.
     *
     * @return the owner Window.
     */
    public final Window getOwner() {
        return owner;
    }

    /**
     * Defines whether the {@code Stage} is resizable or not by the user.
     * Programatically you may still change the size of the Stage. This is
     * a hint which allows the implementation to optionally make the Stage
     * resizable by the user.
     * <p>
     * <b>Warning:</b> Since 8.0 the property cannot be bound and will throw
     * {@code RuntimeException} on an attempt to do so. This is because
     * the setting of resizable is asynchronous on some systems or generally
     * might be set by the system / window manager.
     * <br>
     * Bidirectional binds are still allowed, as they don't block setting of the
     * property by the system.
     *
     * @defaultValue true
     */
    private BooleanProperty resizable;

    public final void setResizable(boolean value) {
        resizableProperty().setValue(value);
    }

    public final boolean isResizable() {
        return resizable == null ? true : resizable.getValue();
    }

    public final BooleanProperty resizableProperty() {
        if (resizable == null) {
            resizable = new ResizableProperty();
        }
        return resizable;
    }

    //We cannot return ReadOnlyProperty in resizable, as this would be
    // backward incompatible. All we can do is to create this custom property
    // implementation that disallows binds
    private class ResizableProperty extends SimpleBooleanProperty {
        private boolean noInvalidate;

        public ResizableProperty() {
            super(Stage.this, "resizable", true);
        }

        void setNoInvalidate(boolean value) {
            noInvalidate = true;
            set(value);
            noInvalidate = false;
        }

        @Override
        protected void invalidated() {
            if (noInvalidate) {
                return;
            }

            if (impl_peer != null) {
                applyBounds();
                //impl_peer.setResizable(get());
            }
        }

        @Override
        public void bind(ObservableValue<? extends Boolean> rawObservable) {
            throw new RuntimeException("Resizable property cannot be bound");
        }

    }

    /**
     * Closes this {@code Stage}.
     * This call is equivalent to {@code hide()}.
     */
    public void close() {
        hide();
    }

    @Override
    Window getWindowOwner() {
        return getOwner();
    }

    /**
     * @treatAsPrivate implementation detail
     */
    //@Deprecated
    @Override protected void impl_visibleChanging(boolean value) {
        super.impl_visibleChanging(value);
        //Toolkit toolkit = Toolkit.getToolkit();
        if (value && (impl_peer == null)) {
            // Setup the peer
/*
            Window window = getOwner();
            TKStage tkStage = (window == null ? null : window.impl_getPeer());
            Scene scene = getScene();
            boolean rtl = scene != null && scene.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;

            StageStyle stageStyle = getStyle();
            if (stageStyle == StageStyle.TRANSPARENT) {
                final SecurityManager securityManager =
                        System.getSecurityManager();
                if (securityManager != null) {
                    try {
                        securityManager.checkPermission(new AllPermission());
                    } catch (final SecurityException e) {
                        stageStyle = StageStyle.UNDECORATED;
                    }
                }
            }
*/
/*
            impl_peer = toolkit.createTKStage(this, isSecurityDialog(), stageStyle, isPrimary(),
                    getModality(), tkStage, rtl, acc);
            impl_peer.setMinimumSize((int) Math.ceil(getMinWidth()),
                    (int) Math.ceil(getMinHeight()));
            impl_peer.setMaximumSize((int) Math.floor(getMaxWidth()),
                    (int) Math.floor(getMaxHeight()));
*/
            peerListener = new StagePeerListener(this, STAGE_ACCESSOR);
            // Insert this into stages so we have a references to all created stages
/*
            stages.add(this);
*/
        }
    }

    private final BooleanProperty fullScreenProperty = new SimpleBooleanProperty();

    public BooleanProperty fullScreenPropertyImpl() { // Will be accessed and updated by WebFxKitLauncher
        return fullScreenProperty;
    }

    public final ReadOnlyBooleanProperty fullScreenProperty() {
        return fullScreenPropertyImpl();
    }

    public final void setFullScreen(boolean value) {
        if (value) {
            Scene scene = getScene();
            if (scene != null)
                WebFxKitLauncher.requestNodeFullscreen(scene.getRoot()); // will update fullScreenProperty if succeeded
        } else {
            WebFxKitLauncher.exitFullscreen(); // will fullScreenProperty if succeeded;
        }
/*
        fullScreenPropertyImpl().set(value);
        Toolkit.getToolkit().checkFxUserThread();
        if (getPeer() != null)
            getPeer().setFullScreen(value);
*/
    }

    public final boolean isFullScreen() {
        return fullScreenPropertyImpl().get();
    }

    public void setFullScreenExitHint(String hint) {} // ignored on purpose (not possible in browser)

    public void setMinWidth(double minWidth) {} // ignored on purpose (not possible in browser)

    public void setMinHeight(double minHeight) {} // ignored on purpose (not possible in browser)

    private final ObservableList<Image> icons = FXCollections.observableArrayList();
    public final ObservableList<Image> getIcons() {
        return icons;
    }
}
