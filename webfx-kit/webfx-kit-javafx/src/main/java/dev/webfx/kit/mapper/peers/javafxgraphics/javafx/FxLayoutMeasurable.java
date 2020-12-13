package dev.webfx.kit.mapper.peers.javafxgraphics.javafx;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface FxLayoutMeasurable extends LayoutMeasurable {

    Node getFxNode();

/*
    @Override
    default void setSizeChangedCallback(Runnable callback) {
        Node fxNode = getFxNode();
        if (fxNode instanceof Skinnable)
            ((Skinnable) fxNode).skinProperty().addListener((observable, oldValue, newValue) -> callback.run());
    }
*/

    default Bounds getLayoutBounds() {
        javafx.geometry.Bounds b = getFxNode().getLayoutBounds();
        return new BoundingBox(b.getMinX(), b.getMinY(), b.getMinZ(), b.getWidth(), b.getHeight(), b.getDepth());
    }

    default double minWidth(double height) {
        return getFxNode().minWidth(height);
    }

    default double maxWidth(double height) {
        return getFxNode().maxWidth(height);
    }

    default double minHeight(double width) {
        return getFxNode().minHeight(width);
    }

    default double maxHeight(double width) {
        return getFxNode().maxHeight(width);
    }

    default double prefWidth(double height) {
        return getFxNode().prefWidth(height);
    }

    default double prefHeight(double width) {
        return getFxNode().prefHeight(width);
    }

}
