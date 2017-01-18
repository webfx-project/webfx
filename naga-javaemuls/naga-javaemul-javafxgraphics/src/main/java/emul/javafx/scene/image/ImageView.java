package emul.javafx.scene.image;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.com.sun.javafx.geom.BaseBounds;
import emul.com.sun.javafx.geom.transform.BaseTransform;
import naga.fx.properties.markers.*;
import emul.javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public class ImageView extends Node implements
        HasImageProperty,
        HasImageUrlProperty,
        HasFitWidthProperty,
        HasFitHeightProperty,
        HasXProperty,
        HasYProperty {

    public ImageView() {
    }

    public ImageView(String imageUrl) {
        setImageUrl(imageUrl);
    }

    public ImageView(Image image) {
        setImage(image);
    }


    private final Property<Image> imageProperty = new SimpleObjectProperty<Image>() {
        @Override
        protected void invalidated() {
            invalidateWidthHeight();
            //impl_geomChanged();
            //impl_markDirty(DirtyBits.NODE_CONTENTS);
        }
    };
    @Override
    public Property<Image> imageProperty() {
        return imageProperty;
    }

    private final Property<String> imageUrlProperty = new SimpleObjectProperty<String>() {
        @Override
        protected void invalidated() {
            setImage(new Image(getImageUrl()));
        }
    };
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

    private double destWidth, destHeight;

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        recomputeWidthHeight();

        bounds = bounds.deriveWithNewBounds(getX().floatValue(), getY().floatValue(), 0, (float) (getX() + destWidth), (float) (getY() + destHeight), 0);
        bounds = tx.transform(bounds, bounds);
        return bounds;
    }

    private boolean validWH;

    private void invalidateWidthHeight() {
        validWH = false;
    }

    private void recomputeWidthHeight() {
        if (validWH)
            return;
        Image localImage = getImage();
        //Rectangle2D localViewport = getViewport();

        double w = 0;
        double h = 0;
        /*if (localViewport != null && localViewport.getWidth() > 0 && localViewport.getHeight() > 0) {
            w = localViewport.getWidth();
            h = localViewport.getHeight();
        } else*/ if (localImage != null) {
            w = localImage.getWidth();
            h = localImage.getHeight();
        }

        double localFitWidth = getFitWidth();
        double localFitHeight = getFitHeight();

        /*if (isPreserveRatio() && w > 0 && h > 0 && (localFitWidth > 0 || localFitHeight > 0)) {
            if (localFitWidth <= 0 || (localFitHeight > 0 && localFitWidth * h > localFitHeight * w)) {
                w = w * localFitHeight / h;
                h = localFitHeight;
            } else {
                h = h * localFitWidth / w;
                w = localFitWidth;
            }
        } else*/ {
            if (localFitWidth > 0f) {
                w = localFitWidth;
            }
            if (localFitHeight > 0f) {
                h = localFitHeight;
            }
        }

        // Store these values for use later in impl_computeContains() to support
        // Node.contains().
        destWidth = w;
        destHeight = h;

        validWH = true;
    }
}
