package naga.toolkit.drawing.paint.impl;

import naga.toolkit.drawing.paint.CycleMethod;
import naga.toolkit.drawing.paint.LinearGradient;
import naga.toolkit.drawing.paint.Stop;

import java.util.List;

/**
 * <p>The {@code LinearGradient} class fills a shape
 * with a linear color gradient pattern. The user may specify two or
 * more gradient colors, and this Paint will provide an interpolation
 * between each color.</p>
 *
 * <p>
 * The application provides an array of {@code Stop}s specifying how to distribute
 * the colors along the gradient. The {@code Stop#offset} variable must be
 * the range 0.0 to 1.0 and act like keyframes along the gradient.
 * The offsets mark where the gradient should be exactly a particular color. </p>
 *
 * <p>If the proportional variable is set to true
 * then the start and end points of the gradient
 * should be specified relative to the unit square (0.0->1.0) and will
 * be stretched across the shape. If the proportional variable is set
 * to false, then the start and end points should be specified
 * in the local coordinate system of the shape and the gradient will
 * not be stretched at all.</p>
 *
 * <p>
 * The two filled rectangles in the example below will render the same.
 * The one on the left uses proportional coordinates to specify
 * the end points of the gradient.  The one on the right uses absolute
 * coordinates.  Both of them fill the specified rectangle with a
 * horizontal gradient that varies from black to red</p>
 *
 <PRE>
 // object bounding box relative (proportional = true)
 Stop[] stops = new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Color.RED)};
 LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
 Rectangle r1 = new Rectangle(0, 0, 100, 100);
 r1.setFill(lg1);

 // user space relative (proportional: = false)
 LinearGradient lg2 = new LinearGradient(125, 0, 225, 0, false, CycleMethod.NO_CYCLE, stops);
 Rectangle r2 = new Rectangle(125, 0, 100, 100);
 r2.setFill(lg2);
 </PRE>
 * @since JavaFX 2.0
 */
public final class LinearGradientImpl implements LinearGradient {
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private boolean proportional;
    private CycleMethod cycleMethod;
    private List<Stop> stops;
    private final boolean opaque;
    /**
     * The cached hash code, used to improve performance in situations where
     * we cache gradients, such as in the CSS routines.
     */
    private int hash;

    @Override
    public final double getStartX() {
        return startX;
    }

    @Override
    public final double getStartY() {
        return startY;
    }

    @Override
    public final double getEndX() {
        return endX;
    }

    @Override
    public final double getEndY() {
        return endY;
    }

    @Override
    public final boolean isProportional() {
        return proportional;
    }

    @Override
    public final CycleMethod getCycleMethod() {
        return cycleMethod;
    }

    @Override
    public final List<Stop> getStops() {
        return stops;
    }

    /**
     * @inheritDoc
     * @since JavaFX 8.0
     */
    @Override
    public final boolean isOpaque() {
        return opaque;
    }


    /**
     * Creates a new instance of LinearGradient.
     * @param startX the X coordinate of the gradient axis start point
     * @param startY the Y coordinate of the gradient axis start point
     * @param endX the X coordinate of the gradient axis end point
     * @param endY the Y coordinate of the gradient axis end point
     * @param proportional whether the coordinates are proportional
     * to the shape which this gradient fills
     * @param cycleMethod cycle method applied to the gradient
     * @param stops the gradient's color specification
     */
    public LinearGradientImpl(
            double startX,
            double startY,
            double endX,
            double endY,
            boolean proportional,
            CycleMethod cycleMethod,
            Stop... stops) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.proportional = proportional;
        this.cycleMethod = (cycleMethod == null) ? CycleMethod.NO_CYCLE: cycleMethod;
        this.stops = Stop.normalize(stops);
        this.opaque = determineOpacity();
    }

    /**
     * Creates a new instance of LinearGradient.
     * @param startX the X coordinate of the gradient axis start point
     * @param startY the Y coordinate of the gradient axis start point
     * @param endX the X coordinate of the gradient axis end point
     * @param endY the Y coordinate of the gradient axis end point
     * @param proportional whether the coordinates are proportional
     * to the shape which this gradient fills
     * @param cycleMethod cycle method applied to the gradient
     * @param stops the gradient's color specification
     */
    public LinearGradientImpl(
            double startX,
            double startY,
            double endX,
            double endY,
            boolean proportional,
            CycleMethod cycleMethod,
            List<Stop> stops) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.proportional = proportional;
        this.cycleMethod = (cycleMethod == null) ? CycleMethod.NO_CYCLE: cycleMethod;
        this.stops = Stop.normalize(stops);
        this.opaque = determineOpacity();
    }

    /**
     * Iterate over all the stops. If any one of them has a transparent
     * color, then we return false. If there are no stops, we return false.
     * Otherwise, we return true. Note that this is called AFTER Stop.normalize,
     * which ensures that we always have at least 2 stops.
     *
     * @return Whether this gradient is opaque
     */
    private boolean determineOpacity() {
        for (Stop stop : stops)
            if (!stop.getColor().isOpaque())
                return false;
        return true;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is equal to the {@code obj} argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof LinearGradientImpl))
            return false;
        final LinearGradientImpl other = (LinearGradientImpl) obj;
        if ((startX != other.startX) ||
                (startY != other.startY) ||
                (endX != other.endX) ||
                (endY != other.endY) ||
                (proportional != other.proportional) ||
                (cycleMethod != other.cycleMethod)) return false;
        return stops.equals(other.stops);
    }

    /**
     * Returns a hash code for this {@code LinearGradient} object.
     * @return a hash code for this {@code LinearGradient} object.
     */
    @Override
    public int hashCode() {
        if (hash == 0) {
            long bits = 17L;
            bits = 37L * bits + Double.doubleToLongBits(startX);
            bits = 37L * bits + Double.doubleToLongBits(startY);
            bits = 37L * bits + Double.doubleToLongBits(endX);
            bits = 37L * bits + Double.doubleToLongBits(endY);
            bits = 37L * bits + ((proportional) ? 1231L : 1237L);
            bits = 37L * bits + cycleMethod.hashCode();
            for (Stop stop: stops)
                bits = 37L * bits + stop.hashCode();
            hash = (int) (bits ^ (bits >> 32));
        }
        return hash;
    }

    /**
     * Returns a string representation of this {@code LinearGradient} object.
     * @return a string representation of this {@code LinearGradient} object.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("linear-gradient(from ")
                .append(GradientUtils.lengthToString(startX, proportional))
                .append(" ").append(GradientUtils.lengthToString(startY, proportional))
                .append(" to ").append(GradientUtils.lengthToString(endX, proportional))
                .append(" ").append(GradientUtils.lengthToString(endY, proportional))
                .append(", ");
        switch (cycleMethod) {
            case REFLECT:
                s.append("reflect");
                break;
            case REPEAT:
                s.append("repeat");
                break;
        }
        for (Stop stop : stops)
            s.append(", ").append(stop);
        return s.append(")").toString();
    }

    public static LinearGradient valueOf(String value) {
        if (value == null)
            throw new NullPointerException("gradient must be specified");

        String start = "linear-gradient(";
        String end = ")";
        if (value.startsWith(start)) {
            if (!value.endsWith(end))
                throw new IllegalArgumentException("Invalid gradient specification, must end with \"" + end + '"');
            value = value.substring(start.length(), value.length() - end.length());
        }

        GradientUtils.Parser parser = new GradientUtils.Parser(value);
        if (parser.getSize() < 2)
            throw new IllegalArgumentException("Invalid gradient specification");

        GradientUtils.Point startX = GradientUtils.Point.MIN;
        GradientUtils.Point startY = GradientUtils.Point.MIN;
        GradientUtils.Point endX = GradientUtils.Point.MIN;
        GradientUtils.Point endY = GradientUtils.Point.MIN;

        String[] tokens = parser.splitCurrentToken();
        if ("from".equals(tokens[0])) {
            GradientUtils.Parser.checkNumberOfArguments(tokens, 5);
            startX = parser.parsePoint(tokens[1]);
            startY = parser.parsePoint(tokens[2]);
            if (!"to".equals(tokens[3]))
                throw new IllegalArgumentException("Invalid gradient specification, \"to\" expected");
            endX = parser.parsePoint(tokens[4]);
            endY = parser.parsePoint(tokens[5]);
            parser.shift();
        } else if ("to".equals(tokens[0])) {
            int horizontalSet = 0;
            int verticalSet = 0;

            for (int i = 1; i < 3 && i < tokens.length; i++) {
                if ("left".equals(tokens[i])) {
                    startX = GradientUtils.Point.MAX;
                    endX = GradientUtils.Point.MIN;
                    horizontalSet++;
                } else if ("right".equals(tokens[i])) {
                    startX = GradientUtils.Point.MIN;
                    endX = GradientUtils.Point.MAX;
                    horizontalSet++;
                } else if ("top".equals(tokens[i])) {
                    startY = GradientUtils.Point.MAX;
                    endY = GradientUtils.Point.MIN;
                    verticalSet++;
                } else if ("bottom".equals(tokens[i])) {
                    startY = GradientUtils.Point.MIN;
                    endY = GradientUtils.Point.MAX;
                    verticalSet++;
                } else {
                    throw new IllegalArgumentException("Invalid gradient specification, unknown value after 'to'");
                }
            }
            if (verticalSet > 1)
                throw new IllegalArgumentException("Invalid gradient specification, vertical direction set twice after 'to'");
            if (horizontalSet > 1)
                throw new IllegalArgumentException("Invalid gradient specification, horizontal direction set twice after 'to'");
            parser.shift();
        } else {
            // default is "to bottom"
            startY = GradientUtils.Point.MIN;
            endY = GradientUtils.Point.MAX;
        }

        // repeat/reflect
        CycleMethod method = CycleMethod.NO_CYCLE;
        String currentToken = parser.getCurrentToken();
        if ("repeat".equals(currentToken)) {
            method = CycleMethod.REPEAT;
            parser.shift();
        } else if ("reflect".equals(currentToken)) {
            method = CycleMethod.REFLECT;
            parser.shift();
        }

        double dist = 0;
        if (!startX.proportional) {
            double dx = endX.value - startX.value;
            double dy = endY.value - startY.value;
            dist = Math.sqrt(dx*dx + dy*dy);
        }

        Stop[] stops = parser.parseStops(startX.proportional, dist);

        return new LinearGradientImpl(startX.value, startY.value, endX.value, endY.value, startX.proportional, method, stops);
    }
}