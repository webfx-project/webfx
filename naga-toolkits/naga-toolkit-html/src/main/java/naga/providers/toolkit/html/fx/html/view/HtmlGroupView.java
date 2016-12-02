package naga.providers.toolkit.html.fx.html.view;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.spi.view.base.GroupViewBase;
import naga.toolkit.fx.spi.view.base.GroupViewMixin;

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
