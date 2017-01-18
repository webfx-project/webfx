package naga.fx.spi.swing.peer;

import naga.fx.spi.peer.CanvasNodePeer;
import emul.com.sun.javafx.geom.Point2D;
import emul.javafx.scene.Node;

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
        return getSwingComponent().contains((int) point.x, (int) point.y);
    }
}
