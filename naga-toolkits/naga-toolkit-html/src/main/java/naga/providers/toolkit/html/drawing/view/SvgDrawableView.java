package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.providers.toolkit.html.drawing.SvgDrawing;

/**
 * @author Bruno Salmon
 */
public interface SvgDrawableView {

    void syncSvgPropertiesFromDrawable(SvgDrawing svgDrawing);

    Element getSvgDrawableElement();

}
