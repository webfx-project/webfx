package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.layout.Region;
import naga.toolkit.drawing.spi.view.RegionView;

/**
 * @author Bruno Salmon
 */
public class RegionViewBase<R extends Region>
        extends NodeViewBase<R, RegionViewBase<R>, RegionViewMixin<R>>
        implements RegionView<R> {
}
