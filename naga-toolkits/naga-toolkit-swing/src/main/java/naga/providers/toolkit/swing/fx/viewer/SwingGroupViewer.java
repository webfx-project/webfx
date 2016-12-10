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
        extends SwingNodeViewer<Group, GroupViewerBase, GroupViewerMixin>
        implements GroupViewerMixin {

    public SwingGroupViewer() {
        super(new GroupViewerBase());
    }

    @Override
    public void paint(Graphics2D c) {
    }

    @Override
    public boolean containsPoint(Point2D point) {
        return false;
    }
}
