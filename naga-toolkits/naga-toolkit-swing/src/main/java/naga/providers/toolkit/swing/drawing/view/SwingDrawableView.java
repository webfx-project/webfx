package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.view.DrawableView;

import java.awt.*;


/**
 * @author Bruno Salmon
 */
public interface SwingDrawableView<S extends Drawable> extends DrawableView<S> {

    void paintDrawable(Graphics2D g);

}
