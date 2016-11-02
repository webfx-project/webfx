package naga.toolkit.drawing.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Group;
import naga.toolkit.drawing.spi.view.GroupView;

/**
 * @author Bruno Salmon
 */
public class GroupViewBase
        extends DrawableViewBase<Group, GroupViewBase, GroupViewMixin>
        implements GroupView {

    @Override
    public boolean update(Property changedProperty) {
        return false;
    }
}
