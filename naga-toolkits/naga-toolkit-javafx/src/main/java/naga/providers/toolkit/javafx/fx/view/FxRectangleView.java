package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.shape.Rectangle;
import naga.toolkit.fx.spi.view.base.RectangleViewBase;
import naga.toolkit.fx.spi.view.base.RectangleViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxRectangleView
        extends FxShapeView<javafx.scene.shape.Rectangle, Rectangle, RectangleViewBase, RectangleViewMixin>
        implements RectangleViewMixin {

    public FxRectangleView() {
        super(new RectangleViewBase());
    }

    @Override
    javafx.scene.shape.Rectangle createFxNode() {
        return new javafx.scene.shape.Rectangle();
    }

    @Override
    public void updateX(Double x) {
        getFxNode().setX(x);
    }

    @Override
    public void updateY(Double y) {
        getFxNode().setY(y);
    }

    @Override
    public void updateWidth(Double width) {
        getFxNode().setWidth(width);
    }

    @Override
    public void updateHeight(Double height) {
        getFxNode().setHeight(height);
    }

    @Override
    public void updateArcWidth(Double arcWidth) {
        getFxNode().setArcWidth(arcWidth);
    }

    @Override
    public void updateArcHeight(Double arcHeight) {
        getFxNode().setArcHeight(arcHeight);
    }
}
