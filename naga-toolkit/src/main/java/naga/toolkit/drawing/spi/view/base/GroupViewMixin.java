package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.shapes.Group;
import naga.toolkit.drawing.spi.view.GroupView;

/**
 * @author Bruno Salmon
 */
public interface GroupViewMixin
        extends GroupView,
        DrawableViewMixin<Group, GroupViewBase, GroupViewMixin> {

}
