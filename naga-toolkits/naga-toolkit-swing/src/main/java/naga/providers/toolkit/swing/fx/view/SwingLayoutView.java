package naga.providers.toolkit.swing.fx.view;

import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.view.base.RegionViewBase;
import naga.toolkit.fx.spi.view.base.RegionViewMixin;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingLayoutView
        <N extends Region, NV extends RegionViewBase<N, NV, NM>, NM extends RegionViewMixin<N, NV, NM>>
        extends SwingRegionView<N, NV, NM> {

    public SwingLayoutView() {
        super((NV) new RegionViewBase<N, NV, NM>());
    }

    @Override
    public void paint(Graphics2D c) {
    }

    @Override
    public boolean containsPoint(Point2D point) {
        return false;
    }

}
