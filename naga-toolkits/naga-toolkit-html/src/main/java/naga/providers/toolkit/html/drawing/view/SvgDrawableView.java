package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import javafx.beans.property.Property;
import naga.providers.toolkit.html.drawing.SvgDrawing;

/**
 * @author Bruno Salmon
 */
public interface SvgDrawableView {

    boolean update(SvgDrawing svgDrawing, Property changedProperty);

    Element getElement();

}
