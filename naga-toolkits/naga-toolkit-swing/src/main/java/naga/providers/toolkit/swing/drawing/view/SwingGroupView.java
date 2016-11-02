package naga.providers.toolkit.swing.drawing.view;

import javafx.beans.property.Property;
import naga.providers.toolkit.swing.util.SwingTransforms;
import naga.toolkit.drawing.shapes.Group;
import naga.toolkit.drawing.spi.view.implbase.GroupViewImplBase;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingGroupView extends GroupViewImplBase implements SwingDrawableView<Group> {

    @Override
    public void update(Property changedProperty) {
    }

    @Override
    public void paint(Graphics2D g) {
        g.setTransform(SwingTransforms.toSwingTransform(drawable.getTransforms()));
    }
}
