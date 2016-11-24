package naga.toolkit.drawing.shapes.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.drawing.geom.BaseBounds;
import naga.toolkit.drawing.geom.transform.BaseTransform;
import naga.toolkit.drawing.shapes.Font;
import naga.toolkit.drawing.shapes.TextAlignment;
import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.shapes.VPos;

/**
 * @author Bruno Salmon
 */
public class TextShapeImpl extends ShapeImpl implements TextShape {

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

    private final Property<Double> wrappingWidthProperty = new SimpleObjectProperty<>();
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

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        throw new UnsupportedOperationException("TextShapeImpl.impl_computeGeomBounds() not implemented");
    }
}
