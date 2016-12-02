package naga.toolkit.fx.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.shape.Circle;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.view.CircleView;

/**
 * @author Bruno Salmon
 */
public class CircleViewBase
        extends ShapeViewBase<Circle, CircleViewBase, CircleViewMixin>
        implements CircleView {

    @Override
    public void bind(Circle c, DrawingRequester drawingRequester) {
        super.bind(c, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester,
                c.centerXProperty(),
                c.centerYProperty(),
                c.radiusProperty());
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        Circle c = node;
        return super.updateProperty(changedProperty)
                || updateProperty(c.centerXProperty(), changedProperty, mixin::updateCenterX)
                || updateProperty(c.centerYProperty(), changedProperty, mixin::updateCenterY)
                || updateProperty(c.radiusProperty(), changedProperty, mixin::updateRadius);
    }
}
