package dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.measurable;

import javafx.geometry.Bounds;

/**
 * @author Bruno Salmon
 */
public interface MeasurableMixin extends Measurable {

    Measurable getLayoutMeasurable();

    default Bounds getLayoutBounds() {
        return getLayoutMeasurable().getLayoutBounds();
    }

    default double minWidth(double height) {
        return getLayoutMeasurable().minWidth(height);
    }

    default double maxWidth(double height) {
        return getLayoutMeasurable().maxWidth(height);
    }

    default double minHeight(double width) {
        return getLayoutMeasurable().minHeight(width);
    }

    default double maxHeight(double width) {
        return getLayoutMeasurable().maxHeight(width);
    }

    default double prefWidth(double height) {
        return getLayoutMeasurable().prefWidth(height);
    }

    default double prefHeight(double width) {
        return getLayoutMeasurable().prefHeight(width);
    }

    @Override
    default void clearCache() {
        getLayoutMeasurable().clearCache();
    }
}
