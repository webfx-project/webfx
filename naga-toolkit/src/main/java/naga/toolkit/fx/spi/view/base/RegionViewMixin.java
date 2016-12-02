package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.view.RegionView;

/**
 * @author Bruno Salmon
 */
public interface RegionViewMixin<R extends Region>
        extends RegionView<R>,
        NodeViewMixin<R, RegionViewBase<R>, RegionViewMixin<R>> {

}
