package webfx.demos.mandelbrot.mandelbrotmodel;

import java.math.BigDecimal;

/**
 * @author Bruno Salmon
 */
final class MandelbrotPoint {

    // Input value;
    MandelbrotModel model;
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

