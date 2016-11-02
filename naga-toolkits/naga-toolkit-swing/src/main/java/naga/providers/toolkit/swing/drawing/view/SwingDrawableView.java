package naga.providers.toolkit.swing.drawing.view;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.view.DrawableView;

import java.awt.*;


/**
 * @author Bruno Salmon
 */
public interface SwingDrawableView<S extends Drawable> extends DrawableView<S> {

    void update(Property changedProperty);

    void paint(Graphics2D g);

}
