package javafx.scene.paint;

/**
 * @author Bruno Salmon
 */
/**
 * This enum defines one of the following methods to use when painting
 * outside the gradient bounds: {@code  CycleMethod.NO_CYCLE},
 * {@code CycleMethod.REFLECT}, or {@code  CycleMethod.REPEAT}.
 * @since JavaFX 2.0
 */
public enum CycleMethod {

    /**
     * Defines the cycle method that uses the terminal colors to fill the remaining area.
     */
    NO_CYCLE, // MultipleGradientPaint.CycleMethod.NO_CYCLE

    /**
     * Defines the cycle method that reflects the gradient colors start-to-end,
     * end-to-start to fill the remaining area.
     */
    REFLECT, // MultipleGradientPaint.CycleMethod.REFLECT

    /**
     * Defines the cycle method that repeats the gradient colors to fill the remaining area.
     */
    REPEAT; //MultipleGradientPaint.CycleMethod.REPEAT
}
