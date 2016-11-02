package naga.providers.toolkit.html.drawing.view;

import javafx.beans.property.Property;
import naga.providers.toolkit.html.drawing.SvgDrawing;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.shapes.Group;
import naga.toolkit.drawing.spi.view.base.GroupViewBase;
import naga.toolkit.drawing.spi.view.mixin.GroupViewMixin;

/**
 * @author Bruno Salmon
 */
public class SvgGroupView extends SvgDrawableView<Group> implements GroupViewMixin {

    public SvgGroupView() {
        super(SvgUtil.createSvgGroup());
    }

    private final GroupViewBase base = new GroupViewBase();
    @Override
    public GroupViewBase getDrawableViewBase() {
        return base;
    }

    @Override
    public boolean update(SvgDrawing svgDrawing, Property changedProperty) {
        return false;
    }
}
