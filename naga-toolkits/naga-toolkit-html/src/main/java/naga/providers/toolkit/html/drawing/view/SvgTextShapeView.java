package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.commons.util.Numbers;
import naga.providers.toolkit.html.drawing.SvgDrawingNode;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.shapes.Font;
import naga.toolkit.drawing.shapes.FontPosture;
import naga.toolkit.drawing.shapes.TextAlignment;
import naga.toolkit.drawing.spi.view.implbase.TextShapeViewImplBase;

/**
 * @author Bruno Salmon
 */
public class SvgTextShapeView extends TextShapeViewImplBase implements SvgShapeView {

    private final SvgShapeElementUpdater svgShapeElementUpdater = new SvgShapeElementUpdater(SvgUtil.createSvgText());

    @Override
    public void syncSvgPropertiesFromShape(SvgDrawingNode svgDrawingNode) {
        Element svgElement = svgShapeElementUpdater.syncSvgFromCommonShapeProperties(shape, svgDrawingNode);
        svgElement.textContent = shape.getText();
        double x = Numbers.doubleValue(shape.getX());
        double wrappingWidth = Numbers.doubleValue(shape.getWrappingWidth());
        TextAlignment textAlignment = shape.getTextAlignment();
        // Partial implementation that doesn't support multi-line text wrapping. TODO: Add multi-line wrapping support
        if (wrappingWidth > 0) {
            if (textAlignment == TextAlignment.CENTER)
                x = x + wrappingWidth / 2;
            else if (textAlignment == TextAlignment.RIGHT)
                x = x + wrappingWidth;
        }
        svgShapeElementUpdater.setSvgAttribute("x", x);
        svgShapeElementUpdater.setSvgAttribute("y", shape.getY());
        svgShapeElementUpdater.setSvgAttribute("width", wrappingWidth);
        svgShapeElementUpdater.setSvgAttribute("text-anchor", SvgShapeElementUpdater.textAlignmentToSvgTextAnchor(textAlignment));
        svgShapeElementUpdater.setSvgAttribute("dominant-baseline", SvgShapeElementUpdater.vPosToSvgAlignmentBaseLine(shape.getTextOrigin()));
        Font font = shape.getFont();
        svgShapeElementUpdater.setSvgAttribute("font-family", font.getFamily());
        svgShapeElementUpdater.setSvgAttribute("font-style", font.getPosture() == FontPosture.ITALIC ? "italic" : "normal", "normal");
        svgShapeElementUpdater.setSvgAttribute("font-weight", font.getWeight() == null ? 0 : font.getWeight().getWeight(), 0);
        svgShapeElementUpdater.setSvgAttribute("font-size", font.getSize());
    }

    @Override
    public Element getSvgShapeElement() {
        return svgShapeElementUpdater.getSvgShapeElement();
    }

}
