package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.view.RegionView;

/**
 * @author Bruno Salmon
 */
public class RegionViewBase<R extends Region>
        extends NodeViewBase<R, RegionViewBase<R>, RegionViewMixin<R>>
        implements RegionView<R> {
}
