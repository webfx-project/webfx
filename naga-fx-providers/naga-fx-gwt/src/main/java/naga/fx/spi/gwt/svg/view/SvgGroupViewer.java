package naga.fx.spi.gwt.svg.view;

import elemental2.Element;
import naga.fx.spi.gwt.util.SvgUtil;
import naga.fx.scene.Group;
import naga.fx.spi.viewer.base.GroupViewerBase;
import naga.fx.spi.viewer.base.GroupViewerMixin;

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
