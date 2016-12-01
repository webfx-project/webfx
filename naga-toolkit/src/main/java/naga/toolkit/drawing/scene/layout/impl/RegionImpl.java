package naga.toolkit.drawing.scene.layout.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Callback;
import naga.commons.util.function.Function;
import naga.toolkit.drawing.geom.BaseBounds;
import naga.toolkit.drawing.geom.TempState;
import naga.toolkit.drawing.geom.Vec2d;
import naga.toolkit.drawing.geom.transform.BaseTransform;
import naga.toolkit.drawing.geometry.*;
import naga.toolkit.drawing.geometry.Insets;
import naga.toolkit.drawing.scene.layout.Region;
import naga.toolkit.drawing.scene.Node;
import naga.toolkit.drawing.scene.impl.ParentImpl;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class RegionImpl extends ParentImpl implements Region {

    public RegionImpl() {
    }

    public RegionImpl(Node... nodes) {
        super(nodes);
    }

    private final Property<Double> widthProperty = new SimpleObjectProperty<Double>(0d) {
        @Override
        protected void invalidated() {
            widthOrHeightChanged();
        }
    };
    @Override
    public Property<Double> widthProperty() {
        return widthProperty;
    }

    private final Property<Double> heightProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> heightProperty() {
        return heightProperty;
    }

    private final Property<Double> maxWidthProperty = new SimpleObjectProperty<>(USE_COMPUTED_SIZE);
    @Override
    public Property<Double> maxWidthProperty() {
        return maxWidthProperty;
    }

    private final Property<Double> minWidthProperty = new SimpleObjectProperty<>(USE_COMPUTED_SIZE);
    @Override
    public Property<Double> minWidthProperty() {
        return minWidthProperty;
    }

    private final Property<Double> maxHeightProperty = new SimpleObjectProperty<>(USE_COMPUTED_SIZE);
    @Override
    public Property<Double> maxHeightProperty() {
        return maxHeightProperty;
    }

    private final Property<Double> minHeightProperty = new SimpleObjectProperty<>(USE_COMPUTED_SIZE);
    @Override
    public Property<Double> minHeightProperty() {
        return minHeightProperty;
    }

    private final Property<Double> prefWidthProperty = new SimpleObjectProperty<>(USE_COMPUTED_SIZE);
    @Override
    public Property<Double> prefWidthProperty() {
        return prefWidthProperty;
    }

    private final Property<Double> prefHeightProperty = new SimpleObjectProperty<>(USE_COMPUTED_SIZE);
    @Override
    public Property<Double> prefHeightProperty() {
        return prefHeightProperty;
    }

    private final Property<Insets> insetsProperty = new SimpleObjectProperty<>(Insets.EMPTY);
    @Override
    public Property<Insets> insetsProperty() {
        return insetsProperty;
    }

    private final Property<Boolean> snapToPixelProperty = new SimpleObjectProperty<Boolean>(true) {
        @Override
        protected void invalidated() {
            updateSnappedInsets();
            requestParentLayout();
        }
    };

    @Override
    public Property<Boolean> snapToPixelProperty() {
        return snapToPixelProperty;
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
            bounds = bounds.deriveWithNewBounds(bx1, by1, 0, bx2, by2, 0);
            bounds = tx.transform(bounds, bounds);
            return bounds;
        } else {
            // Union with children's bounds
            BaseBounds tempBounds = TempState.getInstance().bounds;
            tempBounds = tempBounds.deriveWithNewBounds(bx1, by1, 0, bx2, by2, 0);
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
     * If this region's snapToPixel property is true, returns a value rounded
     * to the nearest pixel, else returns the same value.
     * @param value the space value to be snapped
     * @return value rounded to nearest pixel
     */
    protected double snapSpace(double value) {
        return snapSpace(value, isSnapToPixel());
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
    private static double snapSpace(double value, boolean snapToPixel) {
        return snapToPixel ? Math.round(value) : value;
    }

    double snapPortion(double value) {
        return snapPortion(value, isSnapToPixel());
    }

    /**
     * If snapToPixel is true, then the value is ceil'd using Math.ceil. Otherwise,
     * the value is simply returned.
     *
     * @param value The value that needs to be snapped
     * @param snapToPixel Whether to snap to pixel
     * @return value either as passed in or ceil'd based on snapToPixel
     */
    private static double snapSize(double value, boolean snapToPixel) {
        return snapToPixel ? Math.ceil(value) : value;
    }

    /**
     * If this region's snapToPixel property is true, returns a value ceiled
     * to the nearest pixel, else returns the same value.
     * @param value the size value to be snapped
     * @return value ceiled to nearest pixel
     */
    protected double snapSize(double value) {
        return snapSize(value, isSnapToPixel());
    }

    /**
     * If snapToPixel is true, then the value is rounded using Math.round. Otherwise,
     * the value is simply returned.
     *
     * @param value The value that needs to be snapped
     * @param snapToPixel Whether to snap to pixel
     * @return value either as passed in or rounded based on snapToPixel
     */
    private static double snapPosition(double value, boolean snapToPixel) {
        return snapToPixel ? Math.round(value) : value;
    }

    private static double snapPortion(double value, boolean snapToPixel) {
        if (snapToPixel)
            return value == 0 ? 0 :(value > 0 ? Math.max(1, Math.floor(value)) : Math.min(-1, Math.ceil(value)));
        return value;
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
        return getInsets().getLeft() + getInsets().getRight();
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
        return getInsets().getTop() + getInsets().getBottom();
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
        Insets insets = getInsets();
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
        Insets insets = getInsets();
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
        boolean snap = isSnapToPixel();
        double left = margin != null ? snapSpace(margin.getLeft(), snap) : 0;
        double right = margin != null ? snapSpace(margin.getRight(), snap) : 0;
        double alt = -1;
        if (height != -1 && child.isResizable() && child.getContentBias() == Orientation.VERTICAL) { // width depends on height
            double top = margin != null ? snapSpace(margin.getTop(), snap) : 0;
            double bottom = (margin != null ? snapSpace(margin.getBottom(), snap) : 0);
            double bo = child.getBaselineOffset();
            double contentHeight = bo == BASELINE_OFFSET_SAME_AS_HEIGHT && baselineComplement != -1 ?
                    height - top - bottom - baselineComplement :
                    height - top - bottom;
            if (fillHeight)
                alt = snapSize(boundedSize(
                        child.minHeight(-1), contentHeight,
                        child.maxHeight(-1)));
            else
                alt = snapSize(boundedSize(
                        child.minHeight(-1),
                        child.prefHeight(-1),
                        Math.min(child.maxHeight(-1), contentHeight)));
        }
        return left + snapSize(child.minWidth(alt)) + right;
    }

    double computeChildMinAreaHeight(Node child, Insets margin) {
        return computeChildMinAreaHeight(child, -1, margin, -1);
    }

    double computeChildMinAreaHeight(Node child, double minBaselineComplement, Insets margin, double width) {
        boolean snap = isSnapToPixel();
        double top = margin != null ? snapSpace(margin.getTop(), snap) : 0;
        double bottom = margin != null ? snapSpace(margin.getBottom(), snap) : 0;

        double alt = -1;
        if (child.isResizable() && child.getContentBias() == Orientation.HORIZONTAL) { // height depends on width
            double left = margin != null ? snapSpace(margin.getLeft(), snap) : 0;
            double right = margin != null ? snapSpace(margin.getRight(), snap) : 0;
            alt = snapSize(width != -1 ? boundedSize(child.minWidth(-1), width - left - right, child.maxWidth(-1)) : child.maxWidth(-1));
        }

        // For explanation, see computeChildPrefAreaHeight
        if (minBaselineComplement != -1) {
            double baseline = child.getBaselineOffset();
            if (child.isResizable() && baseline == BASELINE_OFFSET_SAME_AS_HEIGHT)
                return top + snapSize(child.minHeight(alt)) + bottom + minBaselineComplement;
            return baseline + minBaselineComplement;
        }
        return top + snapSize(child.minHeight(alt)) + bottom;
    }

    double computeChildPrefAreaWidth(Node child, Insets margin) {
        return computeChildPrefAreaWidth(child, -1, margin, -1, false);
    }

    double computeChildPrefAreaWidth(Node child, double baselineComplement, Insets margin, double height, boolean fillHeight) {
        boolean snap = isSnapToPixel();
        double left = margin != null? snapSpace(margin.getLeft(), snap) : 0;
        double right = margin != null? snapSpace(margin.getRight(), snap) : 0;
        double alt = -1;
        if (height != -1 && child.isResizable() && child.getContentBias() == Orientation.VERTICAL) { // width depends on height
            double top = margin != null? snapSpace(margin.getTop(), snap) : 0;
            double bottom = margin != null? snapSpace(margin.getBottom(), snap) : 0;
            double bo = child.getBaselineOffset();
            double contentHeight = bo == BASELINE_OFFSET_SAME_AS_HEIGHT && baselineComplement != -1 ?
                    height - top - bottom - baselineComplement :
                    height - top - bottom;
            if (fillHeight) {
                alt = snapSize(boundedSize(
                        child.minHeight(-1), contentHeight,
                        child.maxHeight(-1)));
            } else {
                alt = snapSize(boundedSize(
                        child.minHeight(-1),
                        child.prefHeight(-1),
                        Math.min(child.maxHeight(-1), contentHeight)));
            }
        }
        return left + snapSize(boundedSize(child.minWidth(alt), child.prefWidth(alt), child.maxWidth(alt))) + right;
    }

    double computeChildPrefAreaHeight(Node child, Insets margin) {
        return computeChildPrefAreaHeight(child, -1, margin, -1);
    }

    double computeChildPrefAreaHeight(Node child, double prefBaselineComplement, Insets margin, double width) {
        boolean snap = isSnapToPixel();
        double top = margin != null? snapSpace(margin.getTop(), snap) : 0;
        double bottom = margin != null? snapSpace(margin.getBottom(), snap) : 0;

        double alt = -1;
        if (child.isResizable() && child.getContentBias() == Orientation.HORIZONTAL) { // height depends on width
            double left = margin != null ? snapSpace(margin.getLeft(), snap) : 0;
            double right = margin != null ? snapSpace(margin.getRight(), snap) : 0;
            alt = snapSize(boundedSize(
                    child.minWidth(-1), width != -1 ? width - left - right
                            : child.prefWidth(-1), child.maxWidth(-1)));
        }

        if (prefBaselineComplement != -1) {
            double baseline = child.getBaselineOffset();
            if (child.isResizable() && baseline == BASELINE_OFFSET_SAME_AS_HEIGHT)
                // When baseline is same as height, the preferred height of the node will be above the baseline, so we need to add
                // the preferred complement to it
                return top + snapSize(boundedSize(child.minHeight(alt), child.prefHeight(alt), child.maxHeight(alt))) + bottom
                        + prefBaselineComplement;
            // For all other Nodes, it's just their baseline and the complement.
            // Note that the complement already contain the Node's preferred (or fixed) height
            return top + baseline + prefBaselineComplement + bottom;
        }
        return top + snapSize(boundedSize(child.minHeight(alt), child.prefHeight(alt), child.maxHeight(alt))) + bottom;
    }

    double computeChildMaxAreaWidth(Node child, double baselineComplement, Insets margin, double height, boolean fillHeight) {
        double max = child.maxWidth(-1);
        if (max == Double.MAX_VALUE)
            return max;
        boolean snap = isSnapToPixel();
        double left = margin != null ? snapSpace(margin.getLeft(), snap) : 0;
        double right = margin != null ? snapSpace(margin.getRight(), snap) : 0;
        double alt = -1;
        if (height != -1 && child.isResizable() && child.getContentBias() == Orientation.VERTICAL) { // width depends on height
            double top = margin != null ? snapSpace(margin.getTop(), snap) : 0;
            double bottom = (margin != null ? snapSpace(margin.getBottom(), snap) : 0);
            double bo = child.getBaselineOffset();
            double contentHeight = bo == BASELINE_OFFSET_SAME_AS_HEIGHT && baselineComplement != -1 ?
                    height - top - bottom - baselineComplement :
                    height - top - bottom;
            if (fillHeight)
                alt = snapSize(boundedSize(
                        child.minHeight(-1), contentHeight,
                        child.maxHeight(-1)));
            else
                alt = snapSize(boundedSize(
                        child.minHeight(-1),
                        child.prefHeight(-1),
                        Math.min(child.maxHeight(-1), contentHeight)));
            max = child.maxWidth(alt);
        }
        // if min > max, min wins, so still need to call boundedSize()
        return left + snapSize(boundedSize(child.minWidth(alt), max, Double.MAX_VALUE)) + right;
    }

    double computeChildMaxAreaHeight(Node child, double maxBaselineComplement, Insets margin, double width) {
        double max = child.maxHeight(-1);
        if (max == Double.MAX_VALUE)
            return max;

        boolean snap = isSnapToPixel();
        double top = margin != null ? snapSpace(margin.getTop(), snap) : 0;
        double bottom = margin != null ? snapSpace(margin.getBottom(), snap) : 0;
        double alt = -1;
        if (child.isResizable() && child.getContentBias() == Orientation.HORIZONTAL) { // height depends on width
            double left = margin != null ? snapSpace(margin.getLeft(), snap) : 0;
            double right = margin != null ? snapSpace(margin.getRight(), snap) : 0;
            alt = snapSize(width != -1? boundedSize(child.minWidth(-1), width - left - right, child.maxWidth(-1)) :
                    child.minWidth(-1));
            max = child.maxHeight(alt);
        }
        // For explanation, see computeChildPrefAreaHeight
        if (maxBaselineComplement != -1) {
            double baseline = child.getBaselineOffset();
            if (child.isResizable() && baseline == BASELINE_OFFSET_SAME_AS_HEIGHT)
                return top + snapSize(boundedSize(child.minHeight(alt), child.maxHeight(alt), Double.MAX_VALUE)) + bottom
                        + maxBaselineComplement;
            return top + baseline + maxBaselineComplement + bottom;
        }
        // if min > max, min wins, so still need to call boundedSize()
        return top + snapSize(boundedSize(child.minHeight(alt), max, Double.MAX_VALUE)) + bottom;
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
                                 double areaHeight, boolean fillHeight, double minComplement) {
        return getAreaBaselineOffset(children, margins, positionToWidth, areaHeight, fillHeight, minComplement, isSnapToPixel());
    }

    static double getAreaBaselineOffset(List<Node> children, Callback<Node, Insets> margins,
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
    static double getAreaBaselineOffset(List<Node> children, Callback<Node, Insets> margins,
                                        Function<Integer, Double> positionToWidth,
                                        double areaHeight, Function<Integer, Boolean> fillHeight, double minComplement, boolean snapToPixel) {
        double b = 0;
        for (int i = 0;i < children.size(); ++i) {
            Node n = children.get(i);
            Insets margin = margins.call(n);
            double top = margin != null? snapSpace(margin.getTop(), snapToPixel) : 0;
            double bottom = (margin != null? snapSpace(margin.getBottom(), snapToPixel) : 0);
            double bo = n.getBaselineOffset();
            if (bo == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                double alt = -1;
                if (n.getContentBias() == Orientation.HORIZONTAL)
                    alt = positionToWidth.apply(i);
                if (fillHeight.apply(i))
                    // If the children fills it's height, than it's "preferred" height is the area without the complement and insets
                    b = Math.max(b, top + boundedSize(n.minHeight(alt), areaHeight - minComplement - top - bottom,
                            n.maxHeight(alt)));
                else
                    // Otherwise, we must use the area without complement and insets as a maximum for the Node
                    b = Math.max(b, top + boundedSize(n.minHeight(alt), n.prefHeight(alt),
                            Math.min(n.maxHeight(alt), areaHeight - minComplement - top - bottom)));
            } else
                b = Math.max(b, top + bo);
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

        position(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset,
                snapSpace(childMargin.getTop(), isSnapToPixel),
                snapSpace(childMargin.getRight(), isSnapToPixel),
                snapSpace(childMargin.getBottom(), isSnapToPixel),
                snapSpace(childMargin.getLeft(), isSnapToPixel),
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

        double top = snapSpace(childMargin.getTop(), isSnapToPixel);
        double bottom = snapSpace(childMargin.getBottom(), isSnapToPixel);
        double left = snapSpace(childMargin.getLeft(), isSnapToPixel);
        double right = snapSpace(childMargin.getRight(), isSnapToPixel);

        if (valignment == VPos.BASELINE) {
            double bo = child.getBaselineOffset();
            if (bo == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                if (child.isResizable())
                    // Everything below the baseline is like an "inset". The Node with BASELINE_OFFSET_SAME_AS_HEIGHT cannot
                    // be resized to this area
                    bottom += snapSpace(areaHeight - areaBaselineOffset, isSnapToPixel);
                else
                    top = snapSpace(areaBaselineOffset - child.getLayoutBounds().getHeight(), isSnapToPixel);
            } else
                top = snapSpace(areaBaselineOffset - bo, isSnapToPixel);
        }

        if (child.isResizable()) {
            Vec2d size = boundedNodeSizeWithBias(child, areaWidth - left - right, areaHeight - top - bottom,
                    fillWidth, fillHeight, TEMP_VEC2D);
            child.resize(snapSize(size.x, isSnapToPixel),snapSize(size.y, isSnapToPixel));
        }
        position(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset,
                top, right, bottom, left, halignment, valignment, isSnapToPixel);
    }

    private static void position(Node child, double areaX, double areaY, double areaWidth, double areaHeight,
                                 double areaBaselineOffset,
                                 double topMargin, double rightMargin, double bottomMargin, double leftMargin,
                                 HPos hpos, VPos vpos, boolean isSnapToPixel) {
        double xoffset = leftMargin + computeXOffset(areaWidth - leftMargin - rightMargin,
                child.getLayoutBounds().getWidth(), hpos);
        double yoffset;
        if (vpos == VPos.BASELINE) {
            double bo = child.getBaselineOffset();
            if (bo == BASELINE_OFFSET_SAME_AS_HEIGHT)
                // We already know the layout bounds at this stage, so we can use them
                yoffset = areaBaselineOffset - child.getLayoutBounds().getHeight();
            else
                yoffset = areaBaselineOffset - bo;
        } else
            yoffset = topMargin + computeYOffset(areaHeight - topMargin - bottomMargin,
                    child.getLayoutBounds().getHeight(), vpos);
        double x = snapPosition(areaX + xoffset, isSnapToPixel);
        double y = snapPosition(areaY + yoffset, isSnapToPixel);

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
    static double boundedSize(double min, double pref, double max) {
        double a = pref >= min ? pref : min;
        double b = min >= max ? min : max;
        return a <= b ? a : b;
    }

    double adjustWidthByMargin(double width, Insets margin) {
        if (margin == null || margin == Insets.EMPTY)
            return width;
        boolean isSnapToPixel = isSnapToPixel();
        return width - snapSpace(margin.getLeft(), isSnapToPixel) - snapSpace(margin.getRight(), isSnapToPixel);
    }

    double adjustHeightByMargin(double height, Insets margin) {
        if (margin == null || margin == Insets.EMPTY)
            return height;
        boolean isSnapToPixel = isSnapToPixel();
        return height - snapSpace(margin.getTop(), isSnapToPixel) - snapSpace(margin.getBottom(), isSnapToPixel);
    }

}
