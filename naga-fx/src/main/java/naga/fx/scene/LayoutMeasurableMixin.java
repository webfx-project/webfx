package naga.fx.scene;

import javafx.geometry.Bounds;

/**
 * @author Bruno Salmon
 */
public interface LayoutMeasurableMixin extends LayoutMeasurable {

    LayoutMeasurable getLayoutMeasurable();

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
}
