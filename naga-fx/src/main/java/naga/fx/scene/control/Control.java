package naga.fx.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import naga.fx.scene.LayoutMeasurable;
import naga.fx.scene.Node;
import naga.fx.scene.layout.Region;
import naga.fx.spi.Toolkit;
import naga.fx.spi.viewer.NodeViewer;

/**
 * @author Bruno Salmon
 */
public abstract class Control extends Region implements Skinnable {

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
    @Override public final ObjectProperty<Skin<?>> skinProperty() { return skin; }
    @Override public final void setSkin(Skin<?> value) {
        skinProperty().set(value);
    }
    @Override public final Skin<?> getSkin() { return skinProperty().getValue(); }
    private ObjectProperty<Skin<?>> skin = new SimpleObjectProperty<Skin<?>>() {
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

            // clear out the styleable properties so that the list is rebuilt
            // next time they are requested.
            //styleableProperties = null;

            // calling impl_reapplyCSS() as the styleable properties may now
            // be different, as we will now be able to return styleable properties
            // belonging to the skin. If impl_reapplyCSS() is not called, the
            // getCssMetaData() method is never called, so the
            // skin properties are never exposed.
            //impl_reapplyCSS();

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
                NodeViewer nodeViewer = getOrCreateAndBindNodeViewer();
                if (nodeViewer instanceof LayoutMeasurable)
                    return ((LayoutMeasurable) nodeViewer).minWidth(height);
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
                NodeViewer nodeViewer = getOrCreateAndBindNodeViewer();
                if (nodeViewer instanceof LayoutMeasurable)
                    return ((LayoutMeasurable) nodeViewer).minHeight(width);
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
                NodeViewer nodeViewer = getOrCreateAndBindNodeViewer();
                if (nodeViewer instanceof LayoutMeasurable)
                    return ((LayoutMeasurable) nodeViewer).maxWidth(height);
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
                NodeViewer nodeViewer = getOrCreateAndBindNodeViewer();
                if (nodeViewer instanceof LayoutMeasurable)
                    return ((LayoutMeasurable) nodeViewer).maxHeight(width);
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
                NodeViewer nodeViewer = getOrCreateAndBindNodeViewer();
                if (nodeViewer instanceof LayoutMeasurable)
                    return ((LayoutMeasurable) nodeViewer).prefWidth(height);
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
                NodeViewer nodeViewer = getOrCreateAndBindNodeViewer();
                if (nodeViewer instanceof LayoutMeasurable)
                    return ((LayoutMeasurable) nodeViewer).prefHeight(width);
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

    {
        // Simulating skin mechanism (normally done during css pass - not yet implemented)
        Toolkit.get().scheduler().scheduleDeferred(() -> setSkin(createDefaultSkin()));
    }
}
