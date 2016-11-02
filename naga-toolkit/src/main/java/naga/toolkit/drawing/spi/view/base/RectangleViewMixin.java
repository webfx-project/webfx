package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.view.RectangleView;

/**
 * @author Bruno Salmon
 */
public interface RectangleViewMixin
        extends RectangleView,
                ShapeViewMixin<Rectangle, RectangleViewBase, RectangleViewMixin> {

    void updateX(Double x);

    void updateY(Double y);

    void updateWidth(Double width);

    void updateHeight(Double height);

    void updateArcWidth(Double arcWidth);

    void updateArcHeight(Double arcHeight);
}
