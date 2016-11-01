package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.providers.toolkit.html.drawing.SvgDrawingNode;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.spi.view.implbase.GroupViewImplBase;

/**
 * @author Bruno Salmon
 */
public class SvgGroupView extends GroupViewImplBase implements SvgDrawableView {

    private final SvgShapeElementUpdater svgShapeElementUpdater = new SvgShapeElementUpdater(SvgUtil.createSvgGroup());

    @Override
    public void syncSvgPropertiesFromDrawable(SvgDrawingNode svgDrawingNode) {
    }

    @Override
    public Element getSvgDrawableElement() {
        return svgShapeElementUpdater.getSvgShapeElement();
    }
}
