package naga.providers.toolkit.html.fx.html.viewer;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.viewer.base.RegionViewerBase;
import naga.toolkit.fx.spi.viewer.base.RegionViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlLayoutViewer
        <N extends Region, NV extends RegionViewerBase<N, NV, NM>, NM extends RegionViewerMixin<N, NV, NM>>

        extends HtmlRegionViewer<N, NV, NM> {

    public HtmlLayoutViewer() {
        super((NV) new RegionViewerBase<N, NV, NM>(), HtmlUtil.createDivElement());
    }

}
