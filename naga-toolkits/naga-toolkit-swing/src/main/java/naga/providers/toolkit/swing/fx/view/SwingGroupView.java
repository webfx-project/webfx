package naga.providers.toolkit.swing.fx.view;

import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.spi.view.base.GroupViewBase;
import naga.toolkit.fx.spi.view.base.GroupViewMixin;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingGroupView
        extends SwingNodeView<Group, GroupViewBase, GroupViewMixin>
        implements GroupViewMixin {

    public SwingGroupView() {
        super(new GroupViewBase());
    }

    @Override
    public void paint(Graphics2D c) {
    }

    @Override
    public boolean containsPoint(Point2D point) {
        return false;
    }
}
