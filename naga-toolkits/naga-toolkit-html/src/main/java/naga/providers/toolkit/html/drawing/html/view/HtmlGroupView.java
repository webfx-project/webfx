package naga.providers.toolkit.html.drawing.html.view;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.drawing.scene.Group;
import naga.toolkit.drawing.spi.view.base.GroupViewBase;
import naga.toolkit.drawing.spi.view.base.GroupViewMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlGroupView
        extends HtmlNodeView<Group, GroupViewBase, GroupViewMixin>
        implements GroupViewMixin {

    public HtmlGroupView() {
        super(new GroupViewBase(), HtmlUtil.createDivElement());
    }
}
