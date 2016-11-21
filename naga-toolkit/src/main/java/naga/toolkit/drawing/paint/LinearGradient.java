package naga.toolkit.drawing.paint;

import naga.toolkit.drawing.paint.impl.LinearGradientImpl;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface LinearGradient extends Paint {

    /**
     * Defines the X coordinate of the gradient axis start point.
     * If proportional is true (the default), this value specifies a
     * point on a unit square that will be scaled to match the size of the
     * the shape that the gradient fills.
     */
    double getStartX();

    /**
     * Defines the Y coordinate of the gradient axis start point.
     * If proportional is true (the default), this value specifies a
     * point on a unit square that will be scaled to match the size of the
     * the shape that the gradient fills.
     */
    double getStartY();

    /**
     * Defines the X coordinate of the gradient axis end point.
     * If proportional is true (the default), this value specifies a
     * point on a unit square that will be scaled to match the size of the
     * the shape that the gradient fills.
     */
    double getEndX();

    /**
     * Defines the Y coordinate of the gradient axis end point.
     * If proportional is true (the default), this value specifies a
     * point on a unit square that will be scaled to match the size of the
     * the shape that the gradient fills.
     */
    double getEndY();

    /**
     * Indicates whether start and end locations are proportional or absolute.
     * If this flag is true, the two end points are defined in a coordinate
     * space where coordinates in the range {@code [0..1]} are scaled to map
     * onto the bounds of the shape that the gradient fills.
     * If this flag is false, then the coordinates are specified in the local
     * coordinate system of the node.
     */
    boolean isProportional();

    /**
     * Defines which of the following cycle method is applied
     * to the {@code LinearGradient}: {@code CycleMethod.NO_CYCLE},
     * {@code CycleMethod.REFLECT}, or {@code CycleMethod.REPEAT}.
     */
    CycleMethod getCycleMethod();

    /**
     * A sequence of 2 or more {@code Stop} values specifying how to distribute
     * the colors along the gradient. These values must be in the range
     * 0.0 to 1.0. They act like key frames along the gradient: they mark where
     * the gradient should be exactly a particular color.
     *
     * <p>Each stop in the sequence must have an offset that is greater than the
     * previous stop in the sequence.</p>
     *
     * <p>The list is unmodifiable and will throw
     * {@code UnsupportedOperationException} on each modification attempt.</p>
     */
    List<Stop> getStops();

    default double getAngle() {
        double deltaX = getEndX() - getStartX();
        double deltaY = getEndY() - getStartY();
        return Math.atan2(deltaY, deltaX);
    }

    default double getAngleDegree() {
        return Math.toDegrees(getAngle());
    }

    default double getLength() {
        double deltaX = getEndX() - getStartX();
        double deltaY = getEndY() - getStartY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Creates a linear gradient value from a string representation.
     * <p>The format of the string representation is based on
     * JavaFX CSS specification for linear gradient which is
     * <pre>
     * linear-gradient( [ [from &lt;point&gt; to &lt;point&gt;| [ to &lt;side-or-corner&gt;], ]? [ [ repeat | reflect ], ]? &lt;color-stop&gt;[, &lt;color-stop&gt;]+)
     * </pre>
     * where
     * <pre>
     * &lt;side-or-corner&gt; = [left | right] || [top | bottom]
     * &lt;point&gt; = [ [ &lt;length&gt; &lt;length&gt; ] | [ &lt;percentage&gt; | &lt;percentage&gt; ] ]
     * &lt;color-stop&gt; = [ &lt;color&gt; [ &lt;percentage&gt; | &lt;length&gt;]? ]
     * </pre>
     * </p>
     * <p> Currently length can be only specified in px, the specification of unit can be omited.
     * Format of color representation is the one used in {@link Color#web(String color)}.
     * The linear-gradient keyword can be omited.
     * For additional information about the format of string representation, see the
     * <a href="../doc-files/cssref.html">CSS Reference Guide</a>.
     * </p>
     *
     * Examples:
     * <pre><code>
     * LinearGradient g
     *      = LinearGradient.valueOf("linear-gradient(from 0% 0% to 100% 100%, red  0% , blue 30%,  black 100%)");
     * LinearGradient g
     *      = LinearGradient.valueOf("from 0% 0% to 100% 100%, red  0% , blue 30%,  black 100%");
     * LinearGradient g
     *      = LinearGradient.valueOf("linear-gradient(from 0px 0px to 200px 0px, #00ff00 0%, 0xff0000 50%, 0x1122ff40 100%)");
     * LinearGradient g
     *      = LinearGradient.valueOf("from 0px 0px to 200px 0px, #00ff00 0%, 0xff0000 50%, 0x1122ff40 100%");
     * </code></pre>
     *
     * @param value the string to convert
     * @throws NullPointerException if the {@code value} is {@code null}
     * @throws IllegalArgumentException if the {@code value} cannot be parsed
     * @return a {@code LinearGradient} object holding the value represented
     * by the string argument.
     * @since JavaFX 2.1
     */
    static LinearGradient valueOf(String value) {
        return LinearGradientImpl.valueOf(value);
    }

    static LinearGradient create(double startX, double startY, double endX, double endY, boolean proportional, CycleMethod cycleMethod, Stop... stops) {
        return new LinearGradientImpl(startX, startY, endX, endY, proportional, cycleMethod, stops);
    }

}
