package naga.providers.toolkit.html.fx.svg.view;

import elemental2.Element;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.spi.viewer.base.GroupViewerBase;
import naga.toolkit.fx.spi.viewer.base.GroupViewerMixin;

/**
 * @author Bruno Salmon
 */
public class SvgGroupViewer
        <N extends Group, NV extends GroupViewerBase<N, NV, NM>, NM extends GroupViewerMixin<N, NV, NM>>

        extends SvgNodeViewer<N, NV, NM>
        implements GroupViewerMixin<N, NV, NM> {

    public SvgGroupViewer() {
        this((NV) new GroupViewerBase(), SvgUtil.createSvgGroup());
    }

    public SvgGroupViewer(NV base, Element element) {
        super(base, element);
    }
}
