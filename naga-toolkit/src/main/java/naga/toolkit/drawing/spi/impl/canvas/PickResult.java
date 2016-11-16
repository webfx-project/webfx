package naga.toolkit.drawing.spi.impl.canvas;

import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.Point2D;
import naga.toolkit.drawing.spi.view.DrawableView;

/**
 * @author Bruno Salmon
 */
public class PickResult {

    private final Drawable drawable;
    private final DrawableView drawableView;
    private final Point2D drawableLocalPoint; // expressed in the drawable coordinates space - ie without transformation

    PickResult(Drawable drawable, DrawableView drawableView, Point2D drawableLocalPoint) {
        this.drawable = drawable;
        this.drawableView = drawableView;
        this.drawableLocalPoint = drawableLocalPoint;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public DrawableView getDrawableView() {
        return drawableView;
    }

    public Point2D getDrawableLocalPoint() {
        return drawableLocalPoint;
    }
}
