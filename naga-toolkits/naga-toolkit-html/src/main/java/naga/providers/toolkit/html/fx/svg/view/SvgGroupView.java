package naga.providers.toolkit.html.fx.svg.view;

import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.spi.view.base.GroupViewBase;
import naga.toolkit.fx.spi.view.base.GroupViewMixin;

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
