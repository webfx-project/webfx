package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import javafx.beans.property.Property;
import naga.commons.util.Numbers;
import naga.providers.toolkit.html.drawing.SvgDrawing;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.shapes.TextAlignment;
import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.spi.view.implbase.TextShapeViewImplBase;

/**
 * @author Bruno Salmon
 */
public class SvgTextShapeView extends TextShapeViewImplBase implements SvgDrawableView {

    private final SvgShapeUpdater svgShapeUpdater = new SvgShapeUpdater(SvgUtil.createSvgText());

    @Override
    public boolean update(SvgDrawing svgDrawing, Property changedProperty) {
        TextShape ts = drawable;
        return svgShapeUpdater.update(ts, changedProperty, svgDrawing)
            || svgShapeUpdater.updateSvgTextContent(ts.textProperty(), changedProperty)
            || updateXAttribute(changedProperty)
            || svgShapeUpdater.updateSvgDoubleAttribute("y", ts.yProperty(), changedProperty)
            || svgShapeUpdater.updateSvgDoubleAttribute("width", ts.wrappingWidthProperty(), changedProperty)
            || svgShapeUpdater.updateSvgStringAttribute("text-anchor", ts.textAlignmentProperty(), SvgShapeUpdater::textAlignmentToSvgTextAnchor, changedProperty)
            || svgShapeUpdater.updateSvgStringAttribute("dominant-baseline", ts.textOriginProperty(), SvgShapeUpdater::vPosToSvgAlignmentBaseLine, changedProperty)
            || svgShapeUpdater.updateSvgFontAttributes(ts.fontProperty(), changedProperty);
    }

    boolean updateXAttribute(Property changedProperty) {
        TextShape ts = drawable;
        boolean hitProperty = changedProperty == ts.xProperty();
        if (hitProperty || changedProperty == null || changedProperty == ts.wrappingWidthProperty() || changedProperty == ts.textAlignmentProperty()) {
            double x = Numbers.doubleValue(ts.getX());
            double wrappingWidth = Numbers.doubleValue(ts.getWrappingWidth());
            // Partial implementation that doesn't support multi-line text wrapping. TODO: Add multi-line wrapping support
            if (wrappingWidth > 0) {
                TextAlignment textAlignment = ts.getTextAlignment();
                if (textAlignment == TextAlignment.CENTER)
                    x = x + wrappingWidth / 2;
                else if (textAlignment == TextAlignment.RIGHT)
                    x = x + wrappingWidth;
            }
            svgShapeUpdater.setSvgAttribute("x", x);
        }
        return hitProperty;
    }

    @Override
    public Element getElement() {
        return svgShapeUpdater.getSvgShapeElement();
    }

}
