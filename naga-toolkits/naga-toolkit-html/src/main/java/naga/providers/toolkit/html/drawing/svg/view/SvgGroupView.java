package naga.providers.toolkit.html.drawing.svg.view;

import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.drawing.scene.Group;
import naga.toolkit.drawing.spi.view.base.GroupViewBase;
import naga.toolkit.drawing.spi.view.base.GroupViewMixin;

/**
 * @author Bruno Salmon
 */
public class SvgGroupView
        extends SvgNodeView<Group, GroupViewBase, GroupViewMixin>
        implements GroupViewMixin {

    public SvgGroupView() {
        super(new GroupViewBase(), SvgUtil.createSvgGroup());
    }
}
