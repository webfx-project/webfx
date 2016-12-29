package naga.fx.spi.gwt.html.viewer;

import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.scene.layout.Region;
import naga.fx.spi.viewer.base.RegionViewerBase;
import naga.fx.spi.viewer.base.RegionViewerMixin;

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
