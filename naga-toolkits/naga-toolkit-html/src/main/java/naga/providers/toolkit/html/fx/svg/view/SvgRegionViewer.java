package naga.providers.toolkit.html.fx.svg.view;

import elemental2.Element;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.viewer.base.RegionViewerBase;
import naga.toolkit.fx.spi.viewer.base.RegionViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class SvgRegionViewer
        <N extends Region, NV extends RegionViewerBase<N, NV, NM>, NM extends RegionViewerMixin<N, NV, NM>>

        extends SvgNodeViewer<N, NV, NM>
        implements RegionViewerMixin<N, NV, NM> {

    SvgRegionViewer(NV base, Element element) {
        super(base, element);
    }

    @Override
    public void updateWidth(Double width) {
        setElementAttribute("width", width);
    }

    @Override
    public void updateHeight(Double height) {
        setElementAttribute("height", height);
    }
}
