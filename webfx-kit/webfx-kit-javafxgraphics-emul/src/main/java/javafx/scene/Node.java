package javafx.scene;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geometry.BoundsUtils;
import com.sun.javafx.scene.EventHandlerProperties;
import com.sun.javafx.scene.NodeEventDispatcher;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.TraversalMethod;
import com.sun.javafx.util.TempState;
import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.HasSizeChangedCallback;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.measurable.Measurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.*;
import dev.webfx.platform.uischeduler.UiScheduler;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.LayoutFlags;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import static javafx.scene.layout.PreferenceResizableNode.USE_COMPUTED_SIZE;
import static javafx.scene.layout.PreferenceResizableNode.USE_PREF_SIZE;
/**
 * @author Bruno Salmon
 */
public abstract class Node implements INode, EventTarget, Styleable {


    // This method is called when the node peer has been created, inserted to the DOM, and bound to this node (i.e.,
    // reacting to the property changes to update the HTML mapping). The node may need to do something at this point.
    void onNodePeerBound() { }

    private final ObjectProperty<Parent> parentProperty = new SimpleObjectProperty<>(); {
        parentProperty.addListener((observable, oldParent, newParent) -> {
            // If the node is transferred from one parent to another, we need to remove it from the previous parent
            if (oldParent != null)
                oldParent.getChildren().remove(Node.this);
            // The child automatically inherits the scene from the parent (or set to null if it has no parent anymore)
            Scene newScene = newParent == null ? null : newParent.getScene();
            setScene(newScene); // Note: the scene will be propagated to the possible children in the scene listener (see below)
        });
    }
    @Override
    public ObjectProperty<Parent> parentProperty() {
        return parentProperty;
    }

    private final ObjectProperty<Scene> scene = new SimpleObjectProperty<>() {
        @Override
        protected void invalidated() {
            Scene newScene = getScene();
            // Initialising the event dispatcher if not already done
            if (newScene != null)
                newScene.initializeInternalEventDispatcher();
            // Propagating the scene to the children
            if (Node.this instanceof Parent) {
                for (Node child : ((Parent) Node.this).getChildren()) {
                    child.setScene(newScene);
                }
                // If this parent is inserted into a new scene, we need to update the scene graph -> DOM mapping.
                if (newScene != null)
                    newScene.updateParentAndChildrenPeers((Parent) Node.this, null);
            }
            // Also to the clip (if set)
            Node clip = getClip();
            if (clip != null)
                clip.setScene(newScene);
        }
    };

    public ReadOnlyObjectProperty<Scene> sceneProperty() {
        return scene;
    }

    public Scene getScene() {
        return scene.getValue();
    }

    public void setScene(Scene scene) {
        this.scene.setValue(scene);
    }

    private final BooleanProperty managedProperty = new SimpleBooleanProperty(true) {
        @Override
        protected void invalidated() {
            Parent parent = getParent();
            if (parent != null)
                parent.managedChildChanged();
            notifyManagedChanged();
        }
    };

    @Override
    public BooleanProperty managedProperty() {
        return managedProperty;
    }

    private final BooleanProperty mouseTransparentProperty = new SimpleBooleanProperty(false);
    @Override
    public BooleanProperty mouseTransparentProperty() {
        return mouseTransparentProperty;
    }

    /**
     * Called whenever the "managed" flag has changed. This is only
     * used by Parent as an optimization to keep track of whether a
     * Parent node is a layout root or not.
     */
    void notifyManagedChanged() { }

    public final void setOnContextMenuRequested(
            EventHandler<? super ContextMenuEvent> value) {
        onContextMenuRequestedProperty().set(value);
    }

    public final EventHandler<? super ContextMenuEvent> getOnContextMenuRequested() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.onContextMenuRequested();
    }

    /**
     * Defines a function to be called when a context menu
     * has been requested on this {@code Node}.
     * @return the event handler that is called when a context menu has been
     * requested on this {@code Node}
     * @since JavaFX 2.1
     */
    public final ObjectProperty<EventHandler<? super ContextMenuEvent>>
    onContextMenuRequestedProperty() {
        return getEventHandlerProperties().onContextMenuRequestedProperty();
    }

    /**
     * Defines a function to be called when a mouse button has been clicked
     * (pressed and released) on this {@code Node}.
     */
    public final ObjectProperty<EventHandler<? super MouseEvent>>
    onMouseClickedProperty() {
        return getEventHandlerProperties().onMouseClickedProperty();
    }

    private final BooleanProperty visibleProperty = new SimpleBooleanProperty(true);
    @Override
    public BooleanProperty visibleProperty() {
        return visibleProperty;
    }

    private final ObjectProperty<Cursor> cursorProperty = new SimpleObjectProperty<>(impl_cssGetCursorInitialValue());
    public final void setCursor(Cursor value) {
        cursorProperty().set(value);
    }

    public final Cursor getCursor() {
        return cursorProperty.getValue();
    }

    /**
     * Defines the mouse cursor for this {@code Node} and subnodes. If null,
     * then the cursor of the first parent node with a non-null cursor will be
     * used. If no Node in the scene graph defines a cursor, then the cursor
     * of the {@code Scene} will be used.
     *
     * @defaultValue null
     */
    public final ObjectProperty<Cursor> cursorProperty() {
        return cursorProperty;
    }

    /**
     * Not everything uses the default value of null for cursor.
     * This method provides a way to have them return the correct initial value.
     * @treatAsPrivate implementation detail
     */
    @Deprecated
    protected /*do not make final*/ Cursor impl_cssGetCursorInitialValue() {
        return null;
    }


    private final DoubleProperty opacityProperty = new SimpleDoubleProperty(1d);
    @Override
    public DoubleProperty opacityProperty() {
        return opacityProperty;
    }

    private final ObjectProperty<Node> clipProperty = new SimpleObjectProperty<Node>() {
        @Override
        protected void invalidated() {
            setScene(getScene()); // This will propagate the scene into the clip
        }
    };

    @Override
    public ObjectProperty<Node> clipProperty() {
        return clipProperty;
    }

    private final ObjectProperty<BlendMode> blendModeProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<BlendMode> blendModeProperty() {
        return blendModeProperty;
    }

    private final ObjectProperty<Effect> effectProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<Effect> effectProperty() {
        return effectProperty;
    }

    private final DoubleProperty layoutXProperty = new SimpleDoubleProperty();
    @Override
    public DoubleProperty layoutXProperty() {
        return layoutXProperty;
    }

    private final DoubleProperty layoutYProperty = new SimpleDoubleProperty();
    @Override
    public DoubleProperty layoutYProperty() {
        return layoutYProperty;
    }

    // Cache property (only for JavaFX API compatibility - currently ignored by implementation)
    private BooleanProperty cacheProperty;

    public final BooleanProperty cacheProperty() {
        if (cacheProperty == null)
            cacheProperty = new SimpleBooleanProperty();
        return cacheProperty;
    }

    public final void setCache(boolean value) {
        cacheProperty().setValue(value);
    }

    public final boolean isCache() {
        return cacheProperty().getValue();
    }

    // CacheHint property (only for JavaFX API compatibility - currently ignored by implementation)
    private Property<CacheHint> cacheHintProperty;

    public final Property<CacheHint> cacheHintProperty() {
        if (cacheHintProperty == null)
            cacheHintProperty = new SimpleObjectProperty<CacheHint>();
        return cacheHintProperty;
    }

    public final void setCacheHint(CacheHint value) {
        cacheHintProperty().setValue(value);
    }

    public final CacheHint getCacheHint() {
        return cacheHintProperty().getValue();
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
    private BooleanProperty disabled;

    protected final void setDisabled(boolean value) {
        disabledPropertyImpl().setValue(value);
    }

    public final boolean isDisabled() {
        return disabled == null ? false : disabled.getValue();
    }

    public final ReadOnlyBooleanProperty disabledProperty() {
        return disabledPropertyImpl()/*.getReadOnlyProperty()*/;
    }

    private BooleanProperty disabledPropertyImpl() {
        if (disabled == null) {
            disabled = new SimpleBooleanProperty(false) {

                @Override
                protected void invalidated() {
                    //pseudoClassStateChanged(DISABLED_PSEUDOCLASS_STATE, get());
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
            };
        }
        return disabled;
    }

    public final void setDisable(boolean value) {
        disableProperty().setValue(value);
    }

    public final boolean isDisable() {
        //return (miscProperties == null) ? DEFAULT_DISABLE : miscProperties.isDisable();
        return disableProperty.getValue();
    }

    /**
     * Defines the individual disabled state of this {@code Node}. Setting
     * {@code disable} to true will cause this {@code Node} and any subnodes to
     * become disabled. This property should be used only to set the disabled
     * state of a {@code Node}.  For querying the disabled state of a
     * {@code Node}, the {@link #disabledProperty disabled} property should instead be used,
     * since it is possible that a {@code Node} was disabled as a result of an
     * ancestor being disabled even if the individual {@code disable} state on
     * this {@code Node} is {@code false}.
     *
     * @defaultValue false
     */
    private final BooleanProperty disableProperty = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            updateDisabled();
        }

        private void updateDisabled() {
            boolean isDisabled = isDisable();
            if (!isDisabled) {
                isDisabled = getParent() != null ? getParent().isDisabled() :
                        false; //getSubScene() != null && getSubScene().isDisabled();
            }
            setDisabled(isDisabled);
/*
            if (this instanceof SubScene) {
                ((SubScene)this).getRoot().setDisabled(isDisabled);
            }
*/
        }

    };
    public final BooleanProperty disableProperty() {
        return disableProperty; //getMiscProperties().disableProperty();
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
    private BooleanProperty hover;

    protected final void setHover(boolean value) {
        hoverPropertyImpl().setValue(value);
    }

    public final boolean isHover() {
        return hover == null ? false : hover.getValue();
    }

    public final ReadOnlyBooleanProperty hoverProperty() {
        return hoverPropertyImpl()/*.getReadOnlyProperty()*/;
    }

    private BooleanProperty hoverPropertyImpl() {
        if (hover == null) {
            hover = new SimpleBooleanProperty(false)/* {

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
    private BooleanProperty pressed;

    protected final void setPressed(boolean value) {
        pressedPropertyImpl().setValue(value);
    }

    public final boolean isPressed() {
        return pressed == null ? false : pressed.getValue();
    }

    public final ReadOnlyBooleanProperty pressedProperty() {
        return pressedPropertyImpl()/*.getReadOnlyProperty()*/;
    }

    private BooleanProperty pressedPropertyImpl() {
        if (pressed == null) {
            pressed = new SimpleBooleanProperty(false)/* {

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

    /**
     * The id of this {@code Node}. This simple string identifier is useful for
     * finding a specific Node within the scene graph. While the id of a Node
     * should be unique within the scene graph, this uniqueness is not enforced.
     * This is analogous to the "id" attribute on an HTML element
     * (<a href="http://www.w3.org/TR/CSS21/syndata.html#value-def-identifier">CSS ID Specification</a>).
     * <p>
     *     For example, if a Node is given the id of "myId", then the lookup method can
     *     be used to find this node as follows: <code>scene.lookup("#myId");</code>.
     * </p>
     *
     * @defaultValue null
     * @see <a href="doc-files/cssref.html">CSS Reference Guide</a>.
     */
    private StringProperty id;

    public final void setId(String value) {
        idProperty().set(value);
    }

    //TODO: this is copied from the property in order to add the @return statement.
    //      We should have a better, general solution without the need to copy it.
    /**
     * The id of this {@code Node}. This simple string identifier is useful for
     * finding a specific Node within the scene graph. While the id of a Node
     * should be unique within the scene graph, this uniqueness is not enforced.
     * This is analogous to the "id" attribute on an HTML element
     * (<a href="http://www.w3.org/TR/CSS21/syndata.html#value-def-identifier">CSS ID Specification</a>).
     *
     * @return the id assigned to this {@code Node} using the {@code setId}
     *         method or {@code null}, if no id has been assigned.
     * @defaultValue null
     * @see <a href="doc-files/cssref.html">CSS Reference Guide</a>.
     */
    public final String getId() {
        return id == null ? null : id.get();
    }

    public final StringProperty idProperty() {
        if (id == null) {
            id = new StringPropertyBase() {

                @Override
                protected void invalidated() {
/*
                    impl_reapplyCSS();
                    if (PrismSettings.printRenderGraph) {
                        impl_markDirty(DirtyBits.DEBUG);
                    }
*/
                }

                @Override
                public Object getBean() {
                    return Node.this;
                }

                @Override
                public String getName() {
                    return "id";
                }
            };
        }
        return id;
    }
    /**
     * A list of String identifiers which can be used to logically group
     * Nodes, specifically for an external style engine. This variable is
     * analogous to the "class" attribute on an HTML element and, as such,
     * each element of the list is a style class to which this Node belongs.
     *
     * @see <a href="http://www.w3.org/TR/css3-selectors/#class-html">CSS3 class selectors</a>
     * @see <a href="doc-files/cssref.html">CSS Reference Guide</a>.
     * @defaultValue null
     */
    private ObservableList<String> styleClass = new TrackableObservableList<String>() {
        @Override
        protected void onChanged(ListChangeListener.Change<String> c) {
            //impl_reapplyCSS();
        }

        @Override
        public String toString() {
            if (size() == 0) {
                return "";
            } else if (size() == 1) {
                return get(0);
            } else {
                StringBuilder buf = new StringBuilder();
                for (int i = 0; i < size(); i++) {
                    buf.append(get(i));
                    if (i + 1 < size()) {
                        buf.append(' ');
                    }
                }
                return buf.toString();
            }
        }
    };

    @Override
    public final ObservableList<String> getStyleClass() {
        return styleClass;
    }

    /**
     * A string representation of the CSS style associated with this
     * specific {@code Node}. This is analogous to the "style" attribute of an
     * HTML element. Note that, like the HTML style attribute, this
     * variable contains style properties and values and not the
     * selector portion of a style rule.
     * @defaultValue empty string
     * @see <a href="doc-files/cssref.html">CSS Reference Guide</a>.
     */
    private StringProperty style;

    /**
     * A string representation of the CSS style associated with this
     * specific {@code Node}. This is analogous to the "style" attribute of an
     * HTML element. Note that, like the HTML style attribute, this
     * variable contains style properties and values and not the
     * selector portion of a style rule.
     * @param value The inline CSS style to use for this {@code Node}.
     *         {@code null} is implicitly converted to an empty String.
     * @defaultValue empty string
     * @see <a href="doc-files/cssref.html">CSS Reference Guide</a>.
     */
    public final void setStyle(String value) {
        styleProperty().set(value);
    }

    // TODO: javadoc copied from property for the sole purpose of providing a return tag
    /**
     * A string representation of the CSS style associated with this
     * specific {@code Node}. This is analogous to the "style" attribute of an
     * HTML element. Note that, like the HTML style attribute, this
     * variable contains style properties and values and not the
     * selector portion of a style rule.
     * @defaultValue empty string
     * @return The inline CSS style associated with this {@code Node}.
     *         If this {@code Node} does not have an inline style,
     *         an empty String is returned.
     * @see <a href="doc-files/cssref.html">CSS Reference Guide</a>.
     */
    public final String getStyle() {
        return style == null ? "" : style.get();
    }

    public final StringProperty styleProperty() {
        if (style == null) {
            style = new StringPropertyBase("") {

                @Override public void set(String value) {
                    // getStyle returns an empty string if the style property
                    // is null. To be consistent, getStyle should also return
                    // an empty string when the style property's value is null.
                    super.set((value != null) ? value : "");
                }

                @Override
                protected void invalidated() {
                    // If the style has changed, then styles of this node
                    // and child nodes might be affected.
                    //impl_reapplyCSS();
                }

                @Override
                public Object getBean() {
                    return Node.this;
                }

                @Override
                public String getName() {
                    return "style";
                }
            };
        }
        return style;
    }

    private final ObservableList<Transform> transforms = FXCollections.observableArrayList();
    @Override
    public ObservableList<Transform> getTransforms() {
        return transforms;
    }

    private Translate layoutTranslateTransform;
    private Scale scaleTransform;
    private Rotate rotateTransform;

    @Override
    public List<Transform> getAllNodeTransforms() {
        // We need to consider all transforms: I) those declared directly at the Node level via translateX/Y, rotateX/Y
        // and scaleX/Y, and II) the general ones declared in getTransforms()
        // We start with I) ie those declared at the Node level
        List<Transform> nodeLevelTransforms = null;
        // Note: JavaFX applies them in the following sequence: 1) Translation, 2) Rotation, and 3) Scale.
        // 1) Translation (we also include the layout in it)
        double ltX = getTranslateX();
        double ltY = getTranslateY();
        //if (!(getParent() instanceof Group)) { // Breaks EnzoClocks, TallyCounter and SpaceFX demos
        ltX += getLayoutX();
        ltY += getLayoutY();
        //}
        if (ltX != 0 || ltY != 0) {
            if (layoutTranslateTransform == null)
                layoutTranslateTransform = Translate.create();
            layoutTranslateTransform.setX(ltX);
            layoutTranslateTransform.setY(ltY);
            nodeLevelTransforms = new ArrayList<>();
            nodeLevelTransforms.add(layoutTranslateTransform);
        }
        // 2) Rotation
        double rotate = getRotate();
        if (rotate != 0) {
            if (rotateTransform == null)
                rotateTransform = new Rotate();
            // Setting angle
            rotateTransform.setAngle(rotate);
            // Setting pivot = node center
            Bounds b = getBoundsInLocal();
            rotateTransform.setPivotX((b.getMinX() + b.getMaxX()) / 2);
            rotateTransform.setPivotY((b.getMinY() + b.getMaxY()) / 2);
            if (nodeLevelTransforms == null)
                nodeLevelTransforms = new ArrayList<>();
            nodeLevelTransforms.add(rotateTransform);
        }
        // 3) Scale
        double scaleX = getScaleX(), scaleY = getScaleY();
        if (scaleX != 1 || scaleY != 1) {
            if (scaleTransform == null)
                scaleTransform = new Scale();
            scaleTransform.setX(scaleX);
            scaleTransform.setY(scaleY);
            Bounds b = getBoundsInLocal();
            scaleTransform.setPivotX((b.getMinX() + b.getMaxX()) / 2);
            scaleTransform.setPivotY((b.getMinY() + b.getMaxY()) / 2);
            if (nodeLevelTransforms == null)
                nodeLevelTransforms = new ArrayList<>();
            nodeLevelTransforms.add(scaleTransform);
        }
        // We finished with I), so we now need to add II)
        // If I) is empty, we just return II)
        if (nodeLevelTransforms == null)
            return getTransforms();

        // Otherwise we return the concatenation I) + II)
        nodeLevelTransforms.addAll(getTransforms());
        return nodeLevelTransforms;
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

    private static final Object USER_DATA_KEY = new Object();
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
        UiScheduler.requestNextScenePulse();
/*
        if (getSubScene() != null) {
            getSubScene().setDirtyLayout(p);
        }
*/
    }

    /**
     * Called when something has changed on this node that *may* have made the
     * scene's focus dirty. This covers the cases where this node is the focus
     * owner and it may have lost eligibility, or it's traversable and it may
     * have gained eligibility. Note that we do not want to use disabled
     * or treeVisible here, as this function is called from their
     * "on invalidate" triggers, and using them will cause them to be
     * revalidated. The pulse will revalidate everything and make the final
     * determination.
     */
    private void focusSetDirty(Scene s) {
        if (s != null &&
            (this == s.getFocusOwner() || isFocusTraversable())) {
            s.setFocusDirty(true);
        }
    }

    /**
     * Requests that this {@code Node} get the input focus, and that this
     * {@code Node}'s top-level ancestor become the focused window. To be
     * eligible to receive the focus, the node must be part of a scene, it and
     * all of its ancestors must be visible, and it must not be disabled.
     * If this node is eligible, this function will cause it to become this
     * {@code Scene}'s "focus owner". Each scene has at most one focus owner
     * node. The focus owner will not actually have the input focus, however,
     * unless the scene belongs to a {@code Stage} that is both visible
     * and active.
     */
    public void requestFocus() {
        if (getScene() != null) {
            getScene().requestFocus(this);
        }
    }

    private boolean canReceiveFocus = false;

    private void setCanReceiveFocus(boolean value) {
        canReceiveFocus = value;
    }

    final boolean isCanReceiveFocus() {
        updateCanReceiveFocus(); // Computed here as opposed to JavaFX original code (because impl_isTreeVisible() is delegated to the target peer)
        return canReceiveFocus;
    }

    private void updateCanReceiveFocus() {
        setCanReceiveFocus(getScene() != null
                && !isDisabled()
                && impl_isTreeVisible());
    }

    /**
     * Traverses from this node in the direction indicated. Note that this
     * node need not actually have the focus, nor need it be focusTraversable.
     * However, the node must be part of a scene, otherwise this request
     * is ignored.
     */
    //@Deprecated
    public final boolean impl_traverse(Direction dir) {
        if (getScene() == null) {
            return false;
        }
        return false; //getScene().traverse(this, dir);
    }


    /**
     * Specifies whether this {@code Node} should be a part of focus traversal
     * cycle. When this property is {@code true} focus can be moved to this
     * {@code Node} and from this {@code Node} using regular focus traversal
     * keys. On a desktop such keys are usually {@code TAB} for moving focus
     * forward and {@code SHIFT+TAB} for moving focus backward.
     *
     * When a {@code Scene} is created, the system gives focus to a
     * {@code Node} whose {@code focusTraversable} variable is true
     * and that is eligible to receive the focus,
     * unless the focus had been set explicitly via a call
     * to {@link #requestFocus()}.
     *
     * @see #requestFocus()
     * @defaultValue false
     */
    private BooleanProperty focusTraversable;

    public final void setFocusTraversable(boolean value) {
        focusTraversableProperty().setValue(value);
    }
    public final boolean isFocusTraversable() {
        return focusTraversable == null ? false : focusTraversable.getValue();
    }

    public final BooleanProperty focusTraversableProperty() {
        if (focusTraversable == null) {
            focusTraversable = new StyleableBooleanProperty(false) {

                @Override
                public void invalidated() {
                    Scene _scene = getScene();
                    if (_scene != null) {
                        if (get()) {
                            _scene.initializeInternalEventDispatcher();
                        }
                        focusSetDirty(_scene);
                    }
                }

                public CssMetaData getCssMetaData() {
                    return null; //StyleableProperties.FOCUS_TRAVERSABLE;
                }

                @Override
                public Object getBean() {
                    return Node.this;
                }

                @Override
                public String getName() {
                    return "focusTraversable";
                }
            };
        }
        return focusTraversable;
    }

    /***************************************************************************
     *                                                                         *
     *                             Focus Traversal                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Special boolean property which allows for atomic focus change.
     * Focus change means defocusing the old focus owner and focusing a new
     * one. With a usual property, defocusing the old node fires the value
     * changed event and user code can react with something that breaks
     * focusability of the new node, or even remove the new node from the scene.
     * This leads to various error states. This property allows for setting
     * the state without firing the event. The focus change first sets both
     * properties and then fires both events. This makes the focus change look
     * like an atomic operation - when the old node is notified to loose focus,
     * the new node is already focused.
     */
    final class FocusedProperty extends ReadOnlyBooleanPropertyBase {
        private boolean value;
        private boolean valid = true;
        private boolean needsChangeEvent = false;

        public void store(final boolean value) {
            if (value != this.value) {
                this.value = value;
                markInvalid();
            }
        }

        public void notifyListeners() {
            if (needsChangeEvent) {
                fireValueChangedEvent();
                needsChangeEvent = false;
            }
        }

        private void markInvalid() {
            if (valid) {
                valid = false;

                //pseudoClassStateChanged(FOCUSED_PSEUDOCLASS_STATE, get());
/*
                PlatformLogger logger = Logging.getFocusLogger();
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine(this + " focused=" + get());
                }
*/

                needsChangeEvent = true;

                //notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUSED);
            }
        }

        @Override
        public boolean get() {
            valid = true;
            return value;
        }

        @Override
        public Object getBean() {
            return Node.this;
        }

        @Override
        public String getName() {
            return "focused";
        }
    }
    /**
     * Indicates whether this {@code Node} currently has the input focus.
     * To have the input focus, a node must be the {@code Scene}'s focus
     * owner, and the scene must be in a {@code Stage} that is visible
     * and active. See {@link #requestFocus()} for more information.
     *
     * @see #requestFocus()
     * @defaultValue false
     */
    private FocusedProperty focused;

    public final void setFocused(boolean value) {
        FocusedProperty fp = focusedPropertyImpl();
        if (fp.value != value) {
            fp.store(value);
            fp.notifyListeners();
        }
    }

    public final boolean isFocused() {
        return focused == null ? false : focused.get();
    }

    public final ReadOnlyBooleanProperty focusedProperty() {
        return focusedPropertyImpl();
    }

    private FocusedProperty focusedPropertyImpl() {
        if (focused == null) {
            focused = new FocusedProperty();
        }
        return focused;
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
            eventDispatcher = new SimpleObjectProperty<EventDispatcher>(
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

    /**
     * Returns the initial focus traversable state of this node, for use
     * by the JavaFX CSS engine to correctly set its initial value. This method
     * can be overridden by subclasses in instances where focus traversable should
     * initially be true (as the default implementation of this method is to return
     * false).
     *
     * @return the initial focus traversable state for this {@code Node}.
     * @since 9
     */
    protected Boolean getInitialFocusTraversable() {
        return Boolean.FALSE;
    }

    public final NodeOrientation getEffectiveNodeOrientation() {
        return NodeOrientation.LEFT_TO_RIGHT; // Simplified version for WebFX
    }


    private NodePeer nodePeer;
    private boolean peerFocusRequested;

    public NodePeer getNodePeer() {
        return nodePeer;
    }

    // Phantom scene used to temporarily create and bind peers when the application code is already working with nodes
    // before their insertion in the scene graph (ex: drawing in a canvas before it is inserted).
    public final static Scene PHANTOM_SCENE = new Scene(null);

    public NodePeer getOrCreateAndBindNodePeer() {
        Scene scene = getScene();
        if (scene == null) { // Quite annoying (the code is asking the peer before the node has been inserted in the scene graph)
            scene = WebFxKitLauncher.getPrimaryStage().getScene(); // Using the main scene (the most probably case)
            if (scene == null) // If still not set (can happen in application initialization code)
                scene = PHANTOM_SCENE; // We temporarily use a phantom scene for the initial binding (until the final scene is set on the node)
        }
        return scene.getOrCreateAndBindNodePeer(this);
    }

    public void setNodePeer(NodePeer nodePeer) {
        this.nodePeer = nodePeer;
        createLayoutMeasurable(nodePeer);
        if (peerFocusRequested && nodePeer != null) {
            nodePeer.requestFocus();
            peerFocusRequested = false;
        }
    }

    private List<Consumer<NodePeer>> onNodePeerReadyHandlers;

    public void onNodePeerReady(Consumer<NodePeer> handler) { // handler will be called when nodePeer is set and nodePeer.getNode() doesn't return null
        if (nodePeer != null && nodePeer.getNode() != null)
            handler.accept(nodePeer);
        else {
            if (onNodePeerReadyHandlers == null)
                onNodePeerReadyHandlers = new ArrayList<>();
            onNodePeerReadyHandlers.add(handler);
        }
    }

    // Called by Scene once nodePeer is bound to node
    void callNodePeerHandlers() {
        if (nodePeer != null && onNodePeerReadyHandlers != null) {
            onNodePeerReadyHandlers.forEach(handler -> handler.accept(nodePeer));
            onNodePeerReadyHandlers = null;
        }
    }

    // Called by emulated Scene on focus owner change
    public void requestPeerFocus() {
        NodePeer nodePeer = getNodePeer();
        if (nodePeer != null)
            nodePeer.requestFocus();
        else
            peerFocusRequested = true;
    }

    /**
     * Traverses from this node in the direction indicated. Note that this
     * node need not actually have the focus, nor need it be focusTraversable.
     * However, the node must be part of a scene, otherwise this request
     * is ignored.
     */
    public final boolean traverse(Direction dir, TraversalMethod method) {
        if (getScene() == null) {
            return false;
        }
        return getScene().traverse(this, dir, method);
    }


    private Measurable measurable;

    public Measurable getLayoutMeasurable() {
        if (measurable == null) { // Happens when the node peer is not yet created
            Scene scene = getScene();
            if (scene != null && scene.getRoot() != this) // Not doing it on root because this cause an infinite recursion
                scene.createAndBindNodePeerAndChildren(this);
            if (measurable == null)
                createLayoutMeasurable(null); // Temporary using the implementation based on the model
        }
        return measurable;
    }

    protected boolean shouldUseLayoutMeasurable() {
        return true;
    }

    public void onPeerSizeChanged() {
        markDirtyLayoutBranch();
        impl_geomChanged(); // will call parent.requestLayout()
        layoutBounds.setValue(getLayoutBounds());
    }

    public Bounds getBoundsInLocal() {
        return getLayoutBounds(); // Temporary
    }

    protected void createLayoutMeasurable(NodePeer nodePeer) {
        // Always creating a new LayoutMeasurable (even when nodePeer is valid) so that min/pref/max
        // width/height user values are returned in priority whenever they have been set.
        measurable = new Measurable() {
            private final Measurable acceptedMeasurable = nodePeer instanceof Measurable && shouldUseLayoutMeasurable() ? (Measurable) nodePeer : null;
            {
                if (nodePeer instanceof HasSizeChangedCallback)
                    UiScheduler.scheduleDeferred(() ->
                            ((HasSizeChangedCallback) nodePeer).setSizeChangedCallback(() -> onPeerSizeChanged())
                    );
            }

            public Bounds getLayoutBounds() { return acceptedMeasurable != null ?
                    acceptedMeasurable.getLayoutBounds()
                    : impl_getLayoutBounds(); }

            public double minWidth(double height) {
                if (Node.this instanceof HasMinWidthProperty) {
                    double minWidth = ((HasMinWidthProperty) Node.this).getMinWidth();
                    if (minWidth == USE_PREF_SIZE)
                        return prefWidth(height);
                    if (minWidth != USE_COMPUTED_SIZE)
                        return minWidth;
                }
                return acceptedMeasurable != null ?
                        acceptedMeasurable.minWidth(height)
                        : impl_minWidth(height);
            }

            public double maxWidth(double height) {
                if (Node.this instanceof HasMaxWidthProperty) {
                    double maxWidth = ((HasMaxWidthProperty) Node.this).getMaxWidth();
                    if (maxWidth == USE_PREF_SIZE)
                        return prefWidth(height);
                    if (maxWidth != USE_COMPUTED_SIZE)
                        return maxWidth;
                }
                return acceptedMeasurable != null ?
                        acceptedMeasurable.maxWidth(height)
                        : impl_maxWidth(height);
            }

            public double minHeight(double width) {
                if (Node.this instanceof HasMinHeightProperty) {
                    double minHeight = ((HasMinHeightProperty) Node.this).getMinHeight();
                    if (minHeight == USE_PREF_SIZE)
                        return prefHeight(width);
                    if (minHeight != USE_COMPUTED_SIZE)
                        return minHeight;
                }
                return acceptedMeasurable != null ?
                        acceptedMeasurable.minHeight(width)
                        : impl_minHeight(width);
            }

            public double maxHeight(double width) {
                if (Node.this instanceof HasMaxHeightProperty) {
                    double maxHeight = ((HasMaxHeightProperty) Node.this).getMaxHeight();
                    if (maxHeight == USE_PREF_SIZE)
                        return prefHeight(width);
                    if (maxHeight != USE_COMPUTED_SIZE)
                        return maxHeight;
                }
                return acceptedMeasurable != null ?
                        acceptedMeasurable.maxHeight(width) :
                        impl_maxHeight(width);
            }

            public double prefWidth(double height) {
                if (Node.this instanceof HasPrefWidthProperty) {
                    double prefWidth = ((HasPrefWidthProperty) Node.this).getPrefWidth();
                    if (prefWidth != USE_COMPUTED_SIZE)
                        return prefWidth;
                }
                return acceptedMeasurable != null ?
                        acceptedMeasurable.prefWidth(height)
                        : impl_prefWidth(height);
            }

            public double prefHeight(double width) {
                if (Node.this instanceof HasPrefHeightProperty) {
                    double prefHeight = ((HasPrefHeightProperty) Node.this).getPrefHeight();
                    if (prefHeight != USE_COMPUTED_SIZE)
                        return prefHeight;
                }
                return acceptedMeasurable != null ?
                        acceptedMeasurable.prefHeight(width)
                        : impl_prefHeight(width);
            }

            @Override
            public void clearCache() {
                if (acceptedMeasurable != null)
                    acceptedMeasurable.clearCache();
            }
        };
    }

    private final static double DEFAULT_TRANSLATE_X = 0;
    private DoubleProperty translateXProperty;

    public final void setTranslateX(double value) {
        translateXProperty().set(value);
    }

    public final double getTranslateX() {
        return (translateXProperty == null) ? DEFAULT_TRANSLATE_X
                : translateXProperty.get();
    }

    public final DoubleProperty translateXProperty() {
        if (translateXProperty == null)
            translateXProperty = new SimpleDoubleProperty(DEFAULT_TRANSLATE_X);
        return translateXProperty;
    }

    private final static double DEFAULT_TRANSLATE_Y = 0;
    private DoubleProperty translateYProperty;

    public final void setTranslateY(double value) {
        translateYProperty().set(value);
    }

    public final double getTranslateY() {
        return (translateYProperty == null) ? DEFAULT_TRANSLATE_Y
                : translateYProperty.get();
    }

    public final DoubleProperty translateYProperty() {
        if (translateYProperty == null)
            translateYProperty = new SimpleDoubleProperty(DEFAULT_TRANSLATE_Y);
        return translateYProperty;
    }

    private final static double DEFAULT_SCALE_X = 1;
    private DoubleProperty scaleXProperty;

    public final void setScaleX(double value) {
        scaleXProperty().set(value);
    }

    public final double getScaleX() {
        return (scaleXProperty == null) ? DEFAULT_SCALE_X
                : scaleXProperty.get();
    }

    /**
     * Defines the factor by which coordinates are scaled about the center of the
     * object along the X axis of this {@code Node}. This is used to stretch or
     * shrink the node either manually or by using an animation.
     * <p>
     * This scale factor is not included in {@link #layoutBoundsProperty layoutBounds} by
     * default, which makes it ideal for scaling the entire node after
     * all effects and transforms have been taken into account.
     * <p>
     * The pivot point about which the scale occurs is the center of the
     * untransformed {@link #layoutBoundsProperty layoutBounds}.
     *
     * @return the scaleX for this {@code Node}
     * @defaultValue 1.0
     */

    public final DoubleProperty scaleXProperty() {
        if (scaleXProperty == null)
            scaleXProperty = new SimpleDoubleProperty(DEFAULT_SCALE_X);
        return scaleXProperty;
    }

    private final static double DEFAULT_SCALE_Y = 1;
    private DoubleProperty scaleYProperty;

    public final void setScaleY(double value) {
        scaleYProperty().set(value);
    }

    public final double getScaleY() {
        return (scaleYProperty == null) ? DEFAULT_SCALE_Y
                : scaleYProperty.get();
    }

    /**
     * Defines the factor by which coordinates are scaled about the center of the
     * object along the Y axis of this {@code Node}. This is used to stretch or
     * shrink the node either manually or by using an animation.
     * <p>
     * This scale factor is not included in {@link #layoutBoundsProperty layoutBounds} by
     * default, which makes it ideal for scaling the entire node after
     * all effects and transforms have been taken into account.
     * <p>
     * The pivot point about which the scale occurs is the center of the
     * untransformed {@link #layoutBoundsProperty layoutBounds}.
     *
     * @return the scaleY for this {@code Node}
     * @defaultValue 1.0
     */
    public final DoubleProperty scaleYProperty() {
        if (scaleYProperty == null)
            scaleYProperty = new SimpleDoubleProperty(DEFAULT_SCALE_Y);
        return scaleYProperty;
    }

    private final static double DEFAULT_SCALE_Z = 1;
    private DoubleProperty scaleZProperty;

    public final void setScaleZ(double value) {
        scaleZProperty().set(value);
    }

    public final double getScaleZ() {
        return (scaleZProperty == null) ? DEFAULT_SCALE_Z
                : scaleYProperty.get();
    }

    /**
     * Defines the factor by which coordinates are scaled about the center of the
     * object along the Z axis of this {@code Node}. This is used to stretch or
     * shrink the node either manually or by using an animation.
     * <p>
     * This scale factor is not included in {@link #layoutBoundsProperty layoutBounds} by
     * default, which makes it ideal for scaling the entire node after
     * all effects and transforms have been taken into account.
     * <p>
     * The pivot point about which the scale occurs is the center of the
     * untransformed {@link #layoutBoundsProperty layoutBounds}.
     *
     * @return the scaleZ for this {@code Node}
     * @defaultValue 1.0
     */
    public final DoubleProperty scaleZProperty() {
        if (scaleZProperty == null)
            scaleZProperty = new SimpleDoubleProperty(DEFAULT_SCALE_Z);
        return scaleZProperty;
    }

    private final static double DEFAULT_ROTATE = 0;
    private DoubleProperty rotateProperty;

    public final void setRotate(double value) {
        rotateProperty().set(value);
    }

    public final double getRotate() {
        return (rotateProperty == null) ? DEFAULT_ROTATE
                : rotateProperty.get();
    }

    public final DoubleProperty rotateProperty() {
        if (rotateProperty == null)
            rotateProperty = new SimpleDoubleProperty(DEFAULT_ROTATE);
        return rotateProperty;
    }

    /**
     * The rectangular bounds that should be used for layout calculations for
     * this node. {@code layoutBounds} may differ from the visual bounds
     * of the node and is computed differently depending on the node type.
     * <p>
     * If the node type is resizable ({@link javafx.scene.layout.Region Region},
     * {@link javafx.scene.control.Control Control}, or {@link javafx.scene.web.WebView WebView})
     * then the layoutBounds will always be {@code 0,0 width x height}.
     * If the node type is not resizable ({@link javafx.scene.shape.Shape Shape},
     * {@link javafx.scene.text.Text Text}, or {@link Group}), then the layoutBounds
     * are computed based on the node's geometric properties and does not include the
     * node's clip, effect, or transforms.  See individual class documentation
     * for details.
     * <p>
     * Note that the {@link #layoutXProperty layoutX}, {@link #layoutYProperty layoutY}, {@link #translateXProperty translateX}, and
     * {@link #translateYProperty translateY} variables are not included in the layoutBounds.
     * This is important because layout code must first determine the current
     * size and location of the node (using layoutBounds) and then set
     * {@code layoutX} and {@code layoutY} to adjust the translation of the
     * node so that it will have the desired layout position.
     * <p>
     * Because the computation of layoutBounds is often tied to a node's
     * geometric variables, it is an error to bind any such variables to an
     * expression that depends upon {@code layoutBounds}. For example, the
     * x or y variables of a shape should never be bound to layoutBounds
     * for the purpose of positioning the node.
     * <p>
     * The layoutBounds will never be null.
     *
     * Webfx note: this property is set only when the target toolkit reports a change by calling
     * the SizeChangedCallback set on the LayoutMeasurable (ex: once an image is loaded so we
     * now know the size of the image). So layoutBoundsProperty() should be used only to listen
     * such changes.
     *
     */
    private ObjectProperty<Bounds> layoutBounds = new SimpleObjectProperty<>();

    public ReadOnlyObjectProperty<Bounds> layoutBoundsProperty() {
        return layoutBounds;
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
     * Invoked by subclasses whenever their geometric bounds have changed.
     * Because the default layout bounds is based on the node geometry, this
     * function will invoke impl_notifyLayoutBoundsChanged. The default
     * implementation of impl_notifyLayoutBoundsChanged() will simply invalidate
     * layoutBounds. Resizable subclasses will want to override this function
     * in most cases to be a no-op.
     * <p>
     * This function will also invalidate the cached geom bounds, and then
     * invoke localBoundsChanged() which will eventually end up invoking a
     * chain of functions up the tree to ensure that each parent of this
     * Node is notified that its bounds may have also changed.
     * <p>
     * This function should be treated as though it were final. It is not
     * intended to be overridden by subclasses.
     *
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    protected void impl_geomChanged() {
/*
        if (geomBoundsInvalid) {
            // GeomBoundsInvalid is false when node geometry changed and
            // the untransformed node bounds haven't been recalculated yet.
            // Most of the time, the recalculation of layout and transformed
            // node bounds don't require validation of untransformed bounds
            // and so we can not skip the following notifications.
            impl_notifyLayoutBoundsChanged();
            transformedBoundsChanged();
            return;
        }
*/
        geomBounds.makeEmpty();
        //geomBoundsInvalid = true;
        //impl_markDirty(DirtyBits.NODE_BOUNDS);
        impl_notifyLayoutBoundsChanged();
        //localBoundsChanged();
    }

    /**
     * Invoked by impl_geomChanged(). Since layoutBounds is by default based
     * on the geometric bounds, the default implementation of this function will
     * invalidate the layoutBounds. Resizable Node subclasses generally base
     * layoutBounds on the width/height instead of the geometric bounds, and so
     * will generally want to override this function to be a no-op.
     *
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    protected void impl_notifyLayoutBoundsChanged() {
        //impl_layoutBoundsChanged();
        // notify the parent
        // Group instanceof check a little hoaky, but it allows us to disable
        // unnecessary layout for the case of a non-resizable within a group
        Parent p = getParent();
        if (isManaged() && (p != null) && !(p instanceof Group && !isResizable())
                && !p.performingLayout) {
            p.requestLayout();
        }
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

    /**
     * Transforms a point from the local coordinate space of this {@code Node}
     * into the coordinate space of its scene.
     * Note that if this node is in a {@link SubScene}, the result is in the subscene coordinates,
     * not that of {@link javafx.scene.Scene}.
     * @param localX x coordinate of a point in Node's space
     * @param localY y coordinate of a point in Node's space
     * @return scene coordinates of the point or null if Node is not in a {@link Window}
     */
    public Point2D localToScene(double localX, double localY) {
        final com.sun.javafx.geom.Point2D tempPt =
                TempState.getInstance().point;
        tempPt.setLocation((float)localX, (float)localY);
        localToScene(tempPt);
        return new Point2D(tempPt.x, tempPt.y);
    }

    protected void localToScene(com.sun.javafx.geom.Point2D pt) {
        localToParent(pt);
        if (getParent() != null) {
            getParent().localToScene(pt);
        }
    }

    /**
     * Transforms in place the specified point from local coords to parent
     * coords. Made package private for the sake of testing.
     */
    public void localToParent(com.sun.javafx.geom.Point2D pt) {
        // Note: we need to iterate all Node transforms in the inverse order
        List<Transform> transforms = getAllNodeTransforms();
        for (int i = transforms.size() - 1; i >= 0; i--) {
            Transform transform = transforms.get(i);
            com.sun.javafx.geom.Point2D p = transform.transform(pt);
            pt.x = p.x;
            pt.y = p.y;
        }
        // Additional WebFX code to manage the shift introduced by the viewport when this node is the content of a ScrollPane
        Parent parent = getParent();
        if (parent instanceof IScrollPane) {
            ((IScrollPane) parent).localContentToParentViewport(pt);
        }
    }

    public Point2D localToParent(Point2D pt) {
        return localToParent(pt.getX(), pt.getY());
    }

    public Point2D localToParent(double localX, double localY) {
        final com.sun.javafx.geom.Point2D tempPt =
                TempState.getInstance().point;
        tempPt.setLocation((float)localX, (float)localY);
        localToParent(tempPt);
        return new Point2D(tempPt.x, tempPt.y);
    }

    public Point2D sceneToLocal(Point2D pt) {
        return sceneToLocal(pt.getX(), pt.getY());
    }

    public Point2D sceneToLocal(double sceneX, double sceneY) {
        final com.sun.javafx.geom.Point2D tempPt =
                TempState.getInstance().point;
        tempPt.setLocation((float)sceneX, (float)sceneY);
        sceneToLocal(tempPt);
        return new Point2D(tempPt.x, tempPt.y);
    }

    protected void sceneToLocal(com.sun.javafx.geom.Point2D pt) {
        if (getParent() != null) {
            getParent().sceneToLocal(pt);
        }
        parentToLocal(pt);
    }

    void parentToLocal(com.sun.javafx.geom.Point2D pt) {
        // Additional WebFX code to manage the shift introduced by the viewport when this node is the content of a ScrollPane
        Parent parent = getParent();
        if (parent instanceof IScrollPane) {
            ((IScrollPane) parent).parentViewportToLocalContent(pt);
        }
//        List<Transform> transforms = localToParentTransforms();
        for (Transform transform : getAllNodeTransforms()) {
//        for (int i = transforms.size() - 1; i >= 0; i--) { Transform transform = transforms.get(i);
            com.sun.javafx.geom.Point2D p = transform.inverseTransform(pt);
            pt.x = p.x;
            pt.y = p.y;
        }
    }

    public Point2D parentToLocal(double x, double y) {
        final com.sun.javafx.geom.Point2D tempPt =
                TempState.getInstance().point;
        tempPt.setLocation((float)x, (float)y);
        parentToLocal(tempPt);
        return new Point2D(tempPt.x, tempPt.y);
    }

    /**
     * Transforms a point from the local coordinate space of this {@code Node}
     * into the coordinate space of its {@link javafx.stage.Screen}.
     * @param localX x coordinate of a point in Node's space
     * @param localY y coordinate of a point in Node's space
     * @return screen coordinates of the point or null if Node is not in a {@link Window}
     * @since JavaFX 8.0
     */
    public Point2D localToScreen(double localX, double localY) {
        return localToScreen(localX, localY, 0.0);
    }

    /**
     * Transforms a point from the local coordinate space of this {@code Node}
     * into the coordinate space of its {@link javafx.stage.Screen}.
     * @param localPoint a point in Node's space
     * @return screen coordinates of the point or null if Node is not in a {@link Window}
     * @since JavaFX 8.0
     */
    public Point2D localToScreen(Point2D localPoint) {
        return localToScreen(localPoint.getX(), localPoint.getY());
    }

    /**
     * Transforms a point from the local coordinate space of this {@code Node}
     * into the coordinate space of its {@link javafx.stage.Screen}.
     * @param localX x coordinate of a point in Node's space
     * @param localY y coordinate of a point in Node's space
     * @param localZ z coordinate of a point in Node's space
     * @return screen coordinates of the point or null if Node is not in a {@link Window}
     * @since JavaFX 8.0
     */
    public Point2D localToScreen(double localX, double localY, double localZ) {
        Scene scene = getScene();
        if (scene == null) return null;
        Window window = scene.getWindow();
        if (window == null) return null;

/*
        Point3D pt = localToScene(localX, localY, localZ);
        final SubScene subScene = getSubScene();
        if (subScene != null) {
            pt = SceneUtils.subSceneToScene(subScene, pt);
        }
        final Point2D projection = CameraHelper.project(
                SceneHelper.getEffectiveCamera(getScene()), pt);
*/
        Point2D projection = localToScene(localX, localY);

        return new Point2D(projection.getX() + scene.getX() + window.getX(),
                projection.getY() + scene.getY() + window.getY());
    }

    public final boolean impl_isTreeVisible() {
        NodePeer nodePeer = getNodePeer();
        return nodePeer != null && nodePeer.isTreeVisible();
    }

    /**
     * Transforms a bounds from the local coordinate space of this
     * {@code Node} into the coordinate space of its {@link javafx.stage.Screen}.
     * @param localBounds bounds in Node's space
     * @return the bounds in screen coordinates or null if Node is not in a {@link Window}
     * @since JavaFX 8.0
     */
    public Bounds localToScreen(Bounds localBounds) {
        final Point2D p1 = localToScreen(localBounds.getMinX(), localBounds.getMinY(), localBounds.getMinZ());
        final Point2D p2 = localToScreen(localBounds.getMinX(), localBounds.getMinY(), localBounds.getMaxZ());
        final Point2D p3 = localToScreen(localBounds.getMinX(), localBounds.getMaxY(), localBounds.getMinZ());
        final Point2D p4 = localToScreen(localBounds.getMinX(), localBounds.getMaxY(), localBounds.getMaxZ());
        final Point2D p5 = localToScreen(localBounds.getMaxX(), localBounds.getMaxY(), localBounds.getMinZ());
        final Point2D p6 = localToScreen(localBounds.getMaxX(), localBounds.getMaxY(), localBounds.getMaxZ());
        final Point2D p7 = localToScreen(localBounds.getMaxX(), localBounds.getMinY(), localBounds.getMinZ());
        final Point2D p8 = localToScreen(localBounds.getMaxX(), localBounds.getMinY(), localBounds.getMaxZ());

        return BoundsUtils.createBoundingBox(p1, p2, p3, p4, p5, p6, p7, p8);
    }

    /**
     * Convenience method for setting a single Object property that can be
     * retrieved at a later date. This is functionally equivalent to calling
     * the getProperties().put(Object key, Object value) method. This can later
     * be retrieved by calling {@link Node#getUserData()}.
     *
     * @param value The value to be stored - this can later be retrieved by calling
     *          {@link Node#getUserData()}.
     */
    public void setUserData(Object value) {
        getProperties().put(USER_DATA_KEY, value);
    }

    /**
     * Returns a previously set Object property, or null if no such property
     * has been set using the {@link Node#setUserData(java.lang.Object)} method.
     *
     * @return The Object that was previously set, or null if no property
     *          has been set or if null was set.
     */
    public Object getUserData() {
        return getProperties().get(USER_DATA_KEY);
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

    public final void setOnMouseMoved(
            EventHandler<? super MouseEvent> value) {
        onMouseMovedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseMoved() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnMouseMoved();
    }

    /**
     * Defines a function to be called when mouse cursor moves within
     * this {@code Node} but no buttons have been pushed.
     * @return the event handler that is called when a mouse cursor moves
     * within this {@code Node} but no buttons have been pushed
     */
    public final ObjectProperty<EventHandler<? super MouseEvent>>
    onMouseMovedProperty() {
        return getEventHandlerProperties().onMouseMovedProperty();
    }

    public final void setOnMousePressed(
            EventHandler<? super MouseEvent> value) {
        onMousePressedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMousePressed() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnMousePressed();
    }

    /**
     * Defines a function to be called when a mouse button
     * has been pressed on this {@code Node}.
     * @return the event handler that is called when a mouse button has been
     * pressed on this {@code Node}
     */
    public final ObjectProperty<EventHandler<? super MouseEvent>>
    onMousePressedProperty() {
        return getEventHandlerProperties().onMousePressedProperty();
    }

    public final void setOnMouseReleased(
            EventHandler<? super MouseEvent> value) {
        onMouseReleasedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseReleased() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnMouseReleased();
    }

    /**
     * Defines a function to be called when a mouse button
     * has been released on this {@code Node}.
     * @return the event handler that is called when a mouse button has been
     * released on this {@code Node}
     */
    public final ObjectProperty<EventHandler<? super MouseEvent>>
    onMouseReleasedProperty() {
        return getEventHandlerProperties().onMouseReleasedProperty();
    }


    public final void setOnMouseEntered(
            EventHandler<? super MouseEvent> value) {
        onMouseEnteredProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseEntered() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnMouseEntered();
    }

    /**
     * Defines a function to be called when the mouse enters this {@code Node}.
     */
    public final ObjectProperty<EventHandler<? super MouseEvent>>
    onMouseEnteredProperty() {
        return getEventHandlerProperties().onMouseEnteredProperty();
    }

    public final void setOnMouseExited(
            EventHandler<? super MouseEvent> value) {
        onMouseExitedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseExited() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnMouseExited();
    }

    /**
     * Defines a function to be called when the mouse exits this {@code Node}.
     */
    public final ObjectProperty<EventHandler<? super MouseEvent>>
    onMouseExitedProperty() {
        return getEventHandlerProperties().onMouseExitedProperty();
    }

    public final void setOnMouseDragged(
            EventHandler<? super MouseEvent> value) {
        onMouseDraggedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnMouseDragged() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnMouseDragged();
    }

    /* ************************************************************************
     *                                                                        *
     *
     *                                                                        *
     *************************************************************************/

    public final void setOnDragEntered(
            EventHandler<? super DragEvent> value) {
        onDragEnteredProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragEntered() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnDragEntered();
    }

    /**
     * Defines a function to be called when drag gesture
     * enters this {@code Node}.
     * @return the event handler that is called when drag gesture enters this
     * {@code Node}
     */
    public final ObjectProperty<EventHandler<? super DragEvent>>
    onDragEnteredProperty() {
        return getEventHandlerProperties().onDragEnteredProperty();
    }

    public final void setOnDragExited(
            EventHandler<? super DragEvent> value) {
        onDragExitedProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragExited() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnDragExited();
    }

    /**
     * Defines a function to be called when drag gesture
     * exits this {@code Node}.
     * @return the event handler that is called when drag gesture exits this
     * {@code Node}
     */
    public final ObjectProperty<EventHandler<? super DragEvent>>
    onDragExitedProperty() {
        return getEventHandlerProperties().onDragExitedProperty();
    }

    /**
     * Defines a function to be called when a mouse button is pressed
     * on this {@code Node} and then dragged.
     * @return the event handler that is called when a mouse button is pressed
     * on this {@code Node} and then dragged
     */
    public final ObjectProperty<EventHandler<? super MouseEvent>>
    onMouseDraggedProperty() {
        return getEventHandlerProperties().onMouseDraggedProperty();
    }

    public final void setOnDragDetected(
            EventHandler<? super MouseEvent> value) {
        onDragDetectedProperty().set(value);
    }

    public final EventHandler<? super MouseEvent> getOnDragDetected() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnDragDetected();
    }

    /**
     * Defines a function to be called when drag gesture has been
     * detected. This is the right place to start drag and drop operation.
     * @return the event handler that is called when drag gesture has been
     * detected
     */
    public final ObjectProperty<EventHandler<? super MouseEvent>>
    onDragDetectedProperty() {
        return getEventHandlerProperties().onDragDetectedProperty();
    }

    public final void setOnDragOver(
            EventHandler<? super DragEvent> value) {
        onDragOverProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragOver() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnDragOver();
    }

    /**
     * Defines a function to be called when drag gesture progresses within
     * this {@code Node}.
     * @return the event handler that is called when drag gesture progresses
     * within this {@code Node}
     */
    public final ObjectProperty<EventHandler<? super DragEvent>>
    onDragOverProperty() {
        return getEventHandlerProperties().onDragOverProperty();
    }

    public final void setOnDragDropped(
            EventHandler<? super DragEvent> value) {
        onDragDroppedProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragDropped() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnDragDropped();
    }

    /**
     * Defines a function to be called when the mouse button is released
     * on this {@code Node} during drag and drop gesture. Transfer of data from
     * the {@link DragEvent}'s {@link DragEvent#getDragboard() dragboard} should
     * happen in this function.
     * @return the event handler that is called when the mouse button is
     * released on this {@code Node}
     */
    public final ObjectProperty<EventHandler<? super DragEvent>>
    onDragDroppedProperty() {
        return getEventHandlerProperties().onDragDroppedProperty();
    }

    public final void setOnDragDone(
            EventHandler<? super DragEvent> value) {
        onDragDoneProperty().set(value);
    }

    public final EventHandler<? super DragEvent> getOnDragDone() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnDragDone();
    }

    /**
     * Defines a function to be called when this {@code Node} is a
     * drag and drop gesture source after its data has
     * been dropped on a drop target. The {@code transferMode} of the
     * event shows what just happened at the drop target.
     * If {@code transferMode} has the value {@code MOVE}, then the source can
     * clear out its data. Clearing the source's data gives the appropriate
     * appearance to a user that the data has been moved by the drag and drop
     * gesture. A {@code transferMode} that has the value {@code NONE}
     * indicates that no data was transferred during the drag and drop gesture.
     * @return the event handler that is called when this {@code Node} is a drag
     * and drop gesture source after its data has been dropped on a drop target
     */
    public final ObjectProperty<EventHandler<? super DragEvent>>
    onDragDoneProperty() {
        return getEventHandlerProperties().onDragDoneProperty();
    }

    /**
     * Confirms a potential drag and drop gesture that is recognized over this
     * {@code Node}.
     * Can be called only from a DRAG_DETECTED event handler. The returned
     * {@link Dragboard} is used to transfer data during
     * the drag and drop gesture. Placing this {@code Node}'s data on the
     * {@link Dragboard} also identifies this {@code Node} as the source of
     * the drag and drop gesture.
     * More detail about drag and drop gestures is described in the overivew
     * of {@link DragEvent}.
     *
     * @see DragEvent
     * @param transferModes The supported {@code TransferMode}(s) of this {@code Node}
     * @return A {@code Dragboard} to place this {@code Node}'s data on
     * @throws IllegalStateException if drag and drop cannot be started at this
     * moment (it's called outside of {@code DRAG_DETECTED} event handling or
     * this node is not in scene).
     */
    public Dragboard startDragAndDrop(TransferMode... transferModes) {
        if (getScene() != null) {
            return getScene().startDragAndDrop(this, transferModes);
        }

        throw new IllegalStateException("Cannot start drag and drop on node "
                + "that is not in scene");
    }


    public final void setOnScroll(
            EventHandler<? super ScrollEvent> value) {
        onScrollProperty().set(value);
    }

    public final EventHandler<? super ScrollEvent> getOnScroll() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnScroll();
    }

    /**
     * Defines a function to be called when user performs a scrolling action.
     * @return the event handler that is called when user performs a scrolling
     * action
     */
    public final ObjectProperty<EventHandler<? super ScrollEvent>>
    onScrollProperty() {
        return getEventHandlerProperties().onScrollProperty();
    }


    /**
     * Takes a snapshot of this node and returns the rendered image when
     * it is ready.
     * CSS and layout processing will be done for the node, and any of its
     * children, prior to rendering it.
     * The entire destination image is cleared to the fill {@code Paint}
     * specified by the SnapshotParameters. This node is then rendered to
     * the image.
     * If the viewport specified by the SnapshotParameters is null, the
     * upper-left pixel of the {@code boundsInParent} of this
     * node, after first applying the transform specified by the
     * SnapshotParameters,
     * is mapped to the upper-left pixel (0,0) in the image.
     * If a non-null viewport is specified,
     * the upper-left pixel of the viewport is mapped to upper-left pixel
     * (0,0) in the image.
     * In both cases, this mapping to (0,0) of the image is done with an integer
     * translation. The portion of the node that is outside of the rendered
     * image will be clipped by the image.
     *
     * <p>
     * When taking a snapshot of a scene that is being animated, either
     * explicitly by the application or implicitly (such as chart animation),
     * the snapshot will be rendered based on the state of the scene graph at
     * the moment the snapshot is taken and will not reflect any subsequent
     * animation changes.
     * </p>
     *
     * <p>
     * NOTE: In order for CSS and layout to function correctly, the node
     * must be part of a Scene (the Scene may be attached to a Stage, but need
     * not be).
     * </p>
     *
     * @param params the snapshot parameters containing attributes that
     * will control the rendering. If the SnapshotParameters object is null,
     * then the Scene's attributes will be used if this node is part of a scene,
     * or default attributes will be used if this node is not part of a scene.
     *
     * @param image the writable image that will be used to hold the rendered node.
     * It may be null in which case a new WritableImage will be constructed.
     * The new image is constructed using integer width and
     * height values that are derived either from the transformed bounds of this
     * Node or from the size of the viewport as specified in the
     * SnapShotParameters. These integer values are chosen such that the image
     * will wholly contain the bounds of this Node or the specified viewport.
     * If the image is non-null, the node will be rendered into the
     * existing image.
     * In this case, the width and height of the image determine the area
     * that is rendered instead of the width and height of the bounds or
     * viewport.
     *
     * @throws IllegalStateException if this method is called on a thread
     *     other than the JavaFX Application Thread.
     *
     * @return the rendered image
     * @since JavaFX 2.2
     */
    public WritableImage snapshot(SnapshotParameters params, WritableImage image) {
        return getNodePeer().snapshot(params, image);
    }

    /* *************************************************************************
     *                                                                         *
     *                           Keyboard Handling                             *
     *                                                                         *
     **************************************************************************/

    public final void setOnKeyPressed(
            EventHandler<? super KeyEvent> value) {
        onKeyPressedProperty().set(value);
    }

    public final EventHandler<? super KeyEvent> getOnKeyPressed() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnKeyPressed();
    }

    /**
     * Defines a function to be called when this {@code Node} or its child
     * {@code Node} has input focus and a key has been pressed. The function
     * is called only if the event hasn't been already consumed during its
     * capturing or bubbling phase.
     * @return the event handler that is called when this {@code Node} or its
     * child {@code Node} has input focus and a key has been pressed
     */
    public final ObjectProperty<EventHandler<? super KeyEvent>>
    onKeyPressedProperty() {
        return getEventHandlerProperties().onKeyPressedProperty();
    }

    public final void setOnKeyReleased(
            EventHandler<? super KeyEvent> value) {
        onKeyReleasedProperty().set(value);
    }

    public final EventHandler<? super KeyEvent> getOnKeyReleased() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnKeyReleased();
    }

    /**
     * Defines a function to be called when this {@code Node} or its child
     * {@code Node} has input focus and a key has been released. The function
     * is called only if the event hasn't been already consumed during its
     * capturing or bubbling phase.
     * @return the event handler that is called when this {@code Node} or its
     * child {@code Node} has input focus and a key has been released
     */
    public final ObjectProperty<EventHandler<? super KeyEvent>>
    onKeyReleasedProperty() {
        return getEventHandlerProperties().onKeyReleasedProperty();
    }

    public final void setOnKeyTyped(
            EventHandler<? super KeyEvent> value) {
        onKeyTypedProperty().set(value);
    }

    public final EventHandler<? super KeyEvent> getOnKeyTyped() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnKeyTyped();
    }

    /**
     * Defines a function to be called when this {@code Node} or its child
     * {@code Node} has input focus and a key has been typed. The function
     * is called only if the event hasn't been already consumed during its
     * capturing or bubbling phase.
     * @return the event handler that is called when this {@code Node} or its
     * child {@code Node} has input focus and a key has been typed
     */
    public final ObjectProperty<EventHandler<? super KeyEvent>>
    onKeyTypedProperty() {
        return getEventHandlerProperties().onKeyTypedProperty();
    }


    public final void setOnSwipeUp(
            EventHandler<? super SwipeEvent> value) {
        onSwipeUpProperty().set(value);
    }

    public final EventHandler<? super SwipeEvent> getOnSwipeUp() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnSwipeUp();
    }

    /**
     * Defines a function to be called when an upward swipe gesture
     * centered over this node happens.
     * @return the event handler that is called when an upward swipe gesture
     * centered over this node happens
     * @since JavaFX 2.2
     */
    public final ObjectProperty<EventHandler<? super SwipeEvent>>
    onSwipeUpProperty() {
        return getEventHandlerProperties().onSwipeUpProperty();
    }

    public final void setOnSwipeDown(
            EventHandler<? super SwipeEvent> value) {
        onSwipeDownProperty().set(value);
    }

    public final EventHandler<? super SwipeEvent> getOnSwipeDown() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnSwipeDown();
    }

    /**
     * Defines a function to be called when a downward swipe gesture
     * centered over this node happens.
     * @return the event handler that is called when a downward swipe gesture
     * centered over this node happens
     * @since JavaFX 2.2
     */
    public final ObjectProperty<EventHandler<? super SwipeEvent>>
    onSwipeDownProperty() {
        return getEventHandlerProperties().onSwipeDownProperty();
    }

    public final void setOnSwipeLeft(
            EventHandler<? super SwipeEvent> value) {
        onSwipeLeftProperty().set(value);
    }

    public final EventHandler<? super SwipeEvent> getOnSwipeLeft() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnSwipeLeft();
    }

    /**
     * Defines a function to be called when a leftward swipe gesture
     * centered over this node happens.
     * @return the event handler that is called when a leftward swipe gesture
     * centered over this node happens
     * @since JavaFX 2.2
     */
    public final ObjectProperty<EventHandler<? super SwipeEvent>>
    onSwipeLeftProperty() {
        return getEventHandlerProperties().onSwipeLeftProperty();
    }

    public final void setOnSwipeRight(
            EventHandler<? super SwipeEvent> value) {
        onSwipeRightProperty().set(value);
    }

    public final EventHandler<? super SwipeEvent> getOnSwipeRight() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnSwipeRight();
    }

    /**
     * Defines a function to be called when an rightward swipe gesture
     * centered over this node happens.
     * @return the event handler that is called when an rightward swipe gesture
     * centered over this node happens
     * @since JavaFX 2.2
     */
    public final ObjectProperty<EventHandler<? super SwipeEvent>>
    onSwipeRightProperty() {
        return getEventHandlerProperties().onSwipeRightProperty();
    }


    /* *************************************************************************
     *                                                                         *
     *                             Touch Handling                              *
     *                                                                         *
     **************************************************************************/

    public final void setOnTouchPressed(
            EventHandler<? super TouchEvent> value) {
        onTouchPressedProperty().set(value);
    }

    public final EventHandler<? super TouchEvent> getOnTouchPressed() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnTouchPressed();
    }

    /**
     * Defines a function to be called when a new touch point is pressed.
     * @return the event handler that is called when a new touch point is pressed
     * @since JavaFX 2.2
     */
    public final ObjectProperty<EventHandler<? super TouchEvent>>
    onTouchPressedProperty() {
        return getEventHandlerProperties().onTouchPressedProperty();
    }

    public final void setOnTouchMoved(
            EventHandler<? super TouchEvent> value) {
        onTouchMovedProperty().set(value);
    }

    public final EventHandler<? super TouchEvent> getOnTouchMoved() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnTouchMoved();
    }

    /**
     * Defines a function to be called when a touch point is moved.
     * @return the event handler that is called when a touch point is moved
     * @since JavaFX 2.2
     */
    public final ObjectProperty<EventHandler<? super TouchEvent>>
    onTouchMovedProperty() {
        return getEventHandlerProperties().onTouchMovedProperty();
    }

    public final void setOnTouchReleased(
            EventHandler<? super TouchEvent> value) {
        onTouchReleasedProperty().set(value);
    }

    public final EventHandler<? super TouchEvent> getOnTouchReleased() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnTouchReleased();
    }

    /**
     * Defines a function to be called when a touch point is released.
     * @return the event handler that is called when a touch point is released
     * @since JavaFX 2.2
     */
    public final ObjectProperty<EventHandler<? super TouchEvent>>
    onTouchReleasedProperty() {
        return getEventHandlerProperties().onTouchReleasedProperty();
    }

    public final void setOnTouchStationary(
            EventHandler<? super TouchEvent> value) {
        onTouchStationaryProperty().set(value);
    }

    public final EventHandler<? super TouchEvent> getOnTouchStationary() {
        return (eventHandlerProperties == null)
                ? null : eventHandlerProperties.getOnTouchStationary();
    }

    /**
     * Defines a function to be called when a touch point stays pressed and
     * still.
     * @return the event handler that is called when a touch point stays pressed
     * and still
     * @since JavaFX 2.2
     */
    public final ObjectProperty<EventHandler<? super TouchEvent>>
    onTouchStationaryProperty() {
        return getEventHandlerProperties().onTouchStationaryProperty();
    }


    /**
     * Moves this {@code Node} to the back of its sibling nodes in terms of
     * z-order.  This is accomplished by moving this {@code Node} to the
     * first position in its parent's {@code content} ObservableList.
     * This function has no effect if this {@code Node} is not part of a group.
     */
    public void toBack() {
        if (getParent() != null) {
            getParent().toBack(this);
        }
    }

    /**
     * Moves this {@code Node} to the front of its sibling nodes in terms of
     * z-order.  This is accomplished by moving this {@code Node} to the
     * last position in its parent's {@code content} ObservableList.
     * This function has no effect if this {@code Node} is not part of a group.
     */
    public void toFront() {
        if (getParent() != null) {
            getParent().toFront(this);
        }
    }


}
