package naga.providers.toolkit.html.fx.svg.view;

import elemental2.Element;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.view.base.RegionViewBase;
import naga.toolkit.fx.spi.view.base.RegionViewMixin;

/**
 * @author Bruno Salmon
 */
abstract class SvgRegionView
        <N extends Region, NV extends RegionViewBase<N, NV, NM>, NM extends RegionViewMixin<N, NV, NM>>

        extends SvgNodeView<N, NV, NM>
        implements RegionViewMixin<N, NV, NM> {

    SvgRegionView(NV base, Element element) {
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
