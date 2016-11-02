package naga.providers.toolkit.html.drawing.view;

import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.shapes.Group;
import naga.toolkit.drawing.spi.view.base.GroupViewBase;
import naga.toolkit.drawing.spi.view.base.GroupViewMixin;

/**
 * @author Bruno Salmon
 */
public class SvgGroupView
        extends SvgDrawableView<Group, GroupViewBase, GroupViewMixin>
        implements GroupViewMixin {

    public SvgGroupView() {
        super(new GroupViewBase(), SvgUtil.createSvgGroup());
    }
}
