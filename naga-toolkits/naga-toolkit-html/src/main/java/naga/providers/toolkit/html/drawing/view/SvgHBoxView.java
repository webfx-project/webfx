package naga.providers.toolkit.html.drawing.view;

import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.drawing.layout.HBox;
import naga.toolkit.drawing.spi.view.base.HBoxViewBase;
import naga.toolkit.drawing.spi.view.base.HBoxViewMixin;

/**
 * @author Bruno Salmon
 */
public class SvgHBoxView
        extends SvgNodeView<HBox, HBoxViewBase, HBoxViewMixin>
        implements HBoxViewMixin {

    public SvgHBoxView() {
        super(new HBoxViewBase(), SvgUtil.createSvgGroup());
    }
}
