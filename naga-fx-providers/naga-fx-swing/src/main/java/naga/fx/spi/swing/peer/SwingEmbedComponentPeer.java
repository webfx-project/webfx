package naga.fx.spi.swing.peer;

import naga.fx.spi.peer.CanvasNodePeer;
import naga.fx.sun.geom.Point2D;
import naga.fx.scene.Node;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public interface SwingEmbedComponentPeer
        <N extends Node>
        extends CanvasNodePeer<N, Graphics2D> {

    JComponent getSwingComponent();

    default void paint(Graphics2D g) {
        getSwingComponent().paint(g);
    }

    default boolean containsPoint(Point2D point) {
        return getSwingComponent().contains((int) point.getX(), (int) point.getY());
    }
}
