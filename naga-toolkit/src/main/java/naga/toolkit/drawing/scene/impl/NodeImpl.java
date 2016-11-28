package naga.toolkit.drawing.scene.impl;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import naga.toolkit.drawing.geom.BaseBounds;
import naga.toolkit.drawing.geom.RectBounds;
import naga.toolkit.drawing.geom.TempState;
import naga.toolkit.drawing.geom.transform.BaseTransform;
import naga.toolkit.drawing.geometry.BoundingBox;
import naga.toolkit.drawing.geometry.Bounds;
import naga.toolkit.drawing.effect.BlendMode;
import naga.toolkit.drawing.scene.Node;
import naga.toolkit.drawing.geometry.Orientation;
import naga.toolkit.drawing.scene.Parent;
import naga.toolkit.drawing.effect.Effect;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;
import naga.toolkit.transform.Transform;
import naga.toolkit.transform.Translate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class NodeImpl implements Node {

    private final Property<Parent> parentProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Parent> parentProperty() {
        return parentProperty;
    }

    private final Property<Boolean> managedProperty = new SimpleObjectProperty<Boolean>(true) {
        @Override
        protected void invalidated() {
            Parent parent = getParent();
            if (parent instanceof ParentImpl) {
                ((ParentImpl) parent).managedChildChanged();
            }
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

    private final ObjectProperty<UiEventHandler<? super MouseEvent>> onMouseClickedProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<UiEventHandler<? super MouseEvent>> onMouseClickedProperty() {
        return onMouseClickedProperty;
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

    private final Property<Node> clipProperty = new SimpleObjectProperty<>();
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

    public static double boundedSize(double value, double min, double max) {
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

    @Override
    public Bounds getLayoutBounds() {
        return impl_computeLayoutBounds();
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
        System.out.println("Warning: effect or clip not implemented in getLocalBounds()");
        //if (getEffect() == null && getClip() == null)
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
}
