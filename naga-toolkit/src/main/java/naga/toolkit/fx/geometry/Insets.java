package naga.toolkit.fx.geometry;

import naga.toolkit.fx.geometry.impl.InsetsImpl;

/**
 * @author Bruno Salmon
 */
public interface Insets {
    /**
     * Empty insets. An {@code Insets} instance with all offsets equal to zero.
     */
    Insets EMPTY = create(0, 0, 0, 0);

    /**
     * The inset on the top side
     */
    double getTop();

    /**
     * The inset on the right side
     */
    double getRight();

    /**
     * The inset on the bottom side
     */
    double getBottom();

    /**
     * The inset on the left side
     */
    double getLeft();

    static Insets create(double top, double right, double bottom, double left) {
        return new InsetsImpl(top, right, bottom, left);
    }
}
