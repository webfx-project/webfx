package naga.providers.toolkit.html.fx.svg.view;

import elemental2.Element;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.viewer.base.RegionViewerBase;
import naga.toolkit.fx.spi.viewer.base.RegionViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class SvgRegionViewer
        <N extends Region, NB extends RegionViewerBase<N, NB, NM>, NM extends RegionViewerMixin<N, NB, NM>>

        extends SvgNodeViewer<N, NB, NM>
        implements RegionViewerMixin<N, NB, NM> {

    SvgRegionViewer(NB base, Element element) {
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
