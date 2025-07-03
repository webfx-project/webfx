package javafx.scene.control;

import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.LayoutMeasurable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.ObservableList;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public abstract class Control extends Region implements Skinnable {

    // --- tooltip
    /**
     * The ToolTip for this control.
     * @return the tool tip for this control
     */
    public final ObjectProperty<Tooltip> tooltipProperty() {
        if (tooltip == null) {
            tooltip = new ObjectPropertyBase<>() {
                /*private Tooltip old = null;
                @Override protected void invalidated() {
                    Tooltip t = get();
                    // install / uninstall
                    if (t != old) {
                        if (old != null) {
                            Tooltip.uninstall(Control.this, old);
                        }
                        if (t != null) {
                            Tooltip.install(Control.this, t);
                        }
                        old = t;
                    }
                }*/

                @Override
                public Object getBean() {
                    return Control.this;
                }

                @Override
                public String getName() {
                    return "tooltip";
                }
            };
        }
        return tooltip;
    }
    private ObjectProperty<Tooltip> tooltip;
    public final void setTooltip(Tooltip value) { tooltipProperty().setValue(value); }
    public final Tooltip getTooltip() { return tooltip == null ? null : tooltip.getValue(); }

    // --- context menu
    /**
     * The ContextMenu to show for this control.
     */
    private ObjectProperty<ContextMenu> contextMenu = new SimpleObjectProperty<ContextMenu>(this, "contextMenu") {
        //private WeakReference<ContextMenu> contextMenuRef;

        @Override protected void invalidated() {
            /*ContextMenu oldMenu = contextMenuRef == null ? null : contextMenuRef.get();
            if (oldMenu != null) {
                ControlAcceleratorSupport.removeAcceleratorsFromScene(oldMenu.getItems(), Control.this);
            }*/

            ContextMenu ctx = get();
            //contextMenuRef = new WeakReference<>(ctx);

            if (ctx != null) {
                // set this flag so contextmenu show will be relative to parent window not anchor
                ctx.setShowRelativeToWindow(true); //RT-15160

                // if a context menu is set, we need to install any accelerators
                // belonging to its menu items ASAP into the scene that this
                // Control is in (if the control is not in a Scene, we will need
                // to wait until it is and then do it).
                //ControlAcceleratorSupport.addAcceleratorsIntoScene(ctx.getItems(), Control.this);
            }
        }
    };
    public final ObjectProperty<ContextMenu> contextMenuProperty() { return contextMenu; }
    public final void setContextMenu(ContextMenu value) { contextMenu.setValue(value); }
    public final ContextMenu getContextMenu() { return contextMenu == null ? null : contextMenu.getValue(); }



    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     *  Create a new Control.
     */
    protected Control() {
        // focusTraversable is styleable through css. Calling setFocusTraversable
        // makes it look to css like the user set the value and css will not
        // override. Initializing focusTraversable by calling applyStyle
        // with null for StyleOrigin ensures that css will be able to override
        // the value.
        final StyleableProperty<Boolean> prop = (StyleableProperty<Boolean>)(WritableValue<Boolean>)focusTraversableProperty();
        prop.applyStyle(null, Boolean.TRUE);

        // we add a listener for menu request events to show the context menu
        // that may be set on the Control
        this.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, contextMenuHandler);
    }

    /***************************************************************************
     *                                                                         *
     * Event Handlers / Listeners                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Handles context menu requests by popping up the menu.
     * Note that we use this pattern to remove some of the anonymous inner
     * classes which we'd otherwise have to create. When lambda expressions
     * are supported, we could do it that way instead (or use MethodHandles).
     */
    private final static EventHandler<ContextMenuEvent> contextMenuHandler = event -> {
        if (event.isConsumed()) return;

        // If a context menu was shown, consume the event to prevent multiple context menus
        Object source = event.getSource();
        if (source instanceof Control) {
            Control c = (Control) source;
            if (c.getContextMenu() != null) {
                c.getContextMenu().show(c, event.getScreenX(), event.getScreenY());
                event.consume();
            }
        }
    };



    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * A private reference directly to the SkinBase instance that is used as the
     * Skin for this Control. A Control's Skin doesn't have to be of type
     * SkinBase, although 98% of the time or greater it probably will be.
     * Because instanceof checks and reading a value from a property are
     * not cheap (on interpreters on slower hardware or mobile devices)
     * it pays to have a direct reference here to the skinBase. We simply
     * need to check this variable -- if it is not null then we know the
     * Skin is a SkinBase and this is a direct reference to it. If it is null
     * then we know the skin is not a SkinBase and we need to call getSkin().
     */
    private SkinBase<?> skinBase;

    /***************************************************************************
     * Forward the following to the skin                                       *
     **************************************************************************/

    /**
     * Create a new instance of the default skin for this control. This is called to create a skin for the control if
     * no skin is provided via CSS {@code -fx-skin} or set explicitly in a sub-class with {@code  setSkin(...)}.
     *
     * @return  new instance of default skin for this control. If null then the control will have no skin unless one
     *          is provided by css.
     * @since JavaFX 8.0
     */
    protected Skin<?> createDefaultSkin() {
        return null;
    }

    // --- skin
    /**
     * Skin is responsible for rendering this {@code Control}. From the
     * perspective of the {@code Control}, the {@code Skin} is a black box.
     * It listens and responds to changes in state in a {@code Control}.
     * <p>
     * There is a one-to-one relationship between a {@code Control} and its
     * {@code Skin}. Every {@code Skin} maintains a back reference to the
     * {@code Control} via the {@link Skin#getSkinnable()} method.
     * <p>
     * A skin may be null.
     */
    private ObjectProperty<Skin<?>> skin;
    @Override public final ObjectProperty<Skin<?>> skinProperty() {
        if (skin == null) {
            skin = new SimpleObjectProperty<Skin<?>>() {
                private boolean created;
                @Override
                public Skin<?> get() {
                    Skin<?> skin = super.get();
                    if (!created) {
                        created = true;
                        if (skin == null)
                            set(skin = createDefaultSkin());
                    }
                    return skin;
                }

                // We store a reference to the oldValue so that we can handle
                // changes in the skin properly in the case of binding. This is
                // only needed because invalidated() does not currently take
                // a reference to the old value.
                private Skin<?> oldValue;

                @Override
                //This code is basically a kind of optimization that prevents a Skin that is equal but not instance equal.
                //Although it's not kosher from the property perspective (bindings won't pass through set), it should not do any harm.
                //But it should be evaluated in the future.
                public void set(Skin<?> v) {
                    created = true;
                    if (v == null
                            ? oldValue == null
                            : oldValue != null && v.getClass().equals(oldValue.getClass()))
                        return;

                    super.set(v);
                }

                @Override protected void invalidated() {
                    Skin<?> skin = get();
                    // Collect the name of the currently installed skin class. We do this
                    // so that subsequent updates from CSS to the same skin class will not
                    // result in reinstalling the skin
                    // currentSkinClassName = skin == null ? null : skin.getClass().getName();

                    // if someone calls setSkin, we need to make it look like they
                    // called set on skinClassName in order to keep CSS from overwriting
                    // the skin.
                    // skinClassNameProperty().set(currentSkinClassName);


                    // Dispose of the old skin
                    if (oldValue != null) oldValue.dispose();

                    // Get the new value, and save it off as the new oldValue
                    oldValue = skin;

                    // Reset skinBase to null - it will be set to the new Skin if it
                    // is a SkinBase, otherwise it will remain null, as expected
                    skinBase = null;

                    // We have two paths, one for "legacy" Skins, and one for
                    // any Skin which extends from SkinBase. Legacy Skins will
                    // produce a single node which will be the only child of
                    // the Control via the getNode() method on the Skin. A
                    // SkinBase will manipulate the children of the Control
                    // directly. Further, we maintain a direct reference to
                    // the skinBase for more optimal updates later.
                    if (skin instanceof SkinBase) {
                        // record a reference of the skin, if it is a SkinBase, for
                        // performance reasons
                        skinBase = (SkinBase<?>) skin;
                        // Note I do not remove any children here, because the
                        // skin will have already configured all the children
                        // by the time setSkin has been called. This is because
                        // our Skin interface was lacking an initialize method (doh!)
                        // and so the Skin constructor is where it adds listeners
                        // and so forth. For SkinBase implementations, the
                        // constructor is also where it will take ownership of
                        // the children.
                    } else {
                        final Node n = getSkinNode();
                        if (n != null) {
                            getChildren().setAll(n);
                        } else {
                            getChildren().clear();
                        }
                    }

                    // let the new skin modify this control
                    if (skin != null) {
                        skin.install();
                    }

                    // clear out the styleable properties so that the list is rebuilt
                    // next time they are requested.
                    //styleableProperties = null;

                    // calling impl_reapplyCSS() as the styleable properties may now
                    // be different, as we will now be able to return styleable properties
                    // belonging to the skin. If impl_reapplyCSS() is not called, the
                    // getCssMetaData() method is never called, so the
                    // skin properties are never exposed.
                    //impl_reapplyCSS();

                    // WebFX addition to fix the following problem: SplitPaneSkin.layoutChildren() was not called
                    // when initializing the scene with a SplitPane as root node (ex: in responsive design demo)
                    // so the window just looked empty (but resizing it made the nodes appear).
                    //layoutChildren(); Finally commented as VisualGridVerticalSkin appears in wrong position (0,0) TODO: see possible side effects: no => remove / yes => move this to SplitPaneSkin.install()

                    // DEBUG: Log that we've changed the skin
/*
            final PlatformLogger logger = Logging.getControlsLogger();
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("Stored skin[" + getValue() + "] on " + this);
            }
*/
                }

                // This method should be CssMetaData<Control,Skin> getCssMetaData(),
                // but SKIN is CssMetaData<Control,String>. This does not matter to
                // the CSS code which doesn't care about the actual type. Hence,
                // we'll suppress the warnings
/*
        @Override @SuppressWarnings({"unchecked", "rawtype"})
        public CssMetaData getCssMetaData() {
            return StyleableProperties.SKIN;
        }
*/

                @Override
                public Object getBean() {
                    return Control.this;
                }

                @Override
                public String getName() {
                    return "skin";
                }
            };
        }
        return skin;
    }
    @Override public final void setSkin(Skin<?> value) {
        skinProperty().set(value);
    }
    @Override public final Skin<?> getSkin() { return skinProperty().getValue(); }

    // Implementation of the Resizable interface.
    // Because only the skin can know the min, pref, and max sizes, these
    // functions are implemented to delegate to skin. If there is no skin then
    // we simply return 0 for all the values since a Control without a Skin
    // doesn't render
    /**
     * Computes the minimum allowable width of the Control, based on the provided
     * height. The minimum width is not calculated within the Control, instead
     * the calculation is delegated to the {@link Node#minWidth(double)} method
     * of the {@link Skin}. If the Skin is null, the returned value is 0.
     *
     * @param height The height of the Control, in case this value might dictate
     *      the minimum width.
     * @return A double representing the minimum width of this control.
     */
    @Override protected double computeMinWidth(final double height) {
        if (skinBase != null) {
            return skinBase.computeMinWidth(height, snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset());
        } else {
            if (getScene() != null) {
                NodePeer nodePeer = getOrCreateAndBindNodePeer();
                if (nodePeer instanceof LayoutMeasurable)
                    return ((LayoutMeasurable) nodePeer).minWidth(height);
            }
            final Node skinNode = getSkinNode();
            return skinNode == null ? 0 : skinNode.minWidth(height);
        }
    }

    /**
     * Computes the minimum allowable height of the Control, based on the provided
     * width. The minimum height is not calculated within the Control, instead
     * the calculation is delegated to the {@link Node#minHeight(double)} method
     * of the {@link Skin}. If the Skin is null, the returned value is 0.
     *
     * @param width The width of the Control, in case this value might dictate
     *      the minimum height.
     * @return A double representing the minimum height of this control.
     */
    @Override protected double computeMinHeight(final double width) {
        if (skinBase != null) {
            return skinBase.computeMinHeight(width, snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset());
        } else {
            if (getScene() != null) {
                NodePeer nodePeer = getOrCreateAndBindNodePeer();
                if (nodePeer instanceof LayoutMeasurable)
                    return ((LayoutMeasurable) nodePeer).minHeight(width);
            }
            final Node skinNode = getSkinNode();
            return skinNode == null ? 0 : skinNode.minHeight(width);
        }
    }

    /**
     * Computes the maximum allowable width of the Control, based on the provided
     * height. The maximum width is not calculated within the Control, instead
     * the calculation is delegated to the {@link Node#maxWidth(double)} method
     * of the {@link Skin}. If the Skin is null, the returned value is 0.
     *
     * @param height The height of the Control, in case this value might dictate
     *      the maximum width.
     * @return A double representing the maximum width of this control.
     */
    @Override protected double computeMaxWidth(double height) {
        if (skinBase != null) {
            return skinBase.computeMaxWidth(height, snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset());
        } else {
            if (getScene() != null) {
                NodePeer nodePeer = getOrCreateAndBindNodePeer();
                if (nodePeer instanceof LayoutMeasurable)
                    return ((LayoutMeasurable) nodePeer).maxWidth(height);
            }
            final Node skinNode = getSkinNode();
            return skinNode == null ? 0 : skinNode.maxWidth(height);
        }
    }

    /**
     * Computes the maximum allowable height of the Control, based on the provided
     * width. The maximum height is not calculated within the Control, instead
     * the calculation is delegated to the {@link Node#maxHeight(double)} method
     * of the {@link Skin}. If the Skin is null, the returned value is 0.
     *
     * @param width The width of the Control, in case this value might dictate
     *      the maximum height.
     * @return A double representing the maximum height of this control.
     */
    @Override protected double computeMaxHeight(double width) {
        if (skinBase != null) {
            return skinBase.computeMaxHeight(width, snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset());
        } else {
            if (getScene() != null) {
                NodePeer nodePeer = getOrCreateAndBindNodePeer();
                if (nodePeer instanceof LayoutMeasurable)
                    return ((LayoutMeasurable) nodePeer).maxHeight(width);
            }
            final Node skinNode = getSkinNode();
            return skinNode == null ? 0 : skinNode.maxHeight(width);
        }
    }

    /** {@inheritDoc} */
    @Override protected double computePrefWidth(double height) {
        if (skinBase != null) {
            return skinBase.computePrefWidth(height, snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset());
        } else {
            if (getScene() != null) {
                NodePeer nodePeer = getOrCreateAndBindNodePeer();
                if (nodePeer instanceof LayoutMeasurable)
                    return ((LayoutMeasurable) nodePeer).prefWidth(height);
            }
            final Node skinNode = getSkinNode();
            return skinNode == null ? 0 : skinNode.prefWidth(height);
        }
    }

    /** {@inheritDoc} */
    @Override protected double computePrefHeight(double width) {
        if (skinBase != null) {
            return skinBase.computePrefHeight(width, snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset());
        } else {
            if (getScene() != null) {
                NodePeer nodePeer = getOrCreateAndBindNodePeer();
                if (nodePeer instanceof LayoutMeasurable)
                    return ((LayoutMeasurable) nodePeer).prefHeight(width);
            }
            final Node skinNode = getSkinNode();
            return skinNode == null ? 0 : skinNode.prefHeight(width);
        }
    }

    /** {@inheritDoc} */
    @Override public double getBaselineOffset() {
        if (skinBase != null) {
            return skinBase.computeBaselineOffset(snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset());
        } else {
            final Node skinNode = getSkinNode();
            return skinNode == null ? 0 : skinNode.getBaselineOffset();
        }
    }

    /***************************************************************************
     * Implementation of layout bounds for the Control. We want to preserve    *
     * the lazy semantics of layout bounds. So whenever the width/height       *
     * changes on the node, we end up invalidating layout bounds. We then      *
     * recompute it on demand.                                                 *
     **************************************************************************/

    /** {@inheritDoc} */
    @Override protected void layoutChildren() {
        if (skinBase != null) {
            final double x = snappedLeftInset();
            final double y = snappedTopInset();
            final double w = snapSize(getWidth()) - x - snappedRightInset();
            final double h = snapSize(getHeight()) - y - snappedBottomInset();
            skinBase.layoutChildren(x, y, w, h);
        } else {
            Node n = getSkinNode();
            if (n != null) {
                n.resizeRelocate(0, 0, getWidth(), getHeight());
            }
        }
    }

    /***************************************************************************
     *                                                                         *
     * Package API for SkinBase                                                *
     *                                                                         *
     **************************************************************************/

    // package private for SkinBase
    ObservableList<Node> getControlChildren() {
        return getChildren();
    }

    /***************************************************************************
     *                                                                         *
     * Private implementation                                                  *
     *                                                                         *
     **************************************************************************/

    /**
     * Gets the Skin's node, or returns null if there is no Skin.
     * Convenience method for getting the node of the skin. This is null-safe,
     * meaning if skin is null then it will return null instead of throwing
     * a NullPointerException.
     *
     * @return The Skin's node, or null.
     */
    private Node getSkinNode() {
        assert skinBase == null;
        Skin<?> skin = getSkin();
        return skin == null ? null : skin.getNode();
    }

    @Override
    public boolean shouldUseLayoutMeasurable() {
        return getSkin() == null;
    }

    {
        // Simulating skin mechanism (normally done during css pass - not yet implemented)
        // Scheduler.scheduleDelay(5000, () -> setSkin(createDefaultSkin()));
    }

    /**
     * Returns the initial focus traversable state of this control, for use
     * by the JavaFX CSS engine to correctly set its initial value. By default all
     * UI controls are focus traversable, so this method is overridden in Control
     * to set the initial traversable state to true.
     *
     * @return the initial focus traversable state of this control
     * @since 9
     */
    @Override protected Boolean getInitialFocusTraversable() {
        return Boolean.TRUE;
    }

}
