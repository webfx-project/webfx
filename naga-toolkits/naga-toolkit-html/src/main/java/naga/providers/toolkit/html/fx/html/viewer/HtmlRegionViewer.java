package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.HTMLElement;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.viewer.base.RegionViewerBase;
import naga.toolkit.fx.spi.viewer.base.RegionViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class HtmlRegionViewer
        <N extends Region, NV extends RegionViewerBase<N, NV, NM>, NM extends RegionViewerMixin<N, NV, NM>>

        extends HtmlNodeViewer<N, NV, NM>
        implements RegionViewerMixin<N, NV, NM> {

    HtmlRegionViewer(NV base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateWidth(Double width) {
        getElement().style.width = toPx(width);
    }

    @Override
    public void updateHeight(Double height) {
        getElement().style.height = toPx(height);
    }
}
