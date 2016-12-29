package naga.fx.spi.swing.fx.viewer;

import naga.fx.geom.Point2D;
import naga.fx.scene.Group;
import naga.fx.spi.viewer.base.GroupViewerBase;
import naga.fx.spi.viewer.base.GroupViewerMixin;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingGroupViewer
        <N extends Group, NB extends GroupViewerBase<N, NB, NM>, NM extends GroupViewerMixin<N, NB, NM>>

        extends SwingNodeViewer<N, NB, NM>
        implements GroupViewerMixin<N, NB, NM> {

    public SwingGroupViewer() {
        this((NB) new GroupViewerBase());
    }

    public SwingGroupViewer(NB base) {
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
