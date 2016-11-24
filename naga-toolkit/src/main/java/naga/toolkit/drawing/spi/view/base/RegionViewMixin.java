package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.layout.Region;
import naga.toolkit.drawing.spi.view.RegionView;

/**
 * @author Bruno Salmon
 */
public interface RegionViewMixin
        extends RegionView,
        NodeViewMixin<Region, RegionViewBase, RegionViewMixin> {

}
