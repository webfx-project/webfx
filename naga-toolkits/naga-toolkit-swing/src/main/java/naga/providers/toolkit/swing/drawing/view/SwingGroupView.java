package naga.providers.toolkit.swing.drawing.view;

import naga.providers.toolkit.swing.util.SwingTransforms;
import naga.toolkit.drawing.shapes.Group;
import naga.toolkit.drawing.spi.view.base.GroupViewBase;
import naga.toolkit.drawing.spi.view.base.GroupViewMixin;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingGroupView
        extends SwingDrawableView<Group, GroupViewBase, GroupViewMixin>
        implements GroupViewMixin {

    public SwingGroupView() {
        super(new GroupViewBase());
    }

    @Override
    public void paint(Graphics2D g) {
        g.setTransform(SwingTransforms.toSwingTransform(getDrawable().getTransforms()));
    }
}
