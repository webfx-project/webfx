package javafx.scene.effect;

/**
 * @author Bruno Salmon
 */
public class BoxBlur implements Effect {

    private final double width;
    private final double height;
    private final int iterations;

    /**
     * Creates a new instance of BoxBlur with default parameters.
     */
    public BoxBlur() {
        this(5, 5, 1);
    }

    /**
     * Creates a new instance of BoxBlur with specified width, height and
     * iterations.
     * @param width the horizontal dimension of the blur effect
     * @param height the vertical dimension of the blur effect
     * @param iterations the number of times to iterate the blur effect to
     * improve its "quality" or "smoothness"
     */
    public BoxBlur(double width, double height, int iterations) {
        this.width = width;
        this.height = height;
        this.iterations = iterations;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getIterations() {
        return iterations;
    }
}
