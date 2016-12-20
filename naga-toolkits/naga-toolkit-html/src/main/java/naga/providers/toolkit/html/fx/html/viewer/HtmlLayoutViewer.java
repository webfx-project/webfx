package naga.providers.toolkit.html.fx.html.viewer;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.viewer.base.RegionViewerBase;
import naga.toolkit.fx.spi.viewer.base.RegionViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlLayoutViewer
        <N extends Region, NB extends RegionViewerBase<N, NB, NM>, NM extends RegionViewerMixin<N, NB, NM>>

        extends HtmlRegionViewer<N, NB, NM> {

    public HtmlLayoutViewer() {
        super((NB) new RegionViewerBase<N, NB, NM>(), HtmlUtil.createDivElement());
    }

}
