package naga.toolkit.drawing.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.RectangleView;

/**
 * @author Bruno Salmon
 */
public class RectangleViewBase
        extends ShapeViewBase<Rectangle, RectangleViewBase, RectangleViewMixin>
        implements RectangleView {

    @Override
    public void bind(Rectangle r, DrawingRequester drawingRequester) {
        super.bind(r, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester,
                r.xProperty(),
                r.yProperty(),
                r.widthProperty(),
                r.heightProperty(),
                r.arcWidthProperty(),
                r.arcHeightProperty());
    }

    @Override
    public boolean update(Property changedProperty) {
        Rectangle r = drawable;
        return super.update(changedProperty)
                || updateProperty(r.xProperty(), changedProperty, mixin::updateX)
                || updateProperty(r.yProperty(), changedProperty, mixin::updateY)
                || updateProperty(r.widthProperty(), changedProperty, mixin::updateWidth)
                || updateProperty(r.heightProperty(), changedProperty, mixin::updateHeight)
                || updateProperty(r.arcWidthProperty(), changedProperty, mixin::updateArcWidth)
                || updateProperty(r.arcHeightProperty(), changedProperty, mixin::updateArcHeight);
    }
}
