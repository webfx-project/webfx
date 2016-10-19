package naga.providers.toolkit.swing.drawing.view;

import naga.providers.toolkit.swing.util.SwingPaints;
import naga.toolkit.drawing.spi.view.RectangleView;
import naga.toolkit.drawing.spi.view.implbase.RectangleViewImplBase;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingRectangleView extends RectangleViewImplBase implements SwingShapeView, RectangleView {

    @Override
    public void paintShape(Graphics g) {
        g.setColor((Color) SwingPaints.toSwingPaint(shape.getFill()));
        g.fillRect(shape.getX().intValue(), shape.getY().intValue(), shape.getWidth().intValue(), shape.getHeight().intValue());
    }
}
