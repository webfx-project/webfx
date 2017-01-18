package naga.fx.spi.swing.peer;

import emul.javafx.geometry.BoundingBox;
import emul.javafx.geometry.Bounds;
import emul.javafx.scene.LayoutMeasurable;
import emul.javafx.scene.Node;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public interface SwingLayoutMeasurable<N extends Node> extends LayoutMeasurable, SwingEmbedComponentPeer<N> {

    default Bounds getLayoutBounds() {
        JComponent c = getSwingComponent();
        return new BoundingBox(0, 0, 0, c.getWidth(), c.getHeight(), 0);
    }

    default double minWidth(double height) {
        return getSwingComponent().getMinimumSize().getWidth();
    }

    default double maxWidth(double height) {
        return getSwingComponent().getMaximumSize().getWidth();
    }

    default double minHeight(double width) {
        return getSwingComponent().getMinimumSize().getHeight();
    }

    default double maxHeight(double width) {
        return getSwingComponent().getMaximumSize().getHeight();
    }

    default double prefWidth(double height) {
        return getSwingComponent().getPreferredSize().getWidth();
    }

    default double prefHeight(double width) {
        return getSwingComponent().getPreferredSize().getHeight();
    }

}
