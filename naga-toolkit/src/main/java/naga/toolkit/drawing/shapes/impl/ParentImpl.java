package naga.toolkit.drawing.shapes.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.BoundingBox;
import naga.toolkit.drawing.shapes.Bounds;
import naga.toolkit.drawing.shapes.Node;
import naga.toolkit.drawing.shapes.Parent;
import naga.toolkit.properties.markers.HasManagedProperty;
import naga.toolkit.util.ObservableLists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
class ParentImpl extends NodeImpl implements Parent {

    private final ObservableList<Node> children = FXCollections.observableArrayList();

    {
        children.addListener((ListChangeListener<Node>) c -> requestLayout());
    }

    ParentImpl() {
    }

    ParentImpl(Node... nodes) {
        ObservableLists.setAllNonNulls(getChildren(), nodes);
    }

    public ObservableList<Node> getChildren() {
        return children;
    }

    /**
     * A cached reference to the unmodifiable managed children of this Parent. This is
     * created whenever first asked for, and thrown away whenever children are added
     * or removed or when their managed state changes. This could be written
     * differently, such that this list is essentially a filtered copy of the
     * main children, but that additional overhead might not be worth it.
     */
    private List<Node> unmodifiableManagedChildren = null;
    /**
     * Gets the list of all managed children of this {@code Parent}.
     *
     * @param <E> the type of the children nodes
     * @return list of all managed children in this parent
     */
    protected <E extends Node> List<E> getManagedChildren() {
        if (unmodifiableManagedChildren == null)
            unmodifiableManagedChildren = children.stream().filter(HasManagedProperty::isManaged).collect(Collectors.toList());
        return (List<E>)unmodifiableManagedChildren;
    }
    /**
     * Called by Node whenever its managed state may have changed, this
     * method will cause the view of managed children to be updated
     * such that it properly includes or excludes this child.
     */
    final void managedChildChanged() {
        requestLayout();
        unmodifiableManagedChildren = null;
    }

    /***********************************************************************
     *                               Layout                                *
     *                                                                     *
     *  Functions and variables related to the layout scheme used by       *
     *  JavaFX. Includes both public and private API.                      *
     *                                                                     *
     **********************************************************************/
    /**
     * Indicates that this Node and its subnodes requires a layout pass on
     * the next pulse.
     */
    private Property<Boolean> needsLayout;
    LayoutFlags layoutFlag = LayoutFlags.CLEAN;

    protected final void setNeedsLayout(boolean value) {
        if (value)
            markDirtyLayout(true);
        else if (layoutFlag == LayoutFlags.NEEDS_LAYOUT) {
            boolean hasBranch = false;
            for (Node child : children) {
                if (child instanceof ParentImpl) {
                    if (((ParentImpl) child).layoutFlag != LayoutFlags.CLEAN) {
                        hasBranch = true;
                        break;
                    }
                }
            }
            setLayoutFlag(hasBranch ? LayoutFlags.DIRTY_BRANCH : LayoutFlags.CLEAN);
        }
    }

    public final boolean isNeedsLayout() {
        return layoutFlag == LayoutFlags.NEEDS_LAYOUT;
    }

    public final Property<Boolean> needsLayoutProperty() {
        if (needsLayout == null)
            needsLayout = new SimpleObjectProperty<>(layoutFlag == LayoutFlags.NEEDS_LAYOUT);
        return needsLayout;
    }

    /**
     * This package level is used only by Node. It is set to true while
     * the layout() function is processing and set to false on the conclusion.
     * It is used by the Node to decide whether to perform CSS updates
     * synchronously or asynchronously.
     */
    boolean performingLayout = false;

    private boolean sizeCacheClear = true;
    private double prefWidthCache = -1;
    private double prefHeightCache = -1;
    private double minWidthCache = -1;
    private double minHeightCache = -1;

    void setLayoutFlag(LayoutFlags flag) {
        if (needsLayout != null)
            needsLayout.setValue(flag == LayoutFlags.NEEDS_LAYOUT);
        layoutFlag = flag;
    }

    private void markDirtyLayout(boolean local) {
        setLayoutFlag(LayoutFlags.NEEDS_LAYOUT);
        if (local || layoutRoot) {
/*
            if (sceneRoot) {
                Toolkit.getToolkit().requestNextPulse();
                if (getSubScene() != null) {
                    getSubScene().setDirtyLayout(this);
                }
            } else {
                markDirtyLayoutBranch();
            }
*/
        } else
            requestParentLayout();
    }

    /**
     * Requests a layout pass to be performed before the next scene is
     * rendered. This is batched up asynchronously to happen once per
     * "pulse", or frame of animation.
     * <p>
     * If this parent is either a layout root or unmanaged, then it will be
     * added directly to the scene's dirty layout list, otherwise requestParentLayout
     * will be invoked.
     */
    public void requestLayout() {
        clearSizeCache();
        markDirtyLayout(false);
    }

    /**
     * Requests a layout pass of the parent to be performed before the next scene is
     * rendered. This is batched up asynchronously to happen once per
     * "pulse", or frame of animation.
     * <p>
     * This may be used when the current parent have changed it's min/max/preferred width/height,
     * but doesn't know yet if the change will lead to it's actual size change. This will be determined
     * when it's parent recomputes the layout with the new hints.
     */
    protected final void requestParentLayout() {
        if (!layoutRoot) {
            Parent parent = getParent();
            if (parent instanceof ParentImpl && !((ParentImpl) parent).performingLayout)
                parent.requestLayout();
        }

    }

    void clearSizeCache() {
        if (!sizeCacheClear) {
            prefWidthCache = -1;
            prefHeightCache = -1;
            minWidthCache = -1;
            minHeightCache = -1;
            sizeCacheClear = true;
        }
    }

    @Override
    public double prefWidth(double height) {
        if (height == -1) {
            if (prefWidthCache == -1) {
                prefWidthCache = computePrefWidth(-1);
                if (Double.isNaN(prefWidthCache) || prefWidthCache < 0)
                    prefWidthCache = 0;
                sizeCacheClear = false;
            }
            return prefWidthCache;
        }
        double result = computePrefWidth(height);
        return Double.isNaN(result) || result < 0 ? 0 : result;
    }

    @Override
    public double prefHeight(double width) {
        if (width == -1) {
            if (prefHeightCache == -1) {
                prefHeightCache = computePrefHeight(-1);
                if (Double.isNaN(prefHeightCache) || prefHeightCache < 0)
                    prefHeightCache = 0;
                sizeCacheClear = false;
            }
            return prefHeightCache;
        }
        double result = computePrefHeight(width);
        return Double.isNaN(result) || result < 0 ? 0 : result;
    }

    @Override
    public double minWidth(double height) {
        if (height == -1) {
            if (minWidthCache == -1) {
                minWidthCache = computeMinWidth(-1);
                if (Double.isNaN(minWidthCache) || minWidthCache < 0)
                    minWidthCache = 0;
                sizeCacheClear = false;
            }
            return minWidthCache;
        }
        double result = computeMinWidth(height);
        return Double.isNaN(result) || result < 0 ? 0 : result;
    }

    @Override
    public double minHeight(double width) {
        if (width == -1) {
            if (minHeightCache == -1) {
                minHeightCache = computeMinHeight(-1);
                if (Double.isNaN(minHeightCache) || minHeightCache < 0)
                    minHeightCache = 0;
                sizeCacheClear = false;
            }
            return minHeightCache;
        }
        double result = computeMinHeight(width);
        return Double.isNaN(result) || result < 0 ? 0 : result;
    }

    /**
     * Calculates the preferred width of this {@code Parent}. The default
     * implementation calculates this width as the width of the area occupied
     * by its managed children when they are positioned at their
     * current positions at their preferred widths.
     *
     * @param height the height that should be used if preferred width depends on it
     * @return the calculated preferred width
     */
    protected double computePrefWidth(double height) {
        double minX = 0;
        double maxX = 0;
        for (Node node : children) {
            if (node.isManaged()) {
                double x = node.getLayoutBounds().getMinX() + node.getLayoutX();
                minX = Math.min(minX, x);
                maxX = Math.max(maxX, x + boundedSize(node.prefWidth(-1), node.minWidth(-1), node.maxWidth(-1)));
            }
        }
        return maxX - minX;
    }

    /**
     * Calculates the preferred height of this {@code Parent}. The default
     * implementation calculates this height as the height of the area occupied
     * by its managed children when they are positioned at their current
     * positions at their preferred heights.
     *
     * @param width the width that should be used if preferred height depends on it
     * @return the calculated preferred height
     */
    protected double computePrefHeight(double width) {
        double minY = 0;
        double maxY = 0;
        for (Node node : children) {
            if (node.isManaged()) {
                double y = node.getLayoutBounds().getMinY() + node.getLayoutY();
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y + boundedSize(node.prefHeight(-1), node.minHeight(-1), node.maxHeight(-1)));
            }
        }
        return maxY - minY;
    }

    /**
     * Calculates the minimum width of this {@code Parent}. The default
     * implementation simply returns the pref width.
     *
     * @param height the height that should be used if min width depends on it
     * @return the calculated min width
     */
    protected double computeMinWidth(double height) {
        return prefWidth(height);
    }

    /**
     * Calculates the min height of this {@code Parent}. The default
     * implementation simply returns the pref height;
     *
     * @param width the width that should be used if min height depends on it
     * @return the calculated min height
     */
    protected double computeMinHeight(double width) {
        return prefHeight(width);
    }

    /**
     * Calculates the baseline offset based on the first managed child. If there
     * is no such child, returns {@link Node#getBaselineOffset()}.
     *
     * @return baseline offset
     */
    @Override public double getBaselineOffset() {
        for (Node child : children) {
            if (child.isManaged()) {
                double offset = child.getBaselineOffset();
                if (offset == BASELINE_OFFSET_SAME_AS_HEIGHT)
                    continue;
                return child.getLayoutBounds().getMinY() + child.getLayoutY() + offset;
            }
        }
        return super.getBaselineOffset();
    }

    /**
     * Executes a top-down layout pass on the scene graph under this parent.
     *
     * Calling this method while the Parent is doing layout is a no-op.
     */
    public final void layout() {
        switch(layoutFlag) {
            case CLEAN:
                break;
            case NEEDS_LAYOUT:
                if (performingLayout) {
                    /* This code is here mainly to avoid infinite loops as layout() is public and the call might be (indirectly) invoked accidentally
                     * while doing the layout.
                     * One example might be an invocation from Group layout bounds recalculation
                     *  (e.g. during the localToScene/localToParent calculation).
                     * The layout bounds will thus return layout bounds that are "old" (i.e. before the layout changes, that are just being done),
                     * which is likely what the code would expect.
                     * The changes will invalidate the layout bounds again however, so the layout bounds query after layout pass will return correct answer.
                     */
                    break;
                }
                performingLayout = true;
                layoutChildren();
                // Intended fall-through
            case DIRTY_BRANCH:
                for (Node child : children) {
                    if (child instanceof ParentImpl) {
                        ((ParentImpl) child).layout();
/*
                    } else if (child instanceof SubScene) {
                        ((SubScene)child).layoutPass();
*/
                    }
                }
                setLayoutFlag(LayoutFlags.CLEAN);
                performingLayout = false;
                break;
        }
    }

    /**
     * Invoked during the layout pass to layout the children in this
     * {@code Parent}. By default it will only set the size of managed,
     * resizable content to their preferred sizes and does not do any node
     * positioning.
     * <p>
     * Subclasses should override this function to layout content as needed.
     */
    protected void layoutChildren() {
        children.stream().filter(node -> node.isResizable() && node.isManaged()).forEach(Node::autosize);
    }

    /**
     * This field is managed by the Scene, and set on any node which is the
     * root of a Scene.
     */
    private boolean sceneRoot = false;

    /**
     * Keeps track of whether this node is a layout root. This is updated
     * whenever the sceneRoot field changes, or whenever the managed
     * property changes.
     */
    boolean layoutRoot = false;
    @Override final void notifyManagedChanged() {
        layoutRoot = !isManaged() || sceneRoot;
    }

    final boolean isSceneRoot() {
        return sceneRoot;
    }

    @Override
    public Bounds getLayoutBounds() {
        int n = children.size();
        if (n == 0)
            return BoundingBox.EMPTY;
        Bounds bounds = children.get(0).getLayoutBounds();
        if (n == 1)
            return bounds;
        double minX = bounds.getMinX();
        double maxX = bounds.getMaxX();
        double minY = bounds.getMinY();
        double maxY = bounds.getMaxY();
        for (int i = 1; i < n; i++) {
            Node node = children.get(i);
            bounds = node.getLayoutBounds();
            minX = Math.min(minX, bounds.getMinX());
            maxX = Math.max(maxX, bounds.getMaxX());
            minY = Math.min(minY, bounds.getMinY());
            maxY = Math.max(maxY, bounds.getMaxY());
        }
        return BoundingBox.create(minX, minY, maxX - minX, maxY - minY);
    }
}
