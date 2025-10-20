package dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.measurable;

import javafx.geometry.Bounds;

/**
 * @author Bruno Salmon
 */
public interface Measurable {

    Bounds getLayoutBounds();

    double minWidth(double height);

    double maxWidth(double height);

    double minHeight(double width);

    double maxHeight(double width);

    double prefWidth(double height);

    double prefHeight(double width);

    default void clearCache() {}
}
