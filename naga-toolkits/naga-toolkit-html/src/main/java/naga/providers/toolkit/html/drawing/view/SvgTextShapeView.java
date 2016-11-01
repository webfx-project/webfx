package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.commons.util.Numbers;
import naga.providers.toolkit.html.drawing.SvgDrawingNode;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.shapes.Font;
import naga.toolkit.drawing.shapes.FontPosture;
import naga.toolkit.drawing.shapes.TextAlignment;
import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.spi.view.implbase.TextShapeViewImplBase;

/**
 * @author Bruno Salmon
 */
public class SvgTextShapeView extends TextShapeViewImplBase implements SvgDrawableView {

    private final SvgShapeElementUpdater svgShapeElementUpdater = new SvgShapeElementUpdater(SvgUtil.createSvgText());

    @Override
    public void syncSvgPropertiesFromDrawable(SvgDrawingNode svgDrawingNode) {
        TextShape ts = drawable;
        Element svgElement = svgShapeElementUpdater.syncSvgFromCommonShapeProperties(ts, svgDrawingNode);
        svgElement.textContent = ts.getText();
        double x = Numbers.doubleValue(ts.getX());
        double wrappingWidth = Numbers.doubleValue(ts.getWrappingWidth());
        TextAlignment textAlignment = ts.getTextAlignment();
        // Partial implementation that doesn't support multi-line text wrapping. TODO: Add multi-line wrapping support
        if (wrappingWidth > 0) {
            if (textAlignment == TextAlignment.CENTER)
                x = x + wrappingWidth / 2;
            else if (textAlignment == TextAlignment.RIGHT)
                x = x + wrappingWidth;
        }
        svgShapeElementUpdater.setSvgAttribute("x", x);
        svgShapeElementUpdater.setSvgAttribute("y", ts.getY());
        svgShapeElementUpdater.setSvgAttribute("width", wrappingWidth);
        svgShapeElementUpdater.setSvgAttribute("text-anchor", SvgShapeElementUpdater.textAlignmentToSvgTextAnchor(textAlignment));
        svgShapeElementUpdater.setSvgAttribute("dominant-baseline", SvgShapeElementUpdater.vPosToSvgAlignmentBaseLine(ts.getTextOrigin()));
        Font font = ts.getFont();
        svgShapeElementUpdater.setSvgAttribute("font-family", font.getFamily());
        svgShapeElementUpdater.setSvgAttribute("font-style", font.getPosture() == FontPosture.ITALIC ? "italic" : "normal", "normal");
        svgShapeElementUpdater.setSvgAttribute("font-weight", font.getWeight() == null ? 0 : font.getWeight().getWeight(), 0);
        svgShapeElementUpdater.setSvgAttribute("font-size", font.getSize());
    }

    @Override
    public Element getSvgDrawableElement() {
        return svgShapeElementUpdater.getSvgShapeElement();
    }

}
