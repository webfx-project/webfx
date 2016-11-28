package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.geom.Point2D;
import naga.toolkit.drawing.scene.Group;
import naga.toolkit.drawing.spi.view.base.GroupViewBase;
import naga.toolkit.drawing.spi.view.base.GroupViewMixin;

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
