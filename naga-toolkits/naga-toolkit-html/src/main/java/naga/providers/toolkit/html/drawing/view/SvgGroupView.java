package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.providers.toolkit.html.drawing.SvgDrawing;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.spi.view.implbase.GroupViewImplBase;

/**
 * @author Bruno Salmon
 */
public class SvgGroupView extends GroupViewImplBase implements SvgDrawableView {

    private final SvgShapeElementUpdater svgShapeElementUpdater = new SvgShapeElementUpdater(SvgUtil.createSvgGroup());

    @Override
    public void syncSvgPropertiesFromDrawable(SvgDrawing svgDrawing) {
    }

    @Override
    public Element getSvgDrawableElement() {
        return svgShapeElementUpdater.getSvgShapeElement();
    }
}
