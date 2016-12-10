package naga.providers.toolkit.html.fx.html.viewer;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.spi.viewer.base.GroupViewerBase;
import naga.toolkit.fx.spi.viewer.base.GroupViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlGroupViewer
        extends HtmlNodeViewer<Group, GroupViewerBase, GroupViewerMixin>
        implements GroupViewerMixin {

    public HtmlGroupViewer() {
        super(new GroupViewerBase(), HtmlUtil.createDivElement());
    }
}
