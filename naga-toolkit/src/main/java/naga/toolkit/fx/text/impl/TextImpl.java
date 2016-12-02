package naga.toolkit.fx.text.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.geom.BaseBounds;
import naga.toolkit.fx.geom.BoxBounds;
import naga.toolkit.fx.geom.transform.BaseTransform;
import naga.toolkit.fx.shape.impl.ShapeImpl;
import naga.toolkit.fx.text.Font;
import naga.toolkit.fx.text.Text;
import naga.toolkit.fx.text.TextAlignment;
import naga.toolkit.fx.geometry.VPos;

/**
 * @author Bruno Salmon
 */
public class TextImpl extends ShapeImpl implements Text {

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
        System.out.println("Warning: TextImpl.impl_computeGeomBounds() not implemented");
        return new BoxBounds();
        //throw new UnsupportedOperationException("TextImpl.impl_computeGeomBounds() not implemented");
    }
}
