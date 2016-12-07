package naga.providers.toolkit.swing.fx.view;

import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.impl.canvas.CanvasNodeView;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public interface SwingEmbedComponentView
        <N extends Node>
        extends CanvasNodeView<N, Graphics2D> {

    JComponent getSwingComponent();

    default void paint(Graphics2D g) {
        getSwingComponent().paint(g);
    }

    default boolean containsPoint(Point2D point) {
        return getSwingComponent().contains((int) point.getX(), (int) point.getY());
    }
}
