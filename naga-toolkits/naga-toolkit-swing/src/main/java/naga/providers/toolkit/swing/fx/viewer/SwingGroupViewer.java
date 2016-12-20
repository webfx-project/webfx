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
