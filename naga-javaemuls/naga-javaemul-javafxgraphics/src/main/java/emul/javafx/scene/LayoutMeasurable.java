package emul.javafx.scene;

import emul.javafx.geometry.Bounds;

/**
 * @author Bruno Salmon
 */
public interface LayoutMeasurable {

    Bounds getLayoutBounds();

    double minWidth(double height);

    double maxWidth(double height);

    double minHeight(double width);

    double maxHeight(double width);

    double prefWidth(double height);

    double prefHeight(double width);

    default void setSizeChangedCallback(Runnable sizeChangedCallback) {} // Ex: when the peer skin has changed
}
