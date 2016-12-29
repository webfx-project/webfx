package naga.fx.spi.gwt.svg.view;

import naga.fx.spi.gwt.util.SvgUtil;
import naga.fx.scene.layout.Background;
import naga.fx.scene.layout.Border;
import naga.fx.scene.layout.Region;
import naga.fx.spi.viewer.base.RegionViewerBase;
import naga.fx.spi.viewer.base.RegionViewerMixin;

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

    @Override
    public void updateBorder(Border border) {
        // Not yet implemented
    }
}
