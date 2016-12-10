package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.viewer.base.RegionViewerBase;
import naga.toolkit.fx.spi.viewer.base.RegionViewerMixin;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingLayoutViewer
        <N extends Region, NV extends RegionViewerBase<N, NV, NM>, NM extends RegionViewerMixin<N, NV, NM>>
        extends SwingRegionViewer<N, NV, NM> {

    public SwingLayoutViewer() {
        super((NV) new RegionViewerBase<N, NV, NM>());
    }

    @Override
    public void paint(Graphics2D c) {
    }

    @Override
    public boolean containsPoint(Point2D point) {
        return false;
    }

}
