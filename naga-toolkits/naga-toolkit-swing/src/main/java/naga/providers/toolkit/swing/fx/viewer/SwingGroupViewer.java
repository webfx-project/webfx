package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.spi.viewer.base.GroupViewerBase;
import naga.toolkit.fx.spi.viewer.base.GroupViewerMixin;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingGroupViewer
        <N extends Group, NV extends GroupViewerBase<N, NV, NM>, NM extends GroupViewerMixin<N, NV, NM>>

        extends SwingNodeViewer<N, NV, NM>
        implements GroupViewerMixin<N, NV, NM> {

    public SwingGroupViewer() {
        this((NV) new GroupViewerBase());
    }

    public SwingGroupViewer(NV base) {
        super(base);
    }

    @Override
    public void paint(Graphics2D c) {
    }

    @Override
    public boolean containsPoint(Point2D point) {
        return false;
    }
}
