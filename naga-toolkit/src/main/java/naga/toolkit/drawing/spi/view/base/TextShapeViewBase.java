package naga.toolkit.drawing.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.TextShapeView;

/**
 * @author Bruno Salmon
 */
public class TextShapeViewBase
        extends ShapeViewBase<TextShape, TextShapeViewBase, TextShapeViewMixin>
        implements TextShapeView {

    @Override
    public void bind(TextShape ts, DrawingRequester drawingRequester) {
        super.bind(ts, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester,
                ts.textProperty(),
                ts.textOriginProperty(),
                ts.wrappingWidthProperty(),
                ts.textAlignmentProperty(),
                ts.fontProperty());
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        TextShape ts = drawable;
        return super.updateProperty(changedProperty)
                || updateProperty(ts.textProperty(), changedProperty, mixin::updateText)
                || updateProperty(ts.xProperty(), changedProperty, mixin::updateX)
                || updateProperty(ts.yProperty(), changedProperty, mixin::updateY)
                || updateProperty(ts.wrappingWidthProperty(), changedProperty, mixin::updateWrappingWidth)
                || updateProperty(ts.textAlignmentProperty(), changedProperty, mixin::updateTextAlignment)
                || updateProperty(ts.textOriginProperty(), changedProperty, mixin::updateTextOrigin)
                || updateProperty(ts.fontProperty(), changedProperty, mixin::updateFont);
    }
}
