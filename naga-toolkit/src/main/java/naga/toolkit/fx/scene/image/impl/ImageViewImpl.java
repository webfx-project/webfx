package naga.toolkit.fx.scene.image.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.geom.BaseBounds;
import naga.toolkit.fx.geom.transform.BaseTransform;
import naga.toolkit.fx.scene.image.ImageView;
import naga.toolkit.fx.scene.impl.NodeImpl;

/**
 * @author Bruno Salmon
 */
public class ImageViewImpl extends NodeImpl implements ImageView {

    public ImageViewImpl() {
    }

    public ImageViewImpl(String imageUrl) {
        setImageUrl(imageUrl);
    }

    private final Property<String> imageUrlProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> imageUrlProperty() {
        return imageUrlProperty;
    }

    private final Property<Double> fitWidthProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> fitWidthProperty() {
        return fitWidthProperty;
    }

    private final Property<Double> fitHeightProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> fitHeightProperty() {
        return fitHeightProperty;
    }

    private final Property<Double> xProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> xProperty() {
        return xProperty;
    }

    private final Property<Double> yProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> yProperty() {
        return yProperty;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        // This implementation works only when fitWidth and fitHeight are set. But the node view should measure the
        // layoutBounds so this method shouldn't be called.
        bounds = bounds.deriveWithNewBounds(getX(), getY(), 0, getX() + getFitWidth(), getY() + getFitHeight(), 0);
        bounds = tx.transform(bounds, bounds);
        return bounds;
    }
}
