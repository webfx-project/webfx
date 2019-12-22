package webfx.extras.materialdesign.util.layout;

import javafx.geometry.Insets;
import javafx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public final class LayoutUtil {

    public static <N extends Region> N removePadding(N content) {
        content.setPadding(Insets.EMPTY);
        return content;
    }

    // Snap methods from Region (but public)

    /**
     * If snapToPixel is true, then the value is ceil'd using Math.ceil. Otherwise,
     * the value is simply returned.
     *
     * @param value The value that needs to be snapped
     * @param snapToPixel Whether to snap to pixel
     * @return value either as passed in or ceil'd based on snapToPixel
     */
    private static double snapSize(double value, boolean snapToPixel) {
        return snapToPixel ? Math.ceil(value) : value;
    }

    /**
     * Returns a value ceiled to the nearest pixel.
     * @param value the size value to be snapped
     * @return value ceiled to nearest pixel
     */
    public static double snapSize(double value) {
        return snapSize(value, true);
    }

    /**
     * If snapToPixel is true, then the value is rounded using Math.round. Otherwise,
     * the value is simply returned.
     *
     * @param value The value that needs to be snapped
     * @param snapToPixel Whether to snap to pixel
     * @return value either as passed in or rounded based on snapToPixel
     */
    public static double snapPosition(double value, boolean snapToPixel) {
        return snapToPixel ? Math.round(value) : value;
    }

    /**
     * Returns a value rounded to the nearest pixel.
     * @param value the position value to be snapped
     * @return value rounded to nearest pixel
     */
    public static double snapPosition(double value) {
        return snapPosition(value, true);
    }

}
