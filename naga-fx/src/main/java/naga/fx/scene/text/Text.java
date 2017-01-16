package naga.fx.scene.text;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.geometry.VPos;
import naga.fx.scene.shape.Shape;
import naga.fx.properties.markers.*;

/**
 * @author Bruno Salmon
 */
public class Text extends Shape implements
        HasXProperty,
        HasYProperty,
        HasTextProperty,
        HasTextOriginProperty,
        HasTextAlignmentProperty,
        HasWrappingWidthProperty,
        HasFontProperty {

    public Text() {
    }

    public Text(String text) {
        setText(text);
    }

    private final Property<Double> xProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> xProperty() {
        return xProperty;
    }

    private final Property<VPos> textOriginProperty = new SimpleObjectProperty<>(VPos.BASELINE);
    @Override
    public Property<VPos> textOriginProperty() {
        return textOriginProperty;
    }

    private final Property<TextAlignment> textAlignmentProperty = new SimpleObjectProperty<>(TextAlignment.LEFT);
    @Override
    public Property<TextAlignment> textAlignmentProperty() {
        return textAlignmentProperty;
    }

    private final Property<Double> wrappingWidthProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> wrappingWidthProperty() {
        return wrappingWidthProperty;
    }

    private final Property<Double> yProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> yProperty() {
        return yProperty;
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

    private final Property<Font> fontProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Font> fontProperty() {
        return fontProperty;
    }

    private static final TextBoundsType DEFAULT_BOUNDS_TYPE = TextBoundsType.LOGICAL;

    /**
     * Determines how the bounds of the text node are calculated.
     * Logical bounds is a more appropriate default for text than
     * the visual bounds. See {@code TextBoundsType} for more information.
     *
     * @defaultValue TextBoundsType.LOGICAL
     */
    private Property<TextBoundsType> boundsType;

    public final void setBoundsType(TextBoundsType value) {
        boundsTypeProperty().setValue(value);
    }

    public final TextBoundsType getBoundsType() {
        return boundsType == null ?
                DEFAULT_BOUNDS_TYPE : boundsTypeProperty().getValue();
    }

    public final Property<TextBoundsType> boundsTypeProperty() {
        if (boundsType == null) {
            boundsType =
                    new SimpleObjectProperty<>(DEFAULT_BOUNDS_TYPE)/* {
                        @Override public Object getBean() { return Text.this; }
                        @Override public String getName() { return "boundsType"; }
                        @Override public CssMetaData<Text,TextBoundsType> getCssMetaData() {
                            return StyleableProperties.BOUNDS_TYPE;
                        }
                        @Override public void invalidated() {
                            TextLayout layout = getTextLayout();
                            int type = 0;
                            if (boundsType.get() == TextBoundsType.LOGICAL_VERTICAL_CENTER) {
                                type |= TextLayout.BOUNDS_CENTER;
                            }
                            if (layout.setBoundsType(type)) {
                                needsTextLayout();
                            } else {
                                impl_geomChanged();
                            }
                        }
                    }*/;
        }
        return boundsType;
    }

    private static boolean warned;
    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        if (!warned) {
            System.out.println("Warning: Text.impl_computeGeomBounds() not implemented");
            warned = true;
        }
        return new BoxBounds();
        //throw new UnsupportedOperationException("Text.impl_computeGeomBounds() not implemented");
    }
}
