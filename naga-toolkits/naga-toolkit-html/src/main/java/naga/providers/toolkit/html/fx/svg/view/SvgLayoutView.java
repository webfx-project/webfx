package naga.providers.toolkit.html.fx.svg.view;

import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.view.base.RegionViewBase;
import naga.toolkit.fx.spi.view.base.RegionViewMixin;

/**
 * @author Bruno Salmon
 */
public class SvgLayoutView
        <N extends Region, NV extends RegionViewBase<N, NV, NM>, NM extends RegionViewMixin<N, NV, NM>>

        extends SvgRegionView<N, NV, NM> {

    public SvgLayoutView() {
        super((NV) new RegionViewBase<N, NV, NM>(), SvgUtil.createSvgGroup());
    }

}
