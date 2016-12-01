package naga.providers.toolkit.html.drawing.html.view;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.drawing.scene.layout.Region;
import naga.toolkit.drawing.spi.view.base.RegionViewBase;
import naga.toolkit.drawing.spi.view.base.RegionViewMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlRegionView<R extends Region>
        extends HtmlNodeView<R, RegionViewBase<R>, RegionViewMixin<R>>
        implements RegionViewMixin<R> {

    public HtmlRegionView() {
        super(new RegionViewBase<>(), HtmlUtil.createDivElement());
    }
}
