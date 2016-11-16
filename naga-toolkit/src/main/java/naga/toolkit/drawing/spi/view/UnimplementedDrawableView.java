package naga.toolkit.drawing.spi.view;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.Point2D;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.impl.canvas.CanvasDrawableView;

/**
 * @author Bruno Salmon
 */
public class UnimplementedDrawableView implements CanvasDrawableView<Drawable, Object> {

    @Override
    public void bind(Drawable drawable, DrawingRequester drawingRequester) {
    }

    @Override
    public void unbind() {
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return false;
    }

    @Override
    public boolean updateList(ObservableList changedList) {
        return false;
    }

    @Override
    public void prepareCanvasContext(Object c) {
    }

    @Override
    public void paint(Object c) {
    }

    @Override
    public boolean containsPoint(Point2D point) {
        return false;
    }
}
