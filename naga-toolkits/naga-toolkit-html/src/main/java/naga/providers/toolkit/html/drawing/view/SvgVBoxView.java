package naga.providers.toolkit.html.drawing.view;

import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.drawing.shapes.VBox;
import naga.toolkit.drawing.spi.view.base.VBoxViewBase;
import naga.toolkit.drawing.spi.view.base.VBoxViewMixin;

/**
 * @author Bruno Salmon
 */
public class SvgVBoxView
        extends SvgNodeView<VBox, VBoxViewBase, VBoxViewMixin>
        implements VBoxViewMixin {

    public SvgVBoxView() {
        super(new VBoxViewBase(), SvgUtil.createSvgGroup());
    }
}
