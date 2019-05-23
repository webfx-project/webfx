package javafx.scene.effect;

import javafx.scene.paint.Color;

/**
 * @author Bruno Salmon
 */
public class DropShadow implements Effect {

    private BlurType blurType = BlurType.THREE_PASS_BOX;
    private Color color = Color.BLACK;
    private double radius;
    private double spread;
    private double offsetX;
    private double offsetY;

    /**
     * Creates a new instance of DropShadow with default parameters.
     */
    public DropShadow() {}

    /**
     * Creates a new instance of DropShadow with specified radius and color.
     * @param radius the radius of the shadow blur kernel
     * @param color the shadow {@code Color}
     * @since JavaFX 2.1
     */
    public DropShadow(double radius, Color color) {
        setRadius(radius);
        setColor(color);
    }

    /**
     * Creates a new instance of DropShadow with the specified radius, offsetX,
     * offsetY and color.
     * @param radius the radius of the shadow blur kernel
     * @param offsetX the shadow offset in the x direction
     * @param offsetY the shadow offset in the y direction
     * @param color the shadow {@code Color}
     * @since JavaFX 2.1
     */
    public DropShadow(double radius, double offsetX, double offsetY, Color color) {
        setRadius(radius);
        setOffsetX(offsetX);
        setOffsetY(offsetY);
        setColor(color);
    }

    /**
     * Creates a new instance of DropShadow with the specified blurType, color,
     * radius, spread, offsetX and offsetY.
     * @param blurType the algorithm used to blur the shadow
     * @param color the shadow {@code Color}
     * @param radius the radius of the shadow blur kernel
     * @param spread the portion of the radius where the contribution of
     * the source material will be 100%
     * @param offsetX the shadow offset in the x direction
     * @param offsetY the shadow offset in the y direction
     * @since JavaFX 2.1
     */
    public DropShadow(BlurType blurType, Color color, double radius, double spread,
                      double offsetX, double offsetY) {
        setBlurType(blurType);
        setColor(color);
        setRadius(radius);
        setSpread(spread);
        setOffsetX(offsetX);
        setOffsetY(offsetY);
    }

    public BlurType getBlurType() {
        return blurType;
    }

    public void setBlurType(BlurType blurType) {
        this.blurType = blurType;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getSpread() {
        return spread;
    }

    public void setSpread(double spread) {
        this.spread = spread;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }
}
