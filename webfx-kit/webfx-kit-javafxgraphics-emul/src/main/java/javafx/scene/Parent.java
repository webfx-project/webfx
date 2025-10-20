package javafx.scene;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasManagedProperty;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasTextProperty;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.kit.util.properties.ObservableLists;
import dev.webfx.platform.console.Console;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.LayoutFlags;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public class Parent extends Node {

    private static final boolean LOG_LAYOUT_TIMINGS = false; // Can be set to true to help debugging slow layout
    private static final long LOG_LAYOUT_TIMINGS_THRESHOLD = 0;

    private final ObservableList<Node> children = FXCollections.observableArrayList();
    {
        children.addListener(this::onChildrenChanged);
    }

    private void onChildrenChanged(ListChangeListener.Change<? extends Node> c) {
        // This listener has 2 main tasks: 1) propagate this parent change to the children & 2) ask the scene to update
        // the peers structure (scene graph => DOM tree mapping).

        // First we propagate this parent to the children (and set parent to null for removed children).
        // Note that the scene propagation from parent to children is managed in Node.setParent()
        if (c == null) {
            for (Node child : getChildren())
                child.setParent(this);
        } else {
            while (c.next()) {
                List<? extends Node> removed = c.getRemoved();
                List<? extends Node> addedSubList = c.getAddedSubList();
                // Setting parent (and scene) to null for removed children
                for (Node child : removed) {
                    if (child.getParent() == Parent.this && !addedSubList.contains(child))
                        child.setParent(null);
                }
                // Setting parent to added children
                for (Node child : addedSubList) {
                    if (child.getParent() != Parent.this)
                        child.setParent(Parent.this);
                }
            }
        }
        // Then we do 2) i.e. call scene.updateParentAndChildrenPeers()
        Scene scene = getScene(); // Of course scene needs to be non-null and the node peer of this parent needs to be created
        if (scene != null) {
            if (c != null)
                c.reset(); // Because we already used it in the previous loop.
            scene.updateParentAndChildrenPeers(this, c);
        }

        // Final detail, we need to call managedChildChanged()
        managedChildChanged();
    }

    @Override
    void onNodePeerBound() {
        FXProperties.onPropertySet((ObservableValue) getProperties().get("skinProperty"),
                skin -> onChildrenChanged(null), true);
    }


    Parent() {
    }

    public Parent(Node... nodes) {
        ObservableLists.setAllNonNulls(getChildren(), nodes);
    }

    public ObservableList<Node> getChildren() {
        return children;
    }

    /**
     * A constant reference to an unmodifiable view of the children, such that every time
     * we ask for an unmodifiable list of children, we don't actually create a new
     * collection and return it. The memory overhead is pretty lightweight compared
     * to all the garbage we would otherwise generate.
     */
    private final ObservableList<Node> unmodifiableChildren =
            FXCollections.unmodifiableObservableList(children);


    /**
     * Gets the list of children of this {@code Parent} as a read-only
     * list.
     *
     * @return read-only access to this parent's children ObservableList
     */
    public ObservableList<Node> getChildrenUnmodifiable() {
        return unmodifiableChildren;
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

    private ParentTraversalEngine traversalEngine;

    public final void setTraversalEngine(ParentTraversalEngine value) {
        this.traversalEngine = value;
    }

    public final ParentTraversalEngine getTraversalEngine() {
        return traversalEngine;
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
    private BooleanProperty needsLayout;
    LayoutFlags layoutFlag = LayoutFlags.CLEAN;

    protected final void setNeedsLayout(boolean value) {
        if (value)
            markDirtyLayout(true);
        else if (layoutFlag == LayoutFlags.NEEDS_LAYOUT) {
            boolean hasBranch = false;
            for (Node child : children) {
                if (child instanceof Parent) {
                    if (((Parent) child).layoutFlag != LayoutFlags.CLEAN) {
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

    public final BooleanProperty needsLayoutProperty() {
        if (needsLayout == null)
            needsLayout = new SimpleBooleanProperty(layoutFlag == LayoutFlags.NEEDS_LAYOUT);
        return needsLayout;
    }

    /**
     * This package level is used only by Node. It is set to true while
     * the layout() function is processing and set to false on the conclusion.
     * It is used by the Node to decide whether to perform CSS updates
     * synchronously or asynchronously.
     */
    protected boolean performingLayout = false;

    private boolean sizeCacheClear = true;
    private double prefWidthCache = -1;
    private double prefHeightCache = -1;
    private double minWidthCache = -1;
    private double minHeightCache = -1;

    public void setLayoutFlag(LayoutFlags flag) {
        if (needsLayout != null)
            needsLayout.setValue(flag == LayoutFlags.NEEDS_LAYOUT);
        layoutFlag = flag;
    }

    private void markDirtyLayout(boolean local) {
        setLayoutFlag(LayoutFlags.NEEDS_LAYOUT);
        if (local || layoutRoot) {
            if (sceneRoot)
                requestNextPulse();
            else
                markDirtyLayoutBranch();
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
            if (IS_PERFORMING_LAYOUT) { // WebFX optimization:
                // It's normal that a parent changes its children's sizes during a layout pass, and this shouldn't
                // trigger another request for a layout to the parent again.
                return;
                // Only changes outside the layout pass should trigger a layout request.
            }
            Parent parent = getParent();
            if (parent != null)
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
    protected double impl_prefWidth(double height) {
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
    protected double impl_prefHeight(double width) {
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
    protected double impl_minWidth(double height) {
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
    protected double impl_minHeight(double width) {
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
    @Override
    public double getBaselineOffset() {
        for (Node child : children) {
            if (child.isManaged()) {
                double offset = child.getBaselineOffset();
                if (offset != BASELINE_OFFSET_SAME_AS_HEIGHT)
                    return child.getLayoutBounds().getMinY() + child.getLayoutY() + offset;
            }
        }
        return super.getBaselineOffset();
    }

    public static boolean IS_PERFORMING_LAYOUT; // WebFX addition

    /**
     * Executes a top-down layout pass on the scene graph under this parent.
     *
     * Calling this method while the Parent is doing layout is a no-op.
     */
    public /*final*/ void layout() {
        IS_PERFORMING_LAYOUT = true;
        layout(0);
        IS_PERFORMING_LAYOUT = false;
    }

    private void layout(int graphLevel) {
        long t0 = System.currentTimeMillis();
        long t1 = t0;
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
                t1 = System.currentTimeMillis();
                // Intended fall-through
            case DIRTY_BRANCH:
                for (Node child : new ArrayList<>(children)) {
                    if (child instanceof Parent) {
                        ((Parent) child).layout(graphLevel + 1);
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
        if (LOG_LAYOUT_TIMINGS) {
            long t2 = System.currentTimeMillis();
            if (t2 - t0 >= LOG_LAYOUT_TIMINGS_THRESHOLD) {
                Console.log(levelString(graphLevel) + " layout: " + (t2 - t0) + " ms (" + (t1 - t0) + " + " + (t2 - t1) + ") for " + this + sizeString(this) + textContent(this));
            }
        }
    }

    private static final String[] GRAPH_LEVEL_SYMBOLS = { "️1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "0️⃣"};

    private String levelString(int graphLevel) {
        if (graphLevel == 0)
            return "⚛️";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < graphLevel; i++)
            sb.append(GRAPH_LEVEL_SYMBOLS[i % GRAPH_LEVEL_SYMBOLS.length]);
        return sb.toString();
    }

    private String sizeString(Parent parent) {
        if (parent instanceof Region region) {
            return " - size = " + region.getWidth() + " x " + region.getHeight();
        }
        return "";
    }

    private String textContent(Parent parent) {
        if (parent instanceof HasTextProperty hasTextProperty) {
            return " - text = " + hasTextProperty.getText();
        }
        return "";
    }

    /**
     * Invoked during the layout pass to layout the children in this
     * {@code Parent}. By default it will only set the size of managed,
     * resizable content to their preferred sizes and does not do any node
     * positioning.
     *
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
    private boolean layoutRoot = false;
    @Override
    final void notifyManagedChanged() {
        layoutRoot = !isManaged() || sceneRoot;
    }

    final boolean isSceneRoot() {
        return sceneRoot;
    }

    // Called by Scene only
    public void setSceneRoot(boolean sceneRoot) {
        this.sceneRoot = sceneRoot;
        notifyManagedChanged();
    }

    /***************************************************************************
     *                                                                         *
     *                         Bounds Computations                             *
     *                                                                         *
     *  This code originated in GroupBoundsHelper (part of javafx-sg-common)   *
     *  but has been ported here to the FX side since we cannot rely on the PG *
     *  side for computing the bounds (due to the decoupling of the two        *
     *  scenegraphs for threading and other purposes).                         *
     *                                                                         *
     *  Unfortunately, we cannot simply reuse GroupBoundsHelper without some  *
     *  major (and hacky) modification due to the fact that GroupBoundsHelper  *
     *  relies on PG state and we need to do similar things here that rely on  *
     *  core scenegraph state. Unfortunately, that means we made a port.       *
     *                                                                         *
     **************************************************************************/

    private BaseBounds tmp = new RectBounds();

    /**
     * The cached bounds for the Group. If the cachedBounds are invalid
     * then we have no history of what the bounds are, or were.
     */
    private BaseBounds cachedBounds = new RectBounds();

    /**
     * Indicates that the cachedBounds is invalid (or old) and need to be recomputed.
     * If cachedBoundsInvalid is true and dirtyChildrenCount is non-zero,
     * then when we recompute the cachedBounds we can consider the
     * values in cachedBounds to represent the last valid bounds for the group.
     * This is useful for several fast paths.
     */
    private boolean cachedBoundsInvalid;

    /**
     * The number of dirty children which bounds haven't been incorporated
     * into the cached bounds yet. Can be used even when dirtyChildren is null.
     */
    //private int dirtyChildrenCount;

    /**
     * This set is used to track all of the children of this group which are
     * dirty. It is only used in cases where the number of children is > some
     * value (currently 10). For very wide trees, this can provide a very
     * important speed boost. For the sake of memory consumption, this is
     * null unless the number of children ever crosses the threshold where
     * it will be activated.
     */
/*
    private ArrayList<Node> dirtyChildren;

    private Node top;
    private Node left;
    private Node bottom;
    private Node right;
    private Node near;
    private Node far;
*/

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        // If we have no children, our bounds are invalid
        if (children.isEmpty()) {
            return bounds.makeEmpty();
        }

        if (tx.isTranslateOrIdentity()) {
            // this is a transform which is only doing translations, or nothing
            // at all (no scales, rotates, or shears)
            // so in this case we can easily use the cached bounds
            if (true || cachedBoundsInvalid) {
                recomputeBounds();

                /*if (dirtyChildren != null)
                    dirtyChildren.clear();*/
                cachedBoundsInvalid = false;
                //dirtyChildrenCount = 0;
            }
            if (!tx.isIdentity()) {
                throw new UnsupportedOperationException("Transform other than Identity not supported in Parent.impl_computeGeomBounds()");
/*
                bounds = bounds.deriveWithNewBounds((float)(cachedBounds.getMinX() + tx.getMxt()),
                        (float)(cachedBounds.getMinY() + tx.getMyt()),
                        (float)(cachedBounds.getMinZ() + tx.getMzt()),
                        (float)(cachedBounds.getMaxX() + tx.getMxt()),
                        (float)(cachedBounds.getMaxY() + tx.getMyt()),
                        (float)(cachedBounds.getMaxZ() + tx.getMzt()));
*/
            } else
                bounds = bounds.deriveWithNewBounds(cachedBounds);

            return bounds;
        } else {
            // there is a scale, shear, or rotation happening, so need to
            // do the full transform!
            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE;
            boolean first = true;
            for (Node node : children) {
                if (node.isVisible()) {
                    bounds = getChildTransformedBounds(node, tx, bounds);
                    // if the bounds of the child are invalid, we don't want
                    // to use those in the remaining computations.
                    if (bounds.isEmpty()) continue;
                    if (first) {
                        minX = bounds.getMinX();
                        minY = bounds.getMinY();
                        minZ = bounds.getMinZ();
                        maxX = bounds.getMaxX();
                        maxY = bounds.getMaxY();
                        maxZ = bounds.getMaxZ();
                        first = false;
                    } else {
                        minX = Math.min(bounds.getMinX(), minX);
                        minY = Math.min(bounds.getMinY(), minY);
                        minZ = Math.min(bounds.getMinZ(), minZ);
                        maxX = Math.max(bounds.getMaxX(), maxX);
                        maxY = Math.max(bounds.getMaxY(), maxY);
                        maxZ = Math.max(bounds.getMaxZ(), maxZ);
                    }
                }
            }
            // if "first" is still true, then we didn't have any children with
            // non-empty bounds and thus we must return an empty bounds,
            // otherwise we have non-empty bounds so go for it.
            if (first)
                bounds.makeEmpty();
            else
                bounds = bounds.deriveWithNewBounds((float) minX, (float) minY, (float) minZ, (float) maxX, (float) maxY, (float) maxZ);

            return bounds;
        }
    }

    /**
     * Recomputes the bounds from scratch and saves the cached bounds.
     */
    private void recomputeBounds() {
        // fast path for case of no children
        if (children.isEmpty()) {
            cachedBounds.makeEmpty();
            return;
        }

        // fast path for case of 1 child
        if (children.size() == 1) {
            Node node = children.get(0);
            node.boundsChanged = false;
            if (node.isVisible()) {
                cachedBounds = getChildTransformedBounds(node, BaseTransform.IDENTITY_TRANSFORM, cachedBounds);
                // top = left = bottom = right = near = far = node;
            } else {
                cachedBounds.makeEmpty();
                // no need to null edge nodes here, it was done in childExcluded
                // top = left = bottom = right = near = far = null;
            }
            return;
        }

/*
        if ((dirtyChildrenCount == 0) ||
                !updateCachedBounds(dirtyChildren != null
                                ? dirtyChildren : children,
                        dirtyChildrenCount))
*/
        // failed to update cached bounds, recreate them
        createCachedBounds(children);
    }

    // Note: this marks the currently processed child in terms of transformed bounds. In rare situations like
    // in RT-37879, it might happen that the child bounds will be marked as invalid. Due to optimizations,
    // the invalidation must *always* be propagated to the parent, because the parent with some transformation
    // calls child's getTransformedBounds non-idenitity transform and the child's transformed bounds are thus not validated.
    // This does not apply to the call itself however, because the call will yield the correct result even if something
    // was invalidated during the computation. We can safely ignore such invalidations from that Node in this case
    private Node currentlyProcessedChild;

    private BaseBounds getChildTransformedBounds(Node node, BaseTransform tx, BaseBounds bounds) {
        currentlyProcessedChild = node;
        bounds = node.getTransformedBounds(bounds, tx);
        currentlyProcessedChild = null;
        return bounds;
    }

    private void createCachedBounds(final List<Node> fromNodes) {
        // These indicate the bounds of the Group as computed by this function
        double minX, minY, minZ;
        double maxX, maxY, maxZ;

        int nodeCount = fromNodes.size();
        int i;

        // handle first visible non-empty node
        for (i = 0; i < nodeCount; ++i) {
            Node node = fromNodes.get(i);
            node.boundsChanged = false;
            if (node.isVisible()) {
                tmp = node.getTransformedBounds(
                        tmp, BaseTransform.IDENTITY_TRANSFORM);
                if (!tmp.isEmpty()) {
                    //left = top = near = right = bottom = far = node;
                    break;
                }
            }
        }

        if (i == nodeCount) {
            //left = top = near = right = bottom = far = null;
            cachedBounds.makeEmpty();
            return;
        }

        minX = tmp.getMinX();
        minY = tmp.getMinY();
        minZ = tmp.getMinZ();
        maxX = tmp.getMaxX();
        maxY = tmp.getMaxY();
        maxZ = tmp.getMaxZ();

        // handle remaining visible non-empty nodes
        for (++i; i < nodeCount; ++i) {
            final Node node = fromNodes.get(i);
            node.boundsChanged = false;
            if (node.isVisible()) {
                tmp = node.getTransformedBounds(
                        tmp, BaseTransform.IDENTITY_TRANSFORM);
                if (!tmp.isEmpty()) {
                    double tmpx = tmp.getMinX();
                    double tmpy = tmp.getMinY();
                    double tmpz = tmp.getMinZ();
                    double tmpx2 = tmp.getMaxX();
                    double tmpy2 = tmp.getMaxY();
                    double tmpz2 = tmp.getMaxZ();

                    if (tmpx < minX) { minX = tmpx; /*left = node;*/ }
                    if (tmpy < minY) { minY = tmpy; /*top = node;*/ }
                    if (tmpz < minZ) { minZ = tmpz; /*near = node;*/ }
                    if (tmpx2 > maxX) { maxX = tmpx2; /*right = node;*/ }
                    if (tmpy2 > maxY) { maxY = tmpy2; /*bottom = node;*/ }
                    if (tmpz2 > maxZ) { maxZ = tmpz2; /*far = node;*/ }
                }
            }
        }

        cachedBounds = cachedBounds.deriveWithNewBounds((float) minX, (float) minY, (float) minZ, (float) maxX, (float) maxY, (float) maxZ);
    }

    // implementation of Node.toFront function
    final void toFront(Node node) {
/*
        if (Utils.assertionEnabled()) {
            if (!childSet.contains(node)) {
                throw new java.lang.AssertionError(
                        "specified node is not in the list of children");
            }
        }
*/

        if (children.get(children.size() - 1) != node) {
            //childrenTriggerPermutation = true;
            try {
                children.remove(node);
                children.add(node);
            } finally {
                //childrenTriggerPermutation = false;
            }
        }
    }

    // implementation of Node.toBack function
    final void toBack(Node node) {
/*
        if (Utils.assertionEnabled()) {
            if (!childSet.contains(node)) {
                throw new java.lang.AssertionError(
                        "specified node is not in the list of children");
            }
        }
*/

        if (children.get(0) != node) {
            //childrenTriggerPermutation = true;
            try {
                children.remove(node);
                children.add(0, node);
            } finally {
                //childrenTriggerPermutation = false;
            }
        }
    }

}
