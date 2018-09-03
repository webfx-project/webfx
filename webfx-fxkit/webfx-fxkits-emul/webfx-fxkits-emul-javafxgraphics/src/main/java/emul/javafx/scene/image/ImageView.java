package emul.javafx.scene.image;

import emul.com.sun.javafx.geom.BaseBounds;
import emul.com.sun.javafx.geom.transform.BaseTransform;
import emul.javafx.beans.property.*;
import emul.javafx.scene.Node;
import webfx.fxkits.core.properties.markers.*;

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
            impl_geomChanged();
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

    private final Property<Double> fitWidthProperty = new SimpleObjectProperty<Double>(0d) {
        @Override
        protected void invalidated() {
            invalidateWidthHeight();
            impl_geomChanged();
        }
    };
    @Override
    public Property<Double> fitWidthProperty() {
        return fitWidthProperty;
    }

    private final Property<Double> fitHeightProperty = new SimpleObjectProperty<Double>(0d) {
        @Override
        protected void invalidated() {
            invalidateWidthHeight();
            impl_geomChanged();
        }
    };
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
     * Indicates whether to preserve the aspect ratio of the source image when
     * scaling to fit the image within the fitting bounding box.
     * <p/>
     * If set to {@code true}, it affects the dimensions of this
     * {@code ImageView} in the following way *
     * <ul>
     * <li>If only {@code fitWidth} is set, height is scaled to preserve ratio
     * <li>If only {@code fitHeight} is set, width is scaled to preserve ratio
     * <li>If both are set, they both may be scaled to get the best fit in a
     * width by height rectangle while preserving the original aspect ratio
     * </ul>
     *
     * If unset or set to {@code false}, it affects the dimensions of this
     * {@code ImageView} in the following way *
     * <ul>
     * <li>If only {@code fitWidth} is set, image's view width is scaled to
     * match and height is unchanged;
     * <li>If only {@code fitHeight} is set, image's view height is scaled to
     * match and height is unchanged;
     * <li>If both are set, the image view is scaled to match both.
     * </ul>
     * </p>
     * Note that the dimensions of this node as reported by the node's bounds
     * will be equal to the size of the scaled image and is guaranteed to be
     * contained within {@code fitWidth x fitHeight} bonding box.
     *
     * @defaultValue false
     */
    private BooleanProperty preserveRatio;


    public final void setPreserveRatio(boolean value) {
        preserveRatioProperty().set(value);
    }

    public final boolean isPreserveRatio() {
        return preserveRatio == null ? false : preserveRatio.get();
    }

    public final BooleanProperty preserveRatioProperty() {
        if (preserveRatio == null) {
            preserveRatio = new BooleanPropertyBase() {

                @Override
                protected void invalidated() {
                    invalidateWidthHeight();
                    //impl_markDirty(DirtyBits.NODE_VIEWPORT);
                    impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return ImageView.this;
                }

                @Override
                public String getName() {
                    return "preserveRatio";
                }
            };
        }
        return preserveRatio;
    }
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

    @Override
    protected void onPeerSizeChanged() {
        invalidateWidthHeight();
        super.onPeerSizeChanged();
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

        if (isPreserveRatio() && w > 0 && h > 0 && (localFitWidth > 0 || localFitHeight > 0)) {
            if (localFitWidth <= 0 || (localFitHeight > 0 && localFitWidth * h > localFitHeight * w)) {
                w = w * localFitHeight / h;
                h = localFitHeight;
            } else {
                h = h * localFitWidth / w;
                w = localFitWidth;
            }
        } else {
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
