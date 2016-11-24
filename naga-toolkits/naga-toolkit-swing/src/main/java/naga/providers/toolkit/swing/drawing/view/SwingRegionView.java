package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.layout.Region;
import naga.toolkit.drawing.spi.view.base.RegionViewBase;
import naga.toolkit.drawing.spi.view.base.RegionViewMixin;

/**
 * @author Bruno Salmon
 */
public class SwingRegionView
        extends SwingNodeView<Region, RegionViewBase, RegionViewMixin>
        implements RegionViewMixin {

    public SwingRegionView() {
        super(new RegionViewBase());
    }
}
