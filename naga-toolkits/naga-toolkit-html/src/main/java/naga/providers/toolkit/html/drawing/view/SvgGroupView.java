package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import javafx.beans.property.Property;
import naga.providers.toolkit.html.drawing.SvgDrawing;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.spi.view.implbase.GroupViewImplBase;

/**
 * @author Bruno Salmon
 */
public class SvgGroupView extends GroupViewImplBase implements SvgDrawableView {

    private final Element element = SvgUtil.createSvgGroup();

    @Override
    public boolean update(SvgDrawing svgDrawing, Property changedProperty) {
        return false;
    }

    @Override
    public Element getElement() {
        return element;
    }
}
