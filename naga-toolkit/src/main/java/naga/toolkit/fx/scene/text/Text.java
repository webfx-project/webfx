package naga.toolkit.fx.scene.text;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.geom.BaseBounds;
import naga.toolkit.fx.geom.BoxBounds;
import naga.toolkit.fx.geom.transform.BaseTransform;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.scene.shape.Shape;
import naga.toolkit.properties.markers.*;

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
