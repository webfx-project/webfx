package naga.providers.toolkit.html.fx.svg.view;

import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.spi.viewer.base.GroupViewerBase;
import naga.toolkit.fx.spi.viewer.base.GroupViewerMixin;

/**
 * @author Bruno Salmon
 */
public class SvgGroupViewer
        extends SvgNodeViewer<Group, GroupViewerBase, GroupViewerMixin>
        implements GroupViewerMixin {

    public SvgGroupViewer() {
        super(new GroupViewerBase(), SvgUtil.createSvgGroup());
    }
}
