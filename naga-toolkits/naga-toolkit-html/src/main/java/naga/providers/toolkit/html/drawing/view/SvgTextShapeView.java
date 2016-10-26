package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.providers.toolkit.html.drawing.SvgDrawingNode;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.spi.view.implbase.TextShapeViewImplBase;

/**
 * @author Bruno Salmon
 */
public class SvgTextShapeView extends TextShapeViewImplBase implements SvgShapeView {

    private final SvgShapeElementUpdater svgShapeElementUpdater = new SvgShapeElementUpdater(SvgUtil.createSvgText());

    @Override
    public void syncSvgPropertiesFromShape(SvgDrawingNode svgDrawingNode) {
        Element svgElement = svgShapeElementUpdater.syncSvgPropertiesFromShape(shape, svgDrawingNode);
        svgElement.textContent = shape.getText();
        svgElement.setAttribute("x", shape.getX());
        svgElement.setAttribute("y", shape.getY());
        svgElement.setAttribute("font-family", "Serif");
        svgElement.setAttribute("font-size", 100);
    }

    @Override
    public Element getSvgShapeElement() {
        return svgShapeElementUpdater.getSvgShapeElement();
    }

}
