package javafx.scene.effect;

/**
 * @author Bruno Salmon
 */
public class GaussianBlur implements Effect {

    private final double radius;

    public GaussianBlur() {
        this(10);
    }

    public GaussianBlur(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public double getSigma() {
        return getSigma(getRadius());
    }

    public static double getSigma(double radius) {
        return (radius + 1) / Math.sqrt(2 * Math.log(255));
    }

}
