package naga.providers.toolkit.html.fx.html.view;

import elemental2.HTMLElement;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.view.base.RegionViewBase;
import naga.toolkit.fx.spi.view.base.RegionViewMixin;

/**
 * @author Bruno Salmon
 */
abstract class HtmlRegionView
        <N extends Region, NV extends RegionViewBase<N, NV, NM>, NM extends RegionViewMixin<N, NV, NM>>

        extends HtmlNodeView<N, NV, NM>
        implements RegionViewMixin<N, NV, NM> {

    HtmlRegionView(NV base, HTMLElement element) {
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
