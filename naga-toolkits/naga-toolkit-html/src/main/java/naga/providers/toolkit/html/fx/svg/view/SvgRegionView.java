package naga.providers.toolkit.html.fx.svg.view;

import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.view.base.RegionViewBase;
import naga.toolkit.fx.spi.view.base.RegionViewMixin;

/**
 * @author Bruno Salmon
 */
public class SvgRegionView<R extends Region>
        extends SvgNodeView<R, RegionViewBase<R>, RegionViewMixin<R>>
        implements RegionViewMixin<R> {

    public SvgRegionView() {
        super(new RegionViewBase<>(), SvgUtil.createSvgGroup());
    }
}
