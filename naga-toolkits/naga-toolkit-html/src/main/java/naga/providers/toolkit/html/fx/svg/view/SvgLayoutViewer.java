package naga.providers.toolkit.html.fx.svg.view;

import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.scene.layout.Background;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.viewer.base.RegionViewerBase;
import naga.toolkit.fx.spi.viewer.base.RegionViewerMixin;

/**
 * @author Bruno Salmon
 */
public class SvgLayoutViewer
        <N extends Region, NB extends RegionViewerBase<N, NB, NM>, NM extends RegionViewerMixin<N, NB, NM>>

        extends SvgRegionViewer<N, NB, NM> {

    public SvgLayoutViewer() {
        super((NB) new RegionViewerBase<N, NB, NM>(), SvgUtil.createSvgGroup());
    }

    @Override
    public void updateBackground(Background background) {
        // Not yet implemented
    }
}
