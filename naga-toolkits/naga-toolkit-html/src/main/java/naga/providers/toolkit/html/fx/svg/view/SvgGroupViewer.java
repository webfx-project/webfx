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
        <N extends Group, NB extends GroupViewerBase<N, NB, NM>, NM extends GroupViewerMixin<N, NB, NM>>

        extends SvgNodeViewer<N, NB, NM>
        implements GroupViewerMixin<N, NB, NM> {

    public SvgGroupViewer() {
        this((NB) new GroupViewerBase(), SvgUtil.createSvgGroup());
    }

    public SvgGroupViewer(NB base, Element element) {
        super(base, element);
    }
}
