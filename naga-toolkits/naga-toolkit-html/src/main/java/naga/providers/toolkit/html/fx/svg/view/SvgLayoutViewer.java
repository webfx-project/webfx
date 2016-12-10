package naga.providers.toolkit.html.fx.svg.view;

import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.viewer.base.RegionViewerBase;
import naga.toolkit.fx.spi.viewer.base.RegionViewerMixin;

/**
 * @author Bruno Salmon
 */
public class SvgLayoutViewer
        <N extends Region, NV extends RegionViewerBase<N, NV, NM>, NM extends RegionViewerMixin<N, NV, NM>>

        extends SvgRegionViewer<N, NV, NM> {

    public SvgLayoutViewer() {
        super((NV) new RegionViewerBase<N, NV, NM>(), SvgUtil.createSvgGroup());
    }

}
