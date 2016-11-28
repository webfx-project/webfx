package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.layout.Region;
import naga.toolkit.drawing.spi.view.RegionView;

/**
 * @author Bruno Salmon
 */
public interface RegionViewMixin<R extends Region>
        extends RegionView<R>,
        NodeViewMixin<R, RegionViewBase<R>, RegionViewMixin<R>> {

}
