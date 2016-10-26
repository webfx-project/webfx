package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.view.ShapeView;

import java.awt.*;


/**
 * @author Bruno Salmon
 */
public interface SwingShapeView<S extends Shape> extends ShapeView<S> {

    void paintShape(Graphics2D g);

}
