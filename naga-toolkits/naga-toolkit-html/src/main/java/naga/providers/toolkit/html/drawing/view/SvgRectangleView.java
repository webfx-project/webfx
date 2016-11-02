package naga.providers.toolkit.html.drawing.view;

import javafx.beans.property.Property;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.view.base.RectangleViewBase;
import naga.toolkit.drawing.spi.view.mixin.RectangleViewMixin;

/**
 * @author Bruno Salmon
 */
public class SvgRectangleView extends SvgShapeView<Rectangle, RectangleViewBase> implements RectangleViewMixin {

    public SvgRectangleView() {
        super(SvgUtil.createSvgRectangle());
    }

    private final RectangleViewBase base = new RectangleViewBase();
    @Override
    public RectangleViewBase getDrawableViewBase() {
        return base;
    }

    @Override
    public boolean update(Property changedProperty) {
        Rectangle r = getDrawableViewBase().getDrawable();
        return super.update(changedProperty)
            || updateSvgDoubleAttribute("x", r.xProperty(), changedProperty)
            || updateSvgDoubleAttribute("y", r.yProperty(), changedProperty)
            || updateSvgDoubleAttribute("width", r.widthProperty(), changedProperty)
            || updateSvgDoubleAttribute("height", r.heightProperty(), changedProperty)
            || updateSvgDoubleAttribute("rx", r.arcWidthProperty(), changedProperty)
            || updateSvgDoubleAttribute("ry", r.arcHeightProperty(), changedProperty);
    }
}
