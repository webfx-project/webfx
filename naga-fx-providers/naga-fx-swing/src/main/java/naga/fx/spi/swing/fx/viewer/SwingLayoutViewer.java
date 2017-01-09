package naga.fx.spi.swing.fx.viewer;

import naga.fx.sun.geom.Point2D;
import naga.fx.scene.layout.Region;
import naga.fx.spi.viewer.base.RegionViewerBase;
import naga.fx.spi.viewer.base.RegionViewerMixin;

/**
 * @author Bruno Salmon
 */
public class SwingLayoutViewer
        <N extends Region, NB extends RegionViewerBase<N, NB, NM>, NM extends RegionViewerMixin<N, NB, NM>>
        extends SwingRegionViewer<N, NB, NM> {

    public SwingLayoutViewer() {
        super((NB) new RegionViewerBase<N, NB, NM>());
    }

    @Override
    public boolean containsPoint(Point2D point) {
        return false;
    }

}
