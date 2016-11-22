package naga.toolkit.drawing.shapes.impl;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import naga.toolkit.drawing.shapes.*;
import naga.toolkit.effect.Effect;
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
abstract class NodeImpl implements Node {

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
    public Bounds getLayoutBounds() {
        return null;
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


}
