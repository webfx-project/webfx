package javafx.scene.layout;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.util.TempState;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.util.Callback;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasBackgroundProperty;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasBorderProperty;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasPaddingProperty;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasSnapToPixelProperty;
import dev.webfx.kit.registry.javafxgraphics.JavaFxGraphicsRegistry;

import java.util.List;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public class Region extends Parent implements
        PreferenceResizableNode,
        HasPaddingProperty,
        HasSnapToPixelProperty,
        HasBackgroundProperty,
        HasBorderProperty {

    public Region() {
    }

    public Region(Node... nodes) {
        super(nodes);
    }

    private final DoubleProperty widthProperty = new SimpleDoubleProperty(0d) {
        private double lastValue = -1;
        @Override
        protected void invalidated() {
            double value = getValue();
            if (value != lastValue) { // double check (because it may happen to have a new Double instance but with actual same value as previous one)
                widthOrHeightChanged();
                lastValue = value;
            }
        }
    };
    @Override
    public ReadOnlyDoubleProperty widthProperty() {
        return widthProperty;
    }

    public DoubleProperty widthPropertyImpl() {
        return widthProperty;
    }

    public void setWidth(double value) {
        widthProperty.set(value);
    }

    private final DoubleProperty heightProperty = new SimpleDoubleProperty(0d) {
        private double lastValue = -1;
        @Override
        protected void invalidated() {
            double value = getValue();
            if (value != lastValue) { // double check (because it may happen to have a new Double instance but with actual same value as previous one)
                widthOrHeightChanged();
                lastValue = value;
            }
        }
    };
    @Override
    public ReadOnlyDoubleProperty heightProperty() {
        return heightProperty;
    }

    public DoubleProperty heightPropertyImpl() {
        return heightProperty;
    }

    public void setHeight(double value) {
        heightProperty.set(value);
    }

    private class MinPrefMaxProperty extends SimpleDoubleProperty {
        public MinPrefMaxProperty() {
            super(USE_COMPUTED_SIZE);
        }

        @Override
        protected void invalidated() {
            requestParentLayout();
        }
    }

    private final DoubleProperty maxWidthProperty = new MinPrefMaxProperty();
    @Override
    public DoubleProperty maxWidthProperty() {
        return maxWidthProperty;
    }

    private final DoubleProperty minWidthProperty = new MinPrefMaxProperty();
    @Override
    public DoubleProperty minWidthProperty() {
        return minWidthProperty;
    }

    private final DoubleProperty maxHeightProperty = new MinPrefMaxProperty();
    @Override
    public DoubleProperty maxHeightProperty() {
        return maxHeightProperty;
    }

    private final DoubleProperty minHeightProperty = new MinPrefMaxProperty();
    @Override
    public DoubleProperty minHeightProperty() {
        return minHeightProperty;
    }

    private final DoubleProperty prefWidthProperty = new MinPrefMaxProperty();
    @Override
    public DoubleProperty prefWidthProperty() {
        return prefWidthProperty;
    }

    private final DoubleProperty prefHeightProperty = new MinPrefMaxProperty();
    @Override
    public DoubleProperty prefHeightProperty() {
        return prefHeightProperty;
    }

    /**
     * The insets of the Region define the distance from the edge of the region (its layout bounds,
     * or (0, 0, width, height)) to the edge of the content area. All child nodes should be laid out
     * within the content area. The insets are computed based on the Border which has been specified,
     * if any, and also the padding.
     * @since JavaFX 8.0
     */
    private final InsetsProperty insets = new InsetsProperty();
    public final Insets getInsets() { return insets.get(); }
    public final Property<Insets> insetsProperty() { return insets; }
    private final class InsetsProperty extends SimpleObjectProperty<Insets> {
        private Insets cache = null;
        private ExpressionHelper<Insets> helper = null;

        @Override public Object getBean() { return Region.this; }
        @Override public String getName() { return "insets"; }

        @Override public void addListener(InvalidationListener listener) {
            helper = ExpressionHelper.addListener(helper, this, listener);
        }

        @Override public void removeListener(InvalidationListener listener) {
            helper = ExpressionHelper.removeListener(helper, listener);
        }

        @Override public void addListener(ChangeListener<? super Insets> listener) {
            helper = ExpressionHelper.addListener(helper, this, listener);
        }

        @Override public void removeListener(ChangeListener<? super Insets> listener) {
            helper = ExpressionHelper.removeListener(helper, listener);
        }

        void fireValueChanged() {
            cache = null;
            updateSnappedInsets();
            requestLayout();
            ExpressionHelper.fireValueChangedEvent(helper);
        }

        @Override public Insets get() {
            // If a shape is specified, then we don't really care whether there are any borders
            // specified, since borders of shapes do not contribute to the insets.
            //if (_shape != null) return getPadding();

            // If there is no border or the border has no insets itself, then the only thing
            // affecting the insets is the padding, so we can just return it directly.
            final Border b = getBorder();
            if (b == null || Insets.EMPTY.equals(b.getInsets())) {
                return getPadding();
            }

            // There is a border with some non-zero insets and we do not have a _shape, so we need
            // to take the border's insets into account
            if (cache == null) {
                // Combine the padding and the border insets.
                // TODO note that negative border insets were being ignored, but
                // I'm not sure that that made sense or was reasonable, so I have
                // changed it so that we just do simple math.
                // TODO Stroke borders should NOT contribute to the insets. Ensure via tests.
                final Insets borderInsets = b.getInsets();
                final Insets paddingInsets = getPadding();
                cache = new Insets(
                        borderInsets.getTop() + paddingInsets.getTop(),
                        borderInsets.getRight() + paddingInsets.getRight(),
                        borderInsets.getBottom() + paddingInsets.getBottom(),
                        borderInsets.getLeft() + paddingInsets.getLeft()
                );
            }
            return cache;
        }
    };

    private final ObjectProperty<Insets> padding = new SimpleObjectProperty<Insets>(Insets.EMPTY) {
        // Keep track of the last valid value for the sake of
        // rollback in case padding is set to null. Note that
        // Richard really does not like this pattern because
        // it essentially means that binding the padding property
        // is not possible since a binding expression could very
        // easily produce an intermediate null value.

        // Also note that because padding is set virtually everywhere via CSS, and CSS
        // requires a property object in order to set it, there is no benefit to having
        // lazy initialization here.

        private Insets lastValidValue = Insets.EMPTY;

        @Override public Object getBean() { return Region.this; }
        @Override public String getName() { return "padding"; }
/*
        @Override public CssMetaData<Region, Insets> getCssMetaData() {
            return Region.StyleableProperties.PADDING;
        }
*/
        @Override public void invalidated() {
            final Insets newValue = get();
            if (newValue == null) {
                // rollback
                if (isBound()) {
                    unbind();
                }
                set(lastValidValue);
                throw new NullPointerException("cannot set padding to null");
            } else if (!newValue.equals(lastValidValue)) {
                lastValidValue = newValue;
                insets.fireValueChanged();
            }
        }
    };
    @Override
    public ObjectProperty<Insets> paddingProperty() {
        return padding;
    }

    // Note about snapToPixel property: its default value is true in OpenJFX, but WebFX changes this default value to
    // false, as it has been observed this produces better results in the browser. A 1px mistake may happen during
    // regions layout with snapToPixel = true, while it's resolved with snapToPixel = false. With snapToPixel = false,
    // the mapping may produce non-integer values for HTML positions (ex: left: 15.2, width: 338.5, etc...) but the
    // browser is snapping these values anyway. The issue in WebFX with snapToPixel = true probably comes from the WebFX
    // method Node.getAllNodeTransforms() that reads back the layoutX/Y values to compute an overall transform for the
    // Node that will be transmitted to the HTML mapper. This overall transform requires precise values when followed
    // by other transforms such as rotate and scale.
    // UPDATE 21/07/2025: finally set it back to true to solve some trembling vertical position issues with nodes in
    // VBoxes during animation effects on the container height such as CollapsePane or TransitionPane with animated height
    private final BooleanProperty snapToPixelProperty = new SimpleBooleanProperty(true) {
        @Override
        protected void invalidated() {
            updateSnappedInsets();
            requestParentLayout();
        }
    };

    @Override
    public BooleanProperty snapToPixelProperty() {
        return snapToPixelProperty;
    }

    private final ObjectProperty<Background> backgroundProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<Background> backgroundProperty() {
        return backgroundProperty;
    }

    private final ObjectProperty<Border> borderProperty = new SimpleObjectProperty<>() {
        @Override
        protected void invalidated() {
            insets.fireValueChanged();
        }
    };
    @Override
    public ObjectProperty<Border> borderProperty() {
        return borderProperty;
    }

    private void widthOrHeightChanged() {
        // It is possible that somebody sets the width of the region to a value which
        // it previously held. If this is the case, we want to avoid excessive layouts.
        // Note that I have biased this for layout over binding, because the widthProperty
        // is now going to recompute the width eagerly. The cost of excessive and
        // unnecessary bounds changes, however, is relatively high.
        //cornersValid = false;
        boundingBox = null;
        //impl_layoutBoundsChanged();
        //impl_geomChanged();
        //impl_markDirty(DirtyBits.NODE_GEOMETRY);
        setNeedsLayout(true);
        requestParentLayout();
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        // Unlike Group, a Region has its own intrinsic geometric bounds, even if it has no children.
        // The bounds of the Region must take into account any backgrounds and borders and how
        // they are used to draw the Region. The geom bounds must always take into account
        // all pixels drawn (because the geom bounds forms the basis of the dirty regions).
        // Note that the layout bounds of a Region is not based on the geom bounds.

        // Define some variables to hold the top-left and bottom-right corners of the bounds
        double bx1 = 0;
        double by1 = 0;
        double bx2 = getWidth();
        double by2 = getHeight();

/*
        // If the shape is defined, then the top-left and bottom-right corner positions
        // need to be redefined
        if (_shape != null && isScaleShape() == false) {
            // We will hijack the bounds here temporarily just to compute the shape bounds
            BaseBounds shapeBounds = computeShapeBounds(bounds);
            double shapeWidth = shapeBounds.getWidth();
            double shapeHeight = shapeBounds.getHeight();
            if (isCenterShape()) {
                bx1 = (bx2 - shapeWidth) / 2;
                by1 = (by2 - shapeHeight) / 2;
                bx2 = bx1 + shapeWidth;
                by2 = by1 + shapeHeight;
            } else {
                bx1 = shapeBounds.getMinX();
                by1 = shapeBounds.getMinY();
                bx2 = shapeBounds.getMaxX();
                by2 = shapeBounds.getMaxY();
            }
        } else {
            // Expand the bounds to include the outsets from the background and border.
            // The outsets are the opposite of insets -- a measure of distance from the
            // edge of the Region outward. The outsets cannot, however, be negative.
            Background background = getBackground();
            Border border = getBorder();
            Insets backgroundOutsets = background == null ? Insets.EMPTY : background.getOutsets();
            Insets borderOutsets = border == null ? Insets.EMPTY : border.getOutsets();
            bx1 -= Math.max(backgroundOutsets.getLeft(), borderOutsets.getLeft());
            by1 -= Math.max(backgroundOutsets.getTop(), borderOutsets.getTop());
            bx2 += Math.max(backgroundOutsets.getRight(), borderOutsets.getRight());
            by2 += Math.max(backgroundOutsets.getBottom(), borderOutsets.getBottom());
        }
*/
        // NOTE: Okay to call impl_computeGeomBounds with tx even in the 3D case
        // since Parent.impl_computeGeomBounds does handle 3D correctly.
        BaseBounds cb = super.impl_computeGeomBounds(bounds, tx);
        /*
         * This is a work around for RT-7680. Parent returns invalid bounds from
         * impl_computeGeomBounds when it has no children or if all its children
         * have invalid bounds. If RT-7680 were fixed, then we could omit this
         * first branch of the if and only use the else since the correct value
         * would be computed.
         */
        if (cb.isEmpty()) {
            // There are no children bounds, so
            bounds = bounds.deriveWithNewBounds((float) bx1, (float) by1, 0, (float) bx2, (float) by2, 0);
            bounds = tx.transform(bounds, bounds);
            return bounds;
        } else {
            // Union with children's bounds
            BaseBounds tempBounds = TempState.getInstance().bounds;
            tempBounds = tempBounds.deriveWithNewBounds((float) bx1, (float) by1, 0, (float) bx2, (float) by2, 0);
            BaseBounds bb = tx.transform(tempBounds, tempBounds);
            cb = cb.deriveWithUnion(bb);
            return cb;
        }
    }

    /**
     * cached results of snapped insets, this are used a lot during layout so makes sense
     * to keep fast access cached copies here.
     */
    private double snappedTopInset = 0;
    private double snappedRightInset = 0;
    private double snappedBottomInset = 0;
    private double snappedLeftInset = 0;

    /** Called to update the cached snapped insets */
    private void updateSnappedInsets() {
        Insets insets = getInsets();
        if (isSnapToPixel()) {
            snappedTopInset = Math.ceil(insets.getTop());
            snappedRightInset = Math.ceil(insets.getRight());
            snappedBottomInset = Math.ceil(insets.getBottom());
            snappedLeftInset = Math.ceil(insets.getLeft());
        } else {
            snappedTopInset = insets.getTop();
            snappedRightInset = insets.getRight();
            snappedBottomInset = insets.getBottom();
            snappedLeftInset = insets.getLeft();
        }
    }


    /**
     * Invoked by the region's parent during layout to set the region's
     * width and height. <b>Applications should not invoke this method directly</b>.
     * If an application needs to directly set the size of the region, it should
     * override its size constraints by calling <code>setMinSize()</code>,
     *  <code>setPrefSize()</code>, or <code>setMaxSize()</code> and it's parent
     * will honor those overrides during layout.
     *
     * @param width the target layout bounds width
     * @param height the target layout bounds height
     */
    @Override
    public void resize(double width, double height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * Called during layout to determine the minimum width for this node.
     * Returns the value from <code>computeMinWidth(forHeight)</code> unless
     * the application overrode the minimum width by setting the minWidth property.
     *
     * @see #setMinWidth(Double)
     * @return the minimum width that this node should be resized to during layout
     */
    @Override
    protected final double impl_minWidth(double height) {
        double override = getMinWidth();
        if (override == USE_COMPUTED_SIZE)
            return super.impl_minWidth(height);
        if (override == USE_PREF_SIZE)
            return impl_prefWidth(height);
        return Double.isNaN(override) || override < 0 ? 0 : override;
    }

    /**
     * Called during layout to determine the minimum height for this node.
     * Returns the value from <code>computeMinHeight(forWidth)</code> unless
     * the application overrode the minimum height by setting the minHeight property.
     *
     * @see #setMinHeight
     * @return the minimum height that this node should be resized to during layout
     */
    @Override
    protected final double impl_minHeight(double width) {
        double override = getMinHeight();
        if (override == USE_COMPUTED_SIZE)
            return super.impl_minHeight(width);
        if (override == USE_PREF_SIZE)
            return impl_prefHeight(width);
        return Double.isNaN(override) || override < 0 ? 0 : override;
    }

    /**
     * Called during layout to determine the preferred width for this node.
     * Returns the value from <code>computePrefWidth(forHeight)</code> unless
     * the application overrode the preferred width by setting the prefWidth property.
     *
     * @see #setPrefWidth
     * @return the preferred width that this node should be resized to during layout
     */
    @Override
    protected final double impl_prefWidth(double height) {
        double override = getPrefWidth();
        if (override == USE_COMPUTED_SIZE)
            return super.impl_prefWidth(height);
        return Double.isNaN(override) || override < 0 ? 0 : override;
    }

    /**
     * Called during layout to determine the preferred height for this node.
     * Returns the value from <code>computePrefHeight(forWidth)</code> unless
     * the application overrode the preferred height by setting the prefHeight property.
     *
     * @see #setPrefHeight
     * @return the preferred height that this node should be resized to during layout
     */
    @Override
    protected final double impl_prefHeight(double width) {
        double override = getPrefHeight();
        if (override == USE_COMPUTED_SIZE)
            return super.impl_prefHeight(width);
        return Double.isNaN(override) || override < 0 ? 0 : override;
    }

    /**
     * Called during layout to determine the maximum width for this node.
     * Returns the value from <code>computeMaxWidth(forHeight)</code> unless
     * the application overrode the maximum width by setting the maxWidth property.
     *
     * @see #setMaxWidth
     * @return the maximum width that this node should be resized to during layout
     */
    @Override
    protected final double impl_maxWidth(double height) {
        double override = getMaxWidth();
        if (override == USE_COMPUTED_SIZE)
            return computeMaxWidth(height);
        if (override == USE_PREF_SIZE)
            return impl_prefWidth(height);
        return Double.isNaN(override) || override < 0 ? 0 : override;
    }

    /**
     * Called during layout to determine the maximum height for this node.
     * Returns the value from <code>computeMaxHeight(forWidth)</code> unless
     * the application overrode the maximum height by setting the maxHeight property.
     *
     * @see #setMaxHeight
     * @return the maximum height that this node should be resized to during layout
     */
    @Override
    protected final double impl_maxHeight(double width) {
        double override = getMaxHeight();
        if (override == USE_COMPUTED_SIZE)
            return computeMaxHeight(width);
        if (override == USE_PREF_SIZE)
            return impl_prefHeight(width);
        return Double.isNaN(override) || override < 0 ? 0 : override;
    }

    /**
     * Computes the minimum width of this region.
     * Returns the sum of the left and right insets by default.
     * region subclasses should override this method to return an appropriate
     * value based on their content and layout strategy.  If the subclass
     * doesn't have a VERTICAL content bias, then the height parameter can be
     * ignored.
     *
     * @return the computed minimum width of this region
     */
    @Override
    protected double computeMinWidth(double height) {
        return getPadding().getLeft() + getPadding().getRight();
    }

    /**
     * Computes the minimum height of this region.
     * Returns the sum of the top and bottom insets by default.
     * Region subclasses should override this method to return an appropriate
     * value based on their content and layout strategy.  If the subclass
     * doesn't have a HORIZONTAL content bias, then the width parameter can be
     * ignored.
     *
     * @return the computed minimum height for this region
     */
    @Override
    protected double computeMinHeight(double width) {
        return getPadding().getTop() + getPadding().getBottom();
    }

    /**
     * Computes the preferred width of this region for the given height.
     * Region subclasses should override this method to return an appropriate
     * value based on their content and layout strategy.  If the subclass
     * doesn't have a VERTICAL content bias, then the height parameter can be
     * ignored.
     *
     * @return the computed preferred width for this region
     */
    @Override
    protected double computePrefWidth(double height) {
        double w = super.computePrefWidth(height);
        Insets insets = getPadding();
        return insets.getLeft() + w + insets.getRight();
    }

    /**
     * Computes the preferred height of this region for the given width;
     * Region subclasses should override this method to return an appropriate
     * value based on their content and layout strategy.  If the subclass
     * doesn't have a HORIZONTAL content bias, then the width parameter can be
     * ignored.
     *
     * @return the computed preferred height for this region
     */
    @Override
    protected double computePrefHeight(double width) {
        double h = super.computePrefHeight(width);
        Insets insets = getPadding();
        return insets.getTop() + h + insets.getBottom();
    }

    /**
     * Computes the maximum width for this region.
     * Returns Double.MAX_VALUE by default.
     * Region subclasses may override this method to return an different
     * value based on their content and layout strategy.  If the subclass
     * doesn't have a VERTICAL content bias, then the height parameter can be
     * ignored.
     *
     * @return the computed maximum width for this region
     */
    protected double computeMaxWidth(double height) {
        return Double.MAX_VALUE;
    }

    /**
     * Computes the maximum height of this region.
     * Returns Double.MAX_VALUE by default.
     * Region subclasses may override this method to return a different
     * value based on their content and layout strategy.  If the subclass
     * doesn't have a HORIZONTAL content bias, then the width parameter can be
     * ignored.
     *
     * @return the computed maximum height for this region
     */
    protected double computeMaxHeight(double width) {
        return Double.MAX_VALUE;
    }


    private Bounds boundingBox;

    /**
     * The layout bounds of this region: {@code 0, 0  width x height}
     */
    @Override
    protected final Bounds impl_computeLayoutBounds() {
        if (boundingBox == null)
            // we reuse the bounding box if the width and height haven't changed.
            boundingBox = new BoundingBox(0, 0, 0, getWidth(), getHeight(), 0);
        return boundingBox;
    }

    /**
     * If this region's snapToPixel property is false, this method returns the
     * same value, else it tries to return a value rounded to the nearest
     * pixel, but since there is no indication if the value is a vertical
     * or horizontal measurement then it may be snapped to the wrong pixel
     * size metric on screens with different horizontal and vertical scales.
     * @param value the space value to be snapped
     * @return value rounded to nearest pixel
     * @deprecated replaced by {@code snapSpaceX()} and {@code snapSpaceY()}
     */
    //@Deprecated(since="9") GWT doesn't recognize since attribute
    protected double snapSpace(double value) {
        return snapSpaceX(value, isSnapToPixel());
    }

    /**
     * If this region's snapToPixel property is true, returns a value rounded
     * to the nearest pixel in the horizontal direction, else returns the
     * same value.
     * @param value the space value to be snapped
     * @return value rounded to nearest pixel
     * @since 9
     */
    public double snapSpaceX(double value) {
        return snapSpaceX(value, isSnapToPixel());
    }

    /**
     * If this region's snapToPixel property is true, returns a value rounded
     * to the nearest pixel in the vertical direction, else returns the
     * same value.
     * @param value the space value to be snapped
     * @return value rounded to nearest pixel
     * @since 9
     */
    public double snapSpaceY(double value) {
        return snapSpaceY(value, isSnapToPixel());
    }

    /**
     * If this region's snapToPixel property is false, this method returns the
     * same value, else it tries to return a value ceiled to the nearest
     * pixel, but since there is no indication if the value is a vertical
     * or horizontal measurement then it may be snapped to the wrong pixel
     * size metric on screens with different horizontal and vertical scales.
     * @param value the size value to be snapped
     * @return value ceiled to nearest pixel
     * @deprecated replaced by {@code snapSizeX()} and {@code snapSizeY()}
     */
    //@Deprecated(since="9") // GWT doesn't recognize since attribute
    protected double snapSize(double value) {
        return snapSizeX(value, isSnapToPixel());
    }

    /**
     * If this region's snapToPixel property is true, returns a value ceiled
     * to the nearest pixel in the horizontal direction, else returns the
     * same value.
     * @param value the size value to be snapped
     * @return value ceiled to nearest pixel
     * @since 9
     */
    public double snapSizeX(double value) {
        return snapSizeX(value, isSnapToPixel());
    }

    /**
     * If this region's snapToPixel property is true, returns a value ceiled
     * to the nearest pixel in the vertical direction, else returns the
     * same value.
     * @param value the size value to be snapped
     * @return value ceiled to nearest pixel
     * @since 9
     */
    public double snapSizeY(double value) {
        return snapSizeY(value, isSnapToPixel());
    }

    /**
     * If this region's snapToPixel property is false, this method returns the
     * same value, else it tries to return a value rounded to the nearest
     * pixel, but since there is no indication if the value is a vertical
     * or horizontal measurement then it may be snapped to the wrong pixel
     * size metric on screens with different horizontal and vertical scales.
     * @param value the position value to be snapped
     * @return value rounded to nearest pixel
     * @deprecated replaced by {@code snapPositionX()} and {@code snapPositionY()}
     */
    @Deprecated(/*since="9"*/) //GWT doesn't recognize since attribute
    protected double snapPosition(double value) {
        return snapPositionX(value, isSnapToPixel());
    }

    /**
     * If this region's snapToPixel property is true, returns a value rounded
     * to the nearest pixel in the horizontal direction, else returns the
     * same value.
     * @param value the position value to be snapped
     * @return value rounded to nearest pixel
     * @since 9
     */
    public double snapPositionX(double value) {
        return snapPositionX(value, isSnapToPixel());
    }

    /**
     * If this region's snapToPixel property is true, returns a value rounded
     * to the nearest pixel in the vertical direction, else returns the
     * same value.
     * @param value the position value to be snapped
     * @return value rounded to nearest pixel
     * @since 9
     */
    public double snapPositionY(double value) {
        return snapPositionY(value, isSnapToPixel());
    }

    double snapPortionX(double value) {
        return snapPortionX(value, isSnapToPixel());
    }
    double snapPortionY(double value) {
        return snapPortionY(value, isSnapToPixel());
    }


    /**
     * Utility method to get the top inset which includes padding and border
     * inset. Then snapped to whole pixels if isSnapToPixel() is true.
     *
     * @since JavaFX 8.0
     * @return Rounded up insets top
     */
    public final double snappedTopInset() {
        return snappedTopInset;
    }

    /**
     * Utility method to get the bottom inset which includes padding and border
     * inset. Then snapped to whole pixels if isSnapToPixel() is true.
     *
     * @since JavaFX 8.0
     * @return Rounded up insets bottom
     */
    public final double snappedBottomInset() {
        return snappedBottomInset;
    }

    /**
     * Utility method to get the left inset which includes padding and border
     * inset. Then snapped to whole pixels if isSnapToPixel() is true.
     *
     * @since JavaFX 8.0
     * @return Rounded up insets left
     */
    public final double snappedLeftInset() {
        return snappedLeftInset;
    }

    /**
     * Utility method to get the right inset which includes padding and border
     * inset. Then snapped to whole pixels if isSnapToPixel() is true.
     *
     * @since JavaFX 8.0
     * @return Rounded up insets right
     */
    public final double snappedRightInset() {
        return snappedRightInset;
    }

    double computeChildMinAreaWidth(Node child, Insets margin) {
        return computeChildMinAreaWidth(child, -1, margin, -1, false);
    }

    double computeChildMinAreaWidth(Node child, double baselineComplement, Insets margin, double height, boolean fillHeight) {
        final boolean snap = isSnapToPixel();
        double left = margin != null? snapSpaceX(margin.getLeft(), snap) : 0;
        double right = margin != null? snapSpaceX(margin.getRight(), snap) : 0;
        double alt = -1;
        if (height != -1 && child.isResizable() && child.getContentBias() == Orientation.VERTICAL) { // width depends on height
            double top = margin != null? snapSpaceY(margin.getTop(), snap) : 0;
            double bottom = (margin != null? snapSpaceY(margin.getBottom(), snap) : 0);
            double bo = child.getBaselineOffset();
            final double contentHeight = bo == BASELINE_OFFSET_SAME_AS_HEIGHT && baselineComplement != -1 ?
                    height - top - bottom - baselineComplement :
                    height - top - bottom;
            if (fillHeight) {
                alt = snapSizeY(boundedSize(
                        child.minHeight(-1), contentHeight,
                        child.maxHeight(-1)));
            } else {
                alt = snapSizeY(boundedSize(
                        child.minHeight(-1),
                        child.prefHeight(-1),
                        Math.min(child.maxHeight(-1), contentHeight)));
            }
        }
        return left + snapSizeX(child.minWidth(alt)) + right;
    }

    double computeChildMinAreaHeight(Node child, Insets margin) {
        return computeChildMinAreaHeight(child, -1, margin, -1);
    }

    double computeChildMinAreaHeight(Node child, double minBaselineComplement, Insets margin, double width) {
        final boolean snap = isSnapToPixel();
        double top =margin != null? snapSpaceY(margin.getTop(), snap) : 0;
        double bottom = margin != null? snapSpaceY(margin.getBottom(), snap) : 0;

        double alt = -1;
        if (child.isResizable() && child.getContentBias() == Orientation.HORIZONTAL) { // height depends on width
            double left = margin != null? snapSpaceX(margin.getLeft(), snap) : 0;
            double right = margin != null? snapSpaceX(margin.getRight(), snap) : 0;
            alt = snapSizeX(width != -1? boundedSize(child.minWidth(-1), width - left - right, child.maxWidth(-1)) :
                    child.maxWidth(-1));
        }

        // For explanation, see computeChildPrefAreaHeight
        if (minBaselineComplement != -1) {
            double baseline = child.getBaselineOffset();
            if (child.isResizable() && baseline == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                return top + snapSizeY(child.minHeight(alt)) + bottom
                        + minBaselineComplement;
            } else {
                return baseline + minBaselineComplement;
            }
        } else {
            return top + snapSizeY(child.minHeight(alt)) + bottom;
        }
    }

    double computeChildPrefAreaWidth(Node child, Insets margin) {
        return computeChildPrefAreaWidth(child, -1, margin, -1, false);
    }

    double computeChildPrefAreaWidth(Node child, double baselineComplement, Insets margin, double height, boolean fillHeight) {
        final boolean snap = isSnapToPixel();
        double left = margin != null? snapSpaceX(margin.getLeft(), snap) : 0;
        double right = margin != null? snapSpaceX(margin.getRight(), snap) : 0;
        double alt = -1;
        if (height != -1 && child.isResizable() && child.getContentBias() == Orientation.VERTICAL) { // width depends on height
            double top = margin != null? snapSpaceY(margin.getTop(), snap) : 0;
            double bottom = margin != null? snapSpaceY(margin.getBottom(), snap) : 0;
            double bo = child.getBaselineOffset();
            final double contentHeight = bo == BASELINE_OFFSET_SAME_AS_HEIGHT && baselineComplement != -1 ?
                    height - top - bottom - baselineComplement :
                    height - top - bottom;
            if (fillHeight) {
                alt = snapSizeY(boundedSize(
                        child.minHeight(-1), contentHeight,
                        child.maxHeight(-1)));
            } else {
                alt = snapSizeY(boundedSize(
                        child.minHeight(-1),
                        child.prefHeight(-1),
                        Math.min(child.maxHeight(-1), contentHeight)));
            }
        }
        return left + snapSizeX(boundedSize(child.minWidth(alt), child.prefWidth(alt), child.maxWidth(alt))) + right;
    }

    double computeChildPrefAreaHeight(Node child, Insets margin) {
        return computeChildPrefAreaHeight(child, -1, margin, -1);
    }

    double computeChildPrefAreaHeight(Node child, double prefBaselineComplement, Insets margin, double width) {
        final boolean snap = isSnapToPixel();
        double top = margin != null? snapSpaceY(margin.getTop(), snap) : 0;
        double bottom = margin != null? snapSpaceY(margin.getBottom(), snap) : 0;

        double alt = -1;
        if (child.isResizable() && child.getContentBias() == Orientation.HORIZONTAL) { // height depends on width
            double left = margin != null ? snapSpaceX(margin.getLeft(), snap) : 0;
            double right = margin != null ? snapSpaceX(margin.getRight(), snap) : 0;
            alt = snapSizeX(boundedSize(
                    child.minWidth(-1), width != -1 ? width - left - right
                            : child.prefWidth(-1), child.maxWidth(-1)));
        }

        if (prefBaselineComplement != -1) {
            double baseline = child.getBaselineOffset();
            if (child.isResizable() && baseline == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                // When baseline is same as height, the preferred height of the node will be above the baseline, so we need to add
                // the preferred complement to it
                return top + snapSizeY(boundedSize(child.minHeight(alt), child.prefHeight(alt), child.maxHeight(alt))) + bottom
                        + prefBaselineComplement;
            } else {
                // For all other Nodes, it's just their baseline and the complement.
                // Note that the complement already contain the Node's preferred (or fixed) height
                return top + baseline + prefBaselineComplement + bottom;
            }
        } else {
            return top + snapSizeY(boundedSize(child.minHeight(alt), child.prefHeight(alt), child.maxHeight(alt))) + bottom;
        }
    }

    double computeChildMaxAreaWidth(Node child, double baselineComplement, Insets margin, double height, boolean fillHeight) {
        double max = child.maxWidth(-1);
        if (max == Double.MAX_VALUE) {
            return max;
        }
        final boolean snap = isSnapToPixel();
        double left = margin != null? snapSpaceX(margin.getLeft(), snap) : 0;
        double right = margin != null? snapSpaceX(margin.getRight(), snap) : 0;
        double alt = -1;
        if (height != -1 && child.isResizable() && child.getContentBias() == Orientation.VERTICAL) { // width depends on height
            double top = margin != null? snapSpaceY(margin.getTop(), snap) : 0;
            double bottom = (margin != null? snapSpaceY(margin.getBottom(), snap) : 0);
            double bo = child.getBaselineOffset();
            final double contentHeight = bo == BASELINE_OFFSET_SAME_AS_HEIGHT && baselineComplement != -1 ?
                    height - top - bottom - baselineComplement :
                    height - top - bottom;
            if (fillHeight) {
                alt = snapSizeY(boundedSize(
                        child.minHeight(-1), contentHeight,
                        child.maxHeight(-1)));
            } else {
                alt = snapSizeY(boundedSize(
                        child.minHeight(-1),
                        child.prefHeight(-1),
                        Math.min(child.maxHeight(-1), contentHeight)));
            }
            max = child.maxWidth(alt);
        }
        // if min > max, min wins, so still need to call boundedSize()
        return left + snapSizeX(boundedSize(child.minWidth(alt), max, Double.MAX_VALUE)) + right;
    }

    double computeChildMaxAreaHeight(Node child, double maxBaselineComplement, Insets margin, double width) {
        double max = child.maxHeight(-1);
        if (max == Double.MAX_VALUE) {
            return max;
        }

        final boolean snap = isSnapToPixel();
        double top = margin != null? snapSpaceY(margin.getTop(), snap) : 0;
        double bottom = margin != null? snapSpaceY(margin.getBottom(), snap) : 0;
        double alt = -1;
        if (child.isResizable() && child.getContentBias() == Orientation.HORIZONTAL) { // height depends on width
            double left = margin != null? snapSpaceX(margin.getLeft(), snap) : 0;
            double right = margin != null? snapSpaceX(margin.getRight(), snap) : 0;
            alt = snapSizeX(width != -1? boundedSize(child.minWidth(-1), width - left - right, child.maxWidth(-1)) :
                    child.minWidth(-1));
            max = child.maxHeight(alt);
        }
        // For explanation, see computeChildPrefAreaHeight
        if (maxBaselineComplement != -1) {
            double baseline = child.getBaselineOffset();
            if (child.isResizable() && baseline == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                return top + snapSizeY(boundedSize(child.minHeight(alt), child.maxHeight(alt), Double.MAX_VALUE)) + bottom
                        + maxBaselineComplement;
            } else {
                return top + baseline + maxBaselineComplement + bottom;
            }
        } else {
            // if min > max, min wins, so still need to call boundedSize()
            return top + snapSizeY(boundedSize(child.minHeight(alt), max, Double.MAX_VALUE)) + bottom;
        }
    }

    /* Max of children's minimum area widths */

    double computeMaxMinAreaWidth(List<Node> children, Callback<Node, Insets> margins) {
        return getMaxAreaWidth(children, margins, new double[] { -1 }, false, true);
    }

    double computeMaxMinAreaWidth(List<Node> children, Callback<Node, Insets> margins, double height, boolean fillHeight) {
        return getMaxAreaWidth(children, margins, new double[] { height }, fillHeight, true);
    }

    double computeMaxMinAreaWidth(List<Node> children, Callback<Node, Insets> childMargins, double childHeights[], boolean fillHeight) {
        return getMaxAreaWidth(children, childMargins, childHeights, fillHeight, true);
    }

    /* Max of children's minimum area heights */

    double computeMaxMinAreaHeight(List<Node>children, Callback<Node, Insets> margins, VPos valignment) {
        return getMaxAreaHeight(children, margins, null, valignment, true);
    }

    double computeMaxMinAreaHeight(List<Node>children, Callback<Node, Insets> margins, VPos valignment, double width) {
        return getMaxAreaHeight(children, margins, new double[] { width }, valignment, true);
    }

    double computeMaxMinAreaHeight(List<Node>children, Callback<Node, Insets> childMargins, double childWidths[], VPos valignment) {
        return getMaxAreaHeight(children, childMargins, childWidths, valignment, true);
    }

    /* Max of children's pref area widths */

    double computeMaxPrefAreaWidth(List<Node>children, Callback<Node, Insets> margins) {
        return getMaxAreaWidth(children, margins, new double[] { -1 }, false, false);
    }

    double computeMaxPrefAreaWidth(List<Node>children, Callback<Node, Insets> margins, double height, boolean fillHeight) {
        return getMaxAreaWidth(children, margins, new double[] { height }, fillHeight, false);
    }

    double computeMaxPrefAreaWidth(List<Node>children, Callback<Node, Insets> childMargins, double childHeights[], boolean fillHeight) {
        return getMaxAreaWidth(children, childMargins, childHeights, fillHeight, false);
    }

    /* Max of children's pref area heights */

    double computeMaxPrefAreaHeight(List<Node>children, Callback<Node, Insets> margins, VPos valignment) {
        return getMaxAreaHeight(children, margins, null, valignment, false);
    }

    double computeMaxPrefAreaHeight(List<Node>children, Callback<Node, Insets> margins, double width, VPos valignment) {
        return getMaxAreaHeight(children, margins, new double[] { width }, valignment, false);
    }

    double computeMaxPrefAreaHeight(List<Node>children, Callback<Node, Insets> childMargins, double childWidths[], VPos valignment) {
        return getMaxAreaHeight(children, childMargins, childWidths, valignment, false);
    }

    /* utility method for computing the max of children's min or pref heights, taking into account baseline alignment */
    private double getMaxAreaHeight(List<Node> children, Callback<Node,Insets> childMargins,  double childWidths[], VPos valignment, boolean minimum) {
        double singleChildWidth = childWidths == null ? -1 : childWidths.length == 1 ? childWidths[0] : Double.NaN;
        if (valignment == VPos.BASELINE) {
            double maxAbove = 0;
            double maxBelow = 0;
            for (int i = 0, maxPos = children.size(); i < maxPos; i++) {
                Node child = children.get(i);
                double childWidth = Double.isNaN(singleChildWidth) ? childWidths[i] : singleChildWidth;
                Insets margin = childMargins.call(child);
                double top = margin != null? snapSpace(margin.getTop()) : 0;
                double bottom = margin != null? snapSpace(margin.getBottom()) : 0;
                double baseline = child.getBaselineOffset();

                double childHeight = minimum? snapSize(child.minHeight(childWidth)) : snapSize(child.prefHeight(childWidth));
                if (baseline == BASELINE_OFFSET_SAME_AS_HEIGHT)
                    maxAbove = Math.max(maxAbove, childHeight + top);
                else {
                    maxAbove = Math.max(maxAbove, baseline + top);
                    maxBelow = Math.max(maxBelow,
                            snapSpace(minimum?snapSize(child.minHeight(childWidth)) : snapSize(child.prefHeight(childWidth))) -
                                    baseline + bottom);
                }
            }
            return maxAbove + maxBelow; //remind(aim): ceil this value?
        }
        double max = 0;
        for (int i = 0, maxPos = children.size(); i < maxPos; i++) {
            Node child = children.get(i);
            Insets margin = childMargins.call(child);
            double childWidth = Double.isNaN(singleChildWidth) ? childWidths[i] : singleChildWidth;
            max = Math.max(max, minimum?
                    computeChildMinAreaHeight(child, -1, margin, childWidth) :
                    computeChildPrefAreaHeight(child, -1, margin, childWidth));
        }
        return max;
    }

    /* utility method for computing the max of children's min or pref width, horizontal alignment is ignored for now */
    private double getMaxAreaWidth(List<Node> children, Callback<Node, Insets> childMargins, double childHeights[], boolean fillHeight, boolean minimum) {
        double singleChildHeight = childHeights == null ? -1 : childHeights.length == 1 ? childHeights[0] : Double.NaN;

        double max = 0;
        for (int i = 0, maxPos = children.size(); i < maxPos; i++) {
            Node child = children.get(i);
            Insets margin = childMargins.call(child);
            double childHeight = Double.isNaN(singleChildHeight) ? childHeights[i] : singleChildHeight;
            max = Math.max(max, minimum?
                    computeChildMinAreaWidth(children.get(i), -1, margin, childHeight, fillHeight) :
                    computeChildPrefAreaWidth(child, -1, margin, childHeight, fillHeight));
        }
        return max;
    }

    double getAreaBaselineOffset(List<Node> children, Callback<Node, Insets> margins,
                                 Function<Integer, Double> positionToWidth,
                                 double areaHeight, boolean fillHeight) {
        return getAreaBaselineOffset(children, margins, positionToWidth, areaHeight, fillHeight, isSnapToPixel());
    }

    private static double getAreaBaselineOffset(List<Node> children, Callback<Node, Insets> margins,
                                        Function<Integer, Double> positionToWidth,
                                        double areaHeight, boolean fillHeight, boolean snapToPixel) {
        return getAreaBaselineOffset(children, margins, positionToWidth, areaHeight, fillHeight,
                getMinBaselineComplement(children), snapToPixel);
    }

    double getAreaBaselineOffset(List<Node> children, Callback<Node, Insets> margins,
                                 Function<Integer, Double> positionToWidth,
                                 double areaHeight, boolean fillHeight, double minComplement) {
        return getAreaBaselineOffset(children, margins, positionToWidth, areaHeight, fillHeight, minComplement, isSnapToPixel());
    }

    private static double getAreaBaselineOffset(List<Node> children, Callback<Node, Insets> margins,
                                        Function<Integer, Double> positionToWidth,
                                        double areaHeight, boolean fillHeight, double minComplement, boolean snapToPixel) {
        return getAreaBaselineOffset(children, margins, positionToWidth, areaHeight, t -> fillHeight, minComplement, snapToPixel);
    }

    double getAreaBaselineOffset(List<Node> children, Callback<Node, Insets> margins,
                                 Function<Integer, Double> positionToWidth,
                                 double areaHeight, Function<Integer, Boolean> fillHeight, double minComplement) {
        return getAreaBaselineOffset(children, margins, positionToWidth, areaHeight, fillHeight, minComplement, isSnapToPixel());
    }

    /**
     * Returns the baseline offset of provided children, with respect to the minimum complement, computed
     * by {@link #getMinBaselineComplement(java.util.List)} from the same set of children.
     * @param children the children with baseline alignment
     * @param margins their margins (callback)
     * @param positionToWidth callback for children widths (can return -1 if no bias is used)
     * @param areaHeight height of the area to layout in
     * @param fillHeight callback to specify children that has fillHeight constraint
     * @param minComplement minimum complement
     */
    private static double getAreaBaselineOffset(List<Node> children, Callback<Node, Insets> margins,
                                        Function<Integer, Double> positionToWidth,
                                        double areaHeight, Function<Integer, Boolean> fillHeight, double minComplement, boolean snapToPixel) {
        double b = 0;
        double snapScaleV = 0.0;
        for (int i = 0;i < children.size(); ++i) {
            Node n = children.get(i);
            // Note: all children should be coming from the same parent so they should all have the same snapScale
            if (snapToPixel && i == 0) snapScaleV = getSnapScaleY(n.getParent());
            Insets margin = margins.call(n);
            double top = margin != null ? snapSpace(margin.getTop(), snapToPixel, snapScaleV) : 0;
            double bottom = (margin != null ? snapSpace(margin.getBottom(), snapToPixel, snapScaleV) : 0);
            final double bo = n.getBaselineOffset();
            if (bo == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                double alt = -1;
                if (n.getContentBias() == Orientation.HORIZONTAL) {
                    alt = positionToWidth.apply(i);
                }
                if (fillHeight.apply(i)) {
                    // If the children fills it's height, than it's "preferred" height is the area without the complement and insets
                    b = Math.max(b, top + boundedSize(n.minHeight(alt), areaHeight - minComplement - top - bottom,
                            n.maxHeight(alt)));
                } else {
                    // Otherwise, we must use the area without complement and insets as a maximum for the Node
                    b = Math.max(b, top + boundedSize(n.minHeight(alt), n.prefHeight(alt),
                            Math.min(n.maxHeight(alt), areaHeight - minComplement - top - bottom)));
                }
            } else {
                b = Math.max(b, top + bo);
            }
        }
        return b;
    }

    /**
     * Utility method which positions the child within an area of this
     * region defined by {@code areaX}, {@code areaY}, {@code areaWidth} x {@code areaHeight},
     * with a baseline offset relative to that area.
     * <p>
     * This function does <i>not</i> resize the node and uses the node's layout bounds
     * width and height to determine how it should be positioned within the area.
     * <p>
     * If the vertical alignment is {@code VPos.BASELINE} then it
     * will position the node so that its own baseline aligns with the passed in
     * {@code baselineOffset},  otherwise the baseline parameter is ignored.
     * <p>
     * If {@code snapToPixel} is {@code true} for this region, then the x/y position
     * values will be rounded to their nearest pixel boundaries.
     *
     * @param child the child being positioned within this region
     * @param areaX the horizontal offset of the layout area relative to this region
     * @param areaY the vertical offset of the layout area relative to this region
     * @param areaWidth  the width of the layout area
     * @param areaHeight the height of the layout area
     * @param areaBaselineOffset the baseline offset to be used if VPos is BASELINE
     * @param halignment the horizontal alignment for the child within the area
     * @param valignment the vertical alignment for the child within the area
     *
     */
    protected void positionInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight,
                                  double areaBaselineOffset, HPos halignment, VPos valignment) {
        positionInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset,
                Insets.EMPTY, halignment, valignment, isSnapToPixel());
    }

    /**
     * Utility method which positions the child within an area of this
     * region defined by {@code areaX}, {@code areaY}, {@code areaWidth} x {@code areaHeight},
     * with a baseline offset relative to that area.
     * <p>
     * This function does <i>not</i> resize the node and uses the node's layout bounds
     * width and height to determine how it should be positioned within the area.
     * <p>
     * If the vertical alignment is {@code VPos.BASELINE} then it
     * will position the node so that its own baseline aligns with the passed in
     * {@code baselineOffset},  otherwise the baseline parameter is ignored.
     * <p>
     * If {@code snapToPixel} is {@code true} for this region, then the x/y position
     * values will be rounded to their nearest pixel boundaries.
     * <p>
     * If {@code margin} is non-null, then that space will be allocated around the
     * child within the layout area.  margin may be null.
     *
     * @param child the child being positioned within this region
     * @param areaX the horizontal offset of the layout area relative to this region
     * @param areaY the vertical offset of the layout area relative to this region
     * @param areaWidth  the width of the layout area
     * @param areaHeight the height of the layout area
     * @param areaBaselineOffset the baseline offset to be used if VPos is BASELINE
     * @param margin the margin of space to be allocated around the child
     * @param halignment the horizontal alignment for the child within the area
     * @param valignment the vertical alignment for the child within the area
     *
     * @since JavaFX 8.0
     */
    public static void positionInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight,
                                      double areaBaselineOffset, Insets margin, HPos halignment, VPos valignment, boolean isSnapToPixel) {
        Insets childMargin = margin != null? margin : Insets.EMPTY;
        double snapScaleX = isSnapToPixel ? getSnapScaleX(child) : 1.0;
        double snapScaleY = isSnapToPixel ? getSnapScaleY(child) : 1.0;

        position(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset,
                snapSpace(childMargin.getTop(), isSnapToPixel, snapScaleY),
                snapSpace(childMargin.getRight(), isSnapToPixel, snapScaleX),
                snapSpace(childMargin.getBottom(), isSnapToPixel, snapScaleY),
                snapSpace(childMargin.getLeft(), isSnapToPixel, snapScaleX),
                halignment, valignment, isSnapToPixel);
    }

    /**
     * Utility method which lays out the child within an area of this
     * region defined by {@code areaX}, {@code areaY}, {@code areaWidth} x {@code areaHeight},
     * with a baseline offset relative to that area.
     * <p>
     * If the child is resizable, this method will resize it to fill the specified
     * area unless the node's maximum size prevents it.  If the node's maximum
     * size preference is less than the area size, the maximum size will be used.
     * If node's maximum is greater than the area size, then the node will be
     * resized to fit within the area, unless its minimum size prevents it.
     * <p>
     * If the child has a non-null contentBias, then this method will use it when
     * resizing the child.  If the contentBias is horizontal, it will set its width
     * first to the area's width (up to the child's max width limit) and then pass
     * that value to compute the child's height.  If child's contentBias is vertical,
     * then it will set its height to the area height (up to child's max height limit)
     * and pass that height to compute the child's width.  If the child's contentBias
     * is null, then it's width and height have no dependencies on each other.
     * <p>
     * If the child is not resizable (Shape, Group, etc) then it will only be
     * positioned and not resized.
     * <p>
     * If the child's resulting size differs from the area's size (either
     * because it was not resizable or it's sizing preferences prevented it), then
     * this function will align the node relative to the area using horizontal and
     * vertical alignment values.
     * If valignment is {@code VPos.BASELINE} then the node's baseline will be aligned
     * with the area baseline offset parameter, otherwise the baseline parameter
     * is ignored.
     * <p>
     * If {@code snapToPixel} is {@code true} for this region, then the resulting x,y
     * values will be rounded to their nearest pixel boundaries and the
     * width/height values will be ceiled to the next pixel boundary.
     *
     * @param child the child being positioned within this region
     * @param areaX the horizontal offset of the layout area relative to this region
     * @param areaY the vertical offset of the layout area relative to this region
     * @param areaWidth  the width of the layout area
     * @param areaHeight the height of the layout area
     * @param areaBaselineOffset the baseline offset to be used if VPos is BASELINE
     * @param halignment the horizontal alignment for the child within the area
     * @param valignment the vertical alignment for the child within the area
     *
     */
    protected void layoutInArea(Node child, double areaX, double areaY,
                                double areaWidth, double areaHeight,
                                double areaBaselineOffset,
                                HPos halignment, VPos valignment) {
        layoutInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset,
                Insets.EMPTY, halignment, valignment);
    }

    /**
     * Utility method which lays out the child within an area of this
     * region defined by {@code areaX}, {@code areaY}, {@code areaWidth} x {@code areaHeight},
     * with a baseline offset relative to that area.
     * <p>
     * If the child is resizable, this method will resize it to fill the specified
     * area unless the node's maximum size prevents it.  If the node's maximum
     * size preference is less than the area size, the maximum size will be used.
     * If node's maximum is greater than the area size, then the node will be
     * resized to fit within the area, unless its minimum size prevents it.
     * <p>
     * If the child has a non-null contentBias, then this method will use it when
     * resizing the child.  If the contentBias is horizontal, it will set its width
     * first to the area's width (up to the child's max width limit) and then pass
     * that value to compute the child's height.  If child's contentBias is vertical,
     * then it will set its height to the area height (up to child's max height limit)
     * and pass that height to compute the child's width.  If the child's contentBias
     * is null, then it's width and height have no dependencies on each other.
     * <p>
     * If the child is not resizable (Shape, Group, etc) then it will only be
     * positioned and not resized.
     * <p>
     * If the child's resulting size differs from the area's size (either
     * because it was not resizable or it's sizing preferences prevented it), then
     * this function will align the node relative to the area using horizontal and
     * vertical alignment values.
     * If valignment is {@code VPos.BASELINE} then the node's baseline will be aligned
     * with the area baseline offset parameter, otherwise the baseline parameter
     * is ignored.
     * <p>
     * If {@code margin} is non-null, then that space will be allocated around the
     * child within the layout area.  margin may be null.
     * <p>
     * If {@code snapToPixel} is {@code true} for this region, then the resulting x,y
     * values will be rounded to their nearest pixel boundaries and the
     * width/height values will be ceiled to the next pixel boundary.
     *
     * @param child the child being positioned within this region
     * @param areaX the horizontal offset of the layout area relative to this region
     * @param areaY the vertical offset of the layout area relative to this region
     * @param areaWidth  the width of the layout area
     * @param areaHeight the height of the layout area
     * @param areaBaselineOffset the baseline offset to be used if VPos is BASELINE
     * @param margin the margin of space to be allocated around the child
     * @param halignment the horizontal alignment for the child within the area
     * @param valignment the vertical alignment for the child within the area
     */
    protected void layoutInArea(Node child, double areaX, double areaY,
                                double areaWidth, double areaHeight,
                                double areaBaselineOffset,
                                Insets margin,
                                HPos halignment, VPos valignment) {
        layoutInArea(child, areaX, areaY, areaWidth, areaHeight,
                areaBaselineOffset, margin, true, true, halignment, valignment);
    }

    /**
     * Utility method which lays out the child within an area of this
     * region defined by {@code areaX}, {@code areaY}, {@code areaWidth} x {@code areaHeight},
     * with a baseline offset relative to that area.
     * <p>
     * If the child is resizable, this method will use {@code fillWidth} and {@code fillHeight}
     * to determine whether to resize it to fill the area or keep the child at its
     * preferred dimension.  If fillWidth/fillHeight are true, then this method
     * will only resize the child up to its max size limits.  If the node's maximum
     * size preference is less than the area size, the maximum size will be used.
     * If node's maximum is greater than the area size, then the node will be
     * resized to fit within the area, unless its minimum size prevents it.
     * <p>
     * If the child has a non-null contentBias, then this method will use it when
     * resizing the child.  If the contentBias is horizontal, it will set its width
     * first and then pass that value to compute the child's height.  If child's
     * contentBias is vertical, then it will set its height first
     * and pass that value to compute the child's width.  If the child's contentBias
     * is null, then it's width and height have no dependencies on each other.
     * <p>
     * If the child is not resizable (Shape, Group, etc) then it will only be
     * positioned and not resized.
     * <p>
     * If the child's resulting size differs from the area's size (either
     * because it was not resizable or it's sizing preferences prevented it), then
     * this function will align the node relative to the area using horizontal and
     * vertical alignment values.
     * If valignment is {@code VPos.BASELINE} then the node's baseline will be aligned
     * with the area baseline offset parameter, otherwise the baseline parameter
     * is ignored.
     * <p>
     * If {@code margin} is non-null, then that space will be allocated around the
     * child within the layout area.  margin may be null.
     * <p>
     * If {@code snapToPixel} is {@code true} for this region, then the resulting x,y
     * values will be rounded to their nearest pixel boundaries and the
     * width/height values will be ceiled to the next pixel boundary.
     *
     * @param child the child being positioned within this region
     * @param areaX the horizontal offset of the layout area relative to this region
     * @param areaY the vertical offset of the layout area relative to this region
     * @param areaWidth  the width of the layout area
     * @param areaHeight the height of the layout area
     * @param areaBaselineOffset the baseline offset to be used if VPos is BASELINE
     * @param margin the margin of space to be allocated around the child
     * @param fillWidth whether or not the child should be resized to fill the area width or kept to its preferred width
     * @param fillHeight whether or not the child should e resized to fill the area height or kept to its preferred height
     * @param halignment the horizontal alignment for the child within the area
     * @param valignment the vertical alignment for the child within the area
     */
    protected void layoutInArea(Node child, double areaX, double areaY,
                                double areaWidth, double areaHeight,
                                double areaBaselineOffset,
                                Insets margin, boolean fillWidth, boolean fillHeight,
                                HPos halignment, VPos valignment) {
        layoutInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, margin, fillWidth, fillHeight, halignment, valignment, isSnapToPixel());
    }

    /**
     * Utility method which lays out the child within an area of it's
     * parent defined by {@code areaX}, {@code areaY}, {@code areaWidth} x {@code areaHeight},
     * with a baseline offset relative to that area.
     * <p>
     * If the child is resizable, this method will use {@code fillWidth} and {@code fillHeight}
     * to determine whether to resize it to fill the area or keep the child at its
     * preferred dimension.  If fillWidth/fillHeight are true, then this method
     * will only resize the child up to its max size limits.  If the node's maximum
     * size preference is less than the area size, the maximum size will be used.
     * If node's maximum is greater than the area size, then the node will be
     * resized to fit within the area, unless its minimum size prevents it.
     * <p>
     * If the child has a non-null contentBias, then this method will use it when
     * resizing the child.  If the contentBias is horizontal, it will set its width
     * first and then pass that value to compute the child's height.  If child's
     * contentBias is vertical, then it will set its height first
     * and pass that value to compute the child's width.  If the child's contentBias
     * is null, then it's width and height have no dependencies on each other.
     * <p>
     * If the child is not resizable (Shape, Group, etc) then it will only be
     * positioned and not resized.
     * <p>
     * If the child's resulting size differs from the area's size (either
     * because it was not resizable or it's sizing preferences prevented it), then
     * this function will align the node relative to the area using horizontal and
     * vertical alignment values.
     * If valignment is {@code VPos.BASELINE} then the node's baseline will be aligned
     * with the area baseline offset parameter, otherwise the baseline parameter
     * is ignored.
     * <p>
     * If {@code margin} is non-null, then that space will be allocated around the
     * child within the layout area.  margin may be null.
     * <p>
     * If {@code snapToPixel} is {@code true} for this region, then the resulting x,y
     * values will be rounded to their nearest pixel boundaries and the
     * width/height values will be ceiled to the next pixel boundary.
     *
     * @param child the child being positioned within this region
     * @param areaX the horizontal offset of the layout area relative to this region
     * @param areaY the vertical offset of the layout area relative to this region
     * @param areaWidth  the width of the layout area
     * @param areaHeight the height of the layout area
     * @param areaBaselineOffset the baseline offset to be used if VPos is BASELINE
     * @param margin the margin of space to be allocated around the child
     * @param fillWidth whether or not the child should be resized to fill the area width or kept to its preferred width
     * @param fillHeight whether or not the child should e resized to fill the area height or kept to its preferred height
     * @param halignment the horizontal alignment for the child within the area
     * @param valignment the vertical alignment for the child within the area
     * @param isSnapToPixel whether to snap size and position to pixels
     * @since JavaFX 8.0
     */
    public static void layoutInArea(Node child, double areaX, double areaY,
                                    double areaWidth, double areaHeight,
                                    double areaBaselineOffset,
                                    Insets margin, boolean fillWidth, boolean fillHeight,
                                    HPos halignment, VPos valignment, boolean isSnapToPixel) {

        Insets childMargin = margin != null ? margin : Insets.EMPTY;
        double snapScaleX = isSnapToPixel ? getSnapScaleX(child) : 1.0;
        double snapScaleY = isSnapToPixel ? getSnapScaleY(child) : 1.0;

        double top = snapSpace(childMargin.getTop(), isSnapToPixel, snapScaleY);
        double bottom = snapSpace(childMargin.getBottom(), isSnapToPixel, snapScaleY);
        double left = snapSpace(childMargin.getLeft(), isSnapToPixel, snapScaleX);
        double right = snapSpace(childMargin.getRight(), isSnapToPixel, snapScaleX);

        if (valignment == VPos.BASELINE) {
            double bo = child.getBaselineOffset();
            if (bo == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                if (child.isResizable()) {
                    // Everything below the baseline is like an "inset". The Node with BASELINE_OFFSET_SAME_AS_HEIGHT cannot
                    // be resized to this area
                    bottom += snapSpace(areaHeight - areaBaselineOffset, isSnapToPixel, snapScaleY);
                } else {
                    top = snapSpace(areaBaselineOffset - child.getLayoutBounds().getHeight(), isSnapToPixel, snapScaleY);
                }
            } else {
                top = snapSpace(areaBaselineOffset - bo, isSnapToPixel, snapScaleY);
            }
        }


        if (child.isResizable()) {
            Vec2d size = boundedNodeSizeWithBias(child, areaWidth - left - right, areaHeight - top - bottom,
                    fillWidth, fillHeight, TEMP_VEC2D);
            child.resize(snapSize(size.x, isSnapToPixel, snapScaleX),
                    snapSize(size.y, isSnapToPixel, snapScaleX));
        }
        position(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset,
                top, right, bottom, left, halignment, valignment, isSnapToPixel);
    }

    private static void position(Node child, double areaX, double areaY, double areaWidth, double areaHeight,
                                 double areaBaselineOffset,
                                 double topMargin, double rightMargin, double bottomMargin, double leftMargin,
                                 HPos hpos, VPos vpos, boolean isSnapToPixel) {
        final double xoffset = leftMargin + computeXOffset(areaWidth - leftMargin - rightMargin,
                child.getLayoutBounds().getWidth(), hpos);
        final double yoffset;
        if (vpos == VPos.BASELINE) {
            double bo = child.getBaselineOffset();
            if (bo == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                // We already know the layout bounds at this stage, so we can use them
                yoffset = areaBaselineOffset - child.getLayoutBounds().getHeight();
            } else {
                yoffset = areaBaselineOffset - bo;
            }
        } else {
            yoffset = topMargin + computeYOffset(areaHeight - topMargin - bottomMargin,
                    child.getLayoutBounds().getHeight(), vpos);
        }
        double x = areaX + xoffset;
        double y = areaY + yoffset;
        if (isSnapToPixel) {
            x = snapPosition(x, true, getSnapScaleX(child));
            y = snapPosition(y, true, getSnapScaleY(child));
        }

        child.relocate(x,y);
    }

    static Vec2d TEMP_VEC2D = new Vec2d();

    /**
     * Returns the size of a Node that should be placed in an area of the specified size,
     * bounded in it's min/max size, respecting bias.
     *
     * @param node the node
     * @param areaWidth the width of the bounding area where the node is going to be placed
     * @param areaHeight the height of the bounding area where the node is going to be placed
     * @param fillWidth if Node should try to fill the area width
     * @param fillHeight if Node should try to fill the area height
     * @param result Vec2d object for the result or null if new one should be created
     * @return Vec2d object with width(x parameter) and height (y parameter)
     */
    static Vec2d boundedNodeSizeWithBias(Node node, double areaWidth, double areaHeight,
                                         boolean fillWidth, boolean fillHeight, Vec2d result) {
        if (result == null)
            result = new Vec2d();

        Orientation bias = node.getContentBias();

        double childWidth;
        double childHeight;

        if (bias == null) {
            childWidth = boundedSize(
                    node.minWidth(-1), fillWidth ? areaWidth
                            : Math.min(areaWidth, node.prefWidth(-1)),
                    node.maxWidth(-1));
            childHeight = boundedSize(
                    node.minHeight(-1), fillHeight ? areaHeight
                            : Math.min(areaHeight, node.prefHeight(-1)),
                    node.maxHeight(-1));

        } else if (bias == Orientation.HORIZONTAL) {
            childWidth = boundedSize(
                    node.minWidth(-1), fillWidth ? areaWidth
                            : Math.min(areaWidth, node.prefWidth(-1)),
                    node.maxWidth(-1));
            childHeight = boundedSize(
                    node.minHeight(childWidth), fillHeight ? areaHeight
                            : Math.min(areaHeight, node.prefHeight(childWidth)),
                    node.maxHeight(childWidth));

        } else { // bias == VERTICAL
            childHeight = boundedSize(
                    node.minHeight(-1), fillHeight ? areaHeight
                            : Math.min(areaHeight, node.prefHeight(-1)),
                    node.maxHeight(-1));
            childWidth = boundedSize(
                    node.minWidth(childHeight), fillWidth ? areaWidth
                            : Math.min(areaWidth, node.prefWidth(childHeight)),
                    node.maxWidth(childHeight));
        }

        result.set(childWidth, childHeight);
        return result;
    }


    /**
     * Return the minimum complement of baseline
     */
    static double getMinBaselineComplement(List<Node> children) {
        return getBaselineComplement(children, true, false);
    }

    /**
     * Return the preferred complement of baseline
     */
    static double getPrefBaselineComplement(List<Node> children) {
        return getBaselineComplement(children, false, false);
    }

    /**
     * Return the maximal complement of baseline
     */
    static double getMaxBaselineComplement(List<Node> children) {
        return getBaselineComplement(children, false, true);
    }

    private static double getBaselineComplement(List<Node> children, boolean min, boolean max) {
        double bc = 0;
        for (Node n : children) {
            double bo = n.getBaselineOffset();
            if (bo == BASELINE_OFFSET_SAME_AS_HEIGHT)
                continue;
            if (n.isResizable()) {
                bc = Math.max(bc, (min ? n.minHeight(-1) : max ? n.maxHeight(-1) : n.prefHeight(-1)) - bo);
            } else {
                bc = Math.max(bc, n.getLayoutBounds().getHeight() - bo);
            }
        }
        return bc;
    }
    static double computeXOffset(double width, double contentWidth, HPos hpos) {
        switch(hpos) {
            case LEFT:
                return 0;
            case CENTER:
                return (width - contentWidth) / 2;
            case RIGHT:
                return width - contentWidth;
            default:
                throw new AssertionError("Unhandled hPos");
        }
    }

    static double computeYOffset(double height, double contentHeight, VPos vpos) {
        switch(vpos) {
            case BASELINE:
            case TOP:
                return 0;
            case CENTER:
                return (height - contentHeight) / 2;
            case BOTTOM:
                return height - contentHeight;
            default:
                throw new AssertionError("Unhandled vPos");
        }
    }

    /***************************************************************************
     *                                                                         *
     * Static convenience methods for layout                                   *
     *                                                                         *
     **************************************************************************/

    /**
     * Computes the value based on the given min and max values. We encode in this
     * method the logic surrounding various edge cases, such as when the min is
     * specified as greater than the max, or the max less than the min, or a pref
     * value that exceeds either the max or min in their extremes.
     * <p/>
     * If the min is greater than the max, then we want to make sure the returned
     * value is the min. In other words, in such a case, the min becomes the only
     * acceptable return value.
     * <p/>
     * If the min and max values are well ordered, and the pref is less than the min
     * then the min is returned. Likewise, if the values are well ordered and the
     * pref is greater than the max, then the max is returned. If the pref lies
     * between the min and the max, then the pref is returned.
     *
     *
     * @param min The minimum bound
     * @param pref The value to be clamped between the min and max
     * @param max the maximum bound
     * @return the size bounded by min, pref, and max.
     */
    protected static double boundedSize(double min, double pref, double max) {
        double a = pref >= min ? pref : min;
        double b = min >= max ? min : max;
        return a <= b ? a : b;
    }

    double adjustWidthByMargin(double width, Insets margin) {
        if (margin == null || margin == Insets.EMPTY) {
            return width;
        }
        boolean isSnapToPixel = isSnapToPixel();
        return width - snapSpaceX(margin.getLeft(), isSnapToPixel) - snapSpaceX(margin.getRight(), isSnapToPixel);
    }

    double adjustHeightByMargin(double height, Insets margin) {
        if (margin == null || margin == Insets.EMPTY) {
            return height;
        }
        boolean isSnapToPixel = isSnapToPixel();
        return height - snapSpaceY(margin.getTop(), isSnapToPixel) - snapSpaceY(margin.getBottom(), isSnapToPixel);
    }

    static double[] createDoubleArray(int length, double value) {
        double[] array = new double[length];
        for (int i = 0; i < length; i++) {
            array[i] = value;
        }
        return array;
    }

    /**
     * Convenience method for overriding the region's computed minimum width and height.
     * This should only be called if the region's internally computed minimum size
     * doesn't meet the application's layout needs.
     *
     * @see #setMinWidth
     * @see #setMinHeight
     * @param minWidth  the override value for minimum width
     * @param minHeight the override value for minimum height
     */
    public void setMinSize(double minWidth, double minHeight) {
        setMinWidth(minWidth);
        setMinHeight(minHeight);
    }

    /**
     * Convenience method for overriding the region's computed preferred width and height.
     * This should only be called if the region's internally computed preferred size
     * doesn't meet the application's layout needs.
     *
     * @see #setPrefWidth
     * @see #setPrefHeight
     * @param prefWidth the override value for preferred width
     * @param prefHeight the override value for preferred height
     */
    public void setPrefSize(double prefWidth, double prefHeight) {
        setPrefWidth(prefWidth);
        setPrefHeight(prefHeight);
    }

    /**
     * Convenience method for overriding the region's computed maximum width and height.
     * This should only be called if the region's internally computed maximum size
     * doesn't meet the application's layout needs.
     *
     * @see #setMaxWidth
     * @see #setMaxHeight
     * @param maxWidth  the override value for maximum width
     * @param maxHeight the override value for maximum height
     */
    public void setMaxSize(double maxWidth, double maxHeight) {
        setMaxWidth(maxWidth);
        setMaxHeight(maxHeight);
    }


    private double getSnapScaleX() {
        return 1.0; // _getSnapScaleXimpl(getScene());
    }

    public static double getSnapScaleX(Node n) {
        return 1.0; // _getSnapScaleXimpl(n.getScene());
    }

    private double getSnapScaleY() {
        return 1.0; //_getSnapScaleYimpl(getScene());
    }

    public static double getSnapScaleY(Node n) {
        return 1.0; // _getSnapScaleYimpl(n.getScene());
    }

    private static double scaledRound(double value, double scale) {
        return Math.round(value * scale) / scale;
    }

    private static double scaledFloor(double value, double scale) {
        return Math.floor(value * scale) / scale;
    }

    private static double scaledCeil(double value, double scale) {
        return Math.ceil(value * scale) / scale;
    }

    /**
     * If snapToPixel is true, then the value is rounded using Math.round. Otherwise,
     * the value is simply returned. This method will surely be JIT'd under normal
     * circumstances, however on an interpreter it would be better to inline this
     * method. However the use of Math.round here, and Math.ceil in snapSize is
     * not obvious, and so for code maintenance this logic is pulled out into
     * a separate method.
     *
     * @param value The value that needs to be snapped
     * @param snapToPixel Whether to snap to pixel
     * @return value either as passed in or rounded based on snapToPixel
     */
    private double snapSpaceX(double value, boolean snapToPixel) {
        return snapToPixel ? scaledRound(value, getSnapScaleX()) : value;
    }

    private double snapSpaceY(double value, boolean snapToPixel) {
        return snapToPixel ? scaledRound(value, getSnapScaleY()) : value;
    }

    private static double snapSpace(double value, boolean snapToPixel, double snapScale) {
        return snapToPixel ? scaledRound(value, snapScale) : value;
    }

    /**
     * If snapToPixel is true, then the value is ceil'd using Math.ceil. Otherwise,
     * the value is simply returned.
     *
     * @param value The value that needs to be snapped
     * @param snapToPixel Whether to snap to pixel
     * @return value either as passed in or ceil'd based on snapToPixel
     */
    private double snapSizeX(double value, boolean snapToPixel) {
        return snapToPixel ? scaledCeil(value, getSnapScaleX()) : value;
    }
    private double snapSizeY(double value, boolean snapToPixel) {
        return snapToPixel ? scaledCeil(value, getSnapScaleY()) : value;
    }

    private static double snapSize(double value, boolean snapToPixel, double snapScale) {
        return snapToPixel ? scaledCeil(value, snapScale) : value;
    }

    /**
     * If snapToPixel is true, then the value is rounded using Math.round. Otherwise,
     * the value is simply returned.
     *
     * @param value The value that needs to be snapped
     * @param snapToPixel Whether to snap to pixel
     * @return value either as passed in or rounded based on snapToPixel
     */
    private double snapPositionX(double value, boolean snapToPixel) {
        return snapToPixel ? scaledRound(value, getSnapScaleX()) : value;
    }
    private double snapPositionY(double value, boolean snapToPixel) {
        return snapToPixel ? scaledRound(value, getSnapScaleY()) : value;
    }

    private static double snapPosition(double value, boolean snapToPixel, double snapScale) {
        return snapToPixel ? scaledRound(value, snapScale) : value;
    }

    private double snapPortionX(double value, boolean snapToPixel) {
        if (!snapToPixel || value == 0) return value;
        double s = getSnapScaleX();
        value *= s;
        if (value > 0) {
            value = Math.max(1, Math.floor(value));
        } else {
            value = Math.min(-1, Math.ceil(value));
        }
        return value / s;
    }
    private double snapPortionY(double value, boolean snapToPixel) {
        if (!snapToPixel || value == 0) return value;
        double s = getSnapScaleY();
        value *= s;
        if (value > 0) {
            value = Math.max(1, Math.floor(value));
        } else {
            value = Math.min(-1, Math.ceil(value));
        }
        return value / s;
    }

    static {
        JavaFxGraphicsRegistry.registerRegion();
    }
}
