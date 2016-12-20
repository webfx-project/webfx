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
        <N extends Group, NB extends GroupViewerBase<N, NB, NM>, NM extends GroupViewerMixin<N, NB, NM>>

        extends HtmlNodeViewer<N, NB, NM>
        implements GroupViewerMixin<N, NB, NM> {

    public HtmlGroupViewer() {
        this((NB) new GroupViewerBase(), HtmlUtil.createDivElement());
    }

    public HtmlGroupViewer(NB base, HTMLElement element) {
        super(base, element);
    }
}
