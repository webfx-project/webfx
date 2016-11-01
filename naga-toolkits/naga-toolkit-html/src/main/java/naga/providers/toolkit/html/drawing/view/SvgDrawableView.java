package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.providers.toolkit.html.drawing.SvgDrawingNode;

/**
 * @author Bruno Salmon
 */
public interface SvgDrawableView {

    void syncSvgPropertiesFromDrawable(SvgDrawingNode svgDrawingNode);

    Element getSvgDrawableElement();

}
