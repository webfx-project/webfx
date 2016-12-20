package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.HTMLElement;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.spi.viewer.base.GroupViewerBase;
import naga.toolkit.fx.spi.viewer.base.GroupViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlGroupViewer
        <N extends Group, NV extends GroupViewerBase<N, NV, NM>, NM extends GroupViewerMixin<N, NV, NM>>

        extends HtmlNodeViewer<N, NV, NM>
        implements GroupViewerMixin<N, NV, NM> {

    public HtmlGroupViewer() {
        this((NV) new GroupViewerBase(), HtmlUtil.createDivElement());
    }

    public HtmlGroupViewer(NV base, HTMLElement element) {
        super(base, element);
    }
}
