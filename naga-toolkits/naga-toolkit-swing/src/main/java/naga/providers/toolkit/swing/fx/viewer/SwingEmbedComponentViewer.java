package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.viewer.CanvasNodeViewer;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public interface SwingEmbedComponentViewer
        <N extends Node>
        extends CanvasNodeViewer<N, Graphics2D> {

    JComponent getSwingComponent();

    default void paint(Graphics2D g) {
        getSwingComponent().paint(g);
    }

    default boolean containsPoint(Point2D point) {
        return getSwingComponent().contains((int) point.getX(), (int) point.getY());
    }
}
