package naga.providers.toolkit.html.drawing.view;

import javafx.beans.property.Property;
import naga.commons.util.Numbers;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.shapes.TextAlignment;
import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.spi.view.base.TextShapeViewBase;
import naga.toolkit.drawing.spi.view.mixin.TextShapeViewMixin;

/**
 * @author Bruno Salmon
 */
public class SvgTextShapeView extends SvgShapeView<TextShape, TextShapeViewBase> implements TextShapeViewMixin {

    public SvgTextShapeView() {
        super(SvgUtil.createSvgText());
    }

    private final TextShapeViewBase base = new TextShapeViewBase();
    @Override
    public TextShapeViewBase getDrawableViewBase() {
        return base;
    }

    @Override
    public boolean update(Property changedProperty) {
        TextShape ts = getDrawableViewBase().getDrawable();
        return super.update(changedProperty)
            || updateSvgTextContent(ts.textProperty(), changedProperty)
            || updateXAttribute(changedProperty)
            || updateSvgDoubleAttribute("y", ts.yProperty(), changedProperty)
            || updateSvgDoubleAttribute("width", ts.wrappingWidthProperty(), changedProperty)
            || updateSvgStringAttribute("text-anchor", ts.textAlignmentProperty(), SvgShapeView::textAlignmentToSvgTextAnchor, changedProperty)
            || updateSvgStringAttribute("dominant-baseline", ts.textOriginProperty(), SvgShapeView::vPosToSvgAlignmentBaseLine, changedProperty)
            || updateSvgFontAttributes(ts.fontProperty(), changedProperty);
    }

    boolean updateXAttribute(Property changedProperty) {
        TextShape ts = getDrawableViewBase().getDrawable();
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
            setSvgAttribute("x", x);
        }
        return hitProperty;
    }
}
