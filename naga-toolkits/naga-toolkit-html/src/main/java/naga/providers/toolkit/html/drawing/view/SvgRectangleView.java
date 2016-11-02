package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import javafx.beans.property.Property;
import naga.providers.toolkit.html.drawing.SvgDrawing;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.view.implbase.RectangleViewImplBase;

/**
 * @author Bruno Salmon
 */
public class SvgRectangleView extends RectangleViewImplBase implements SvgDrawableView {

    private final SvgShapeUpdater svgShapeUpdater = new SvgShapeUpdater(SvgUtil.createSvgRectangle());

    @Override
    public boolean update(SvgDrawing svgDrawingNode, Property changedProperty) {
        Rectangle r = drawable;
        return svgShapeUpdater.update(r, changedProperty, svgDrawingNode)
            || svgShapeUpdater.updateSvgDoubleAttribute("x", r.xProperty(), changedProperty)
            || svgShapeUpdater.updateSvgDoubleAttribute("y", r.yProperty(), changedProperty)
            || svgShapeUpdater.updateSvgDoubleAttribute("width", r.widthProperty(), changedProperty)
            || svgShapeUpdater.updateSvgDoubleAttribute("height", r.heightProperty(), changedProperty)
            || svgShapeUpdater.updateSvgDoubleAttribute("rx", r.arcWidthProperty(), changedProperty)
            || svgShapeUpdater.updateSvgDoubleAttribute("ry", r.arcHeightProperty(), changedProperty);
    }

    @Override
    public Element getElement() {
        return svgShapeUpdater.getSvgShapeElement();
    }
}
