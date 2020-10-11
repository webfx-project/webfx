package webfx.demos.mandelbrot.computation;

import java.math.BigDecimal;

/**
 * @author Bruno Salmon
 */
public final class MandelbrotPoint {

    // Input value;
    public double x;
    public double y;

    // Reusable computed values
    double xmin_d;
    double xmax_d;
    BigDecimal dy;
    BigDecimal yval;
    double yval_d;
    double dx;
    double cpy;
}

