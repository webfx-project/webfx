package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.geom.Point2D;
import naga.toolkit.drawing.scene.layout.Region;
import naga.toolkit.drawing.spi.view.base.RegionViewBase;
import naga.toolkit.drawing.spi.view.base.RegionViewMixin;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingRegionView<R extends Region>
        extends SwingNodeView<R, RegionViewBase<R>, RegionViewMixin<R>>
        implements RegionViewMixin<R> {

    public SwingRegionView() {
        super(new RegionViewBase<>());
    }

    @Override
    public void paint(Graphics2D c) {
    }

    @Override
    public boolean containsPoint(Point2D point) {
        return false;
    }
}
