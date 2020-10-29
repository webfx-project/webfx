package webfx.demo.mandelbrot.math;

import java.math.BigDecimal;

/**
 * @author Bruno Salmon
 */
final class MandelbrotPoint {

    // Input value;
    double x;
    double y;

    // Reusable computed values
    double xmin_d;
    double xmax_d;
    BigDecimal dy;
    BigDecimal yval;
    double yval_d;
    double dx;
    double cpy;
}

