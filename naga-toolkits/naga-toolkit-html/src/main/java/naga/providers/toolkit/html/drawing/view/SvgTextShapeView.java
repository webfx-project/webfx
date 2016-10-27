package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.providers.toolkit.html.drawing.SvgDrawingNode;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.shapes.Font;
import naga.toolkit.drawing.shapes.FontPosture;
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
        svgElement.setAttribute("x", shape.getX());
        svgElement.setAttribute("y", shape.getY());
        Font font = shape.getFont();
        svgElement.setAttribute("font-family", font.getFamily());
        svgElement.setAttribute("font-style", font.getPosture() == FontPosture.ITALIC ? "italic" : null);
        svgElement.setAttribute("font-weight", font.getWeight() == null ? 0 : font.getWeight().getWeight());
        svgElement.setAttribute("font-size", font.getSize());
    }

    @Override
    public Element getSvgShapeElement() {
        return svgShapeElementUpdater.getSvgShapeElement();
    }

}
