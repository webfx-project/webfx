package naga.toolkit.drawing.paint;

import naga.toolkit.drawing.paint.impl.StopImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface Stop {

    /**
     * Gets a number ranging from {@code 0} to {@code 1}
     * that indicates where this gradient stop is placed. For linear gradients,
     * the {@code offset} variable represents a location along the gradient vector.
     * For radial gradients, it represents a percentage distance from
     * the focus point to the edge of the outermost/largest circle.
     *
     * @return position of the Stop within the gradient
     *         (ranging from {@code 0} to {@code 1})
     */
    double getOffset();

    Color getColor();

    static Stop create(double offset, Color color) {
        return new StopImpl(offset, color);
    }

    List<Stop> NO_STOPS = Collections.unmodifiableList(Arrays.asList(
                    Stop.create(0.0, Color.TRANSPARENT),
                    Stop.create(1.0, Color.TRANSPARENT)
            ));

    static List<Stop> normalize(Stop stops[]) {
        return normalize((stops == null ? null : Arrays.asList(stops)));
    }

    static List<Stop> normalize(List<Stop> stops) {
        if (stops == null)
            return NO_STOPS;
        Stop zerostop = null;
        Stop onestop = null;
        List<Stop> newlist = new ArrayList<Stop>(stops.size());
        for (Stop s : stops) {
            if (s == null || s.getColor() == null) continue;
            double off = s.getOffset();
            if (off <= 0.0) {
                if (zerostop == null || off >= zerostop.getOffset())
                    zerostop = s;
            } else if (off >= 1.0) {
                if (onestop == null || off < onestop.getOffset())
                    onestop = s;
            } else if (off == off) { // non-NaN
                for (int i = newlist.size() - 1; i >= 0; i--) {
                    Stop s2 = newlist.get(i);
                    if (s2.getOffset() <= off) {
                        if (s2.getOffset() == off) {
                            if (i > 0 && newlist.get(i-1).getOffset() == off)
                                newlist.set(i, s);
                            else
                                newlist.add(i+1, s);
                        } else
                            newlist.add(i+1, s);
                        s = null;
                        break;
                    }
                }
                if (s != null)
                    newlist.add(0, s);
            }
        }

        if (zerostop == null) {
            Color zerocolor;
            if (newlist.isEmpty()) {
                if (onestop == null)
                    return NO_STOPS;
                zerocolor = onestop.getColor();
            } else {
                zerocolor = newlist.get(0).getColor();
                if (onestop == null && newlist.size() == 1) {
                    // Special case for a single color with a non-0,1 offset.
                    // If we leave the color in there we end up with a 3-color
                    // gradient with all the colors being identical and we
                    // will not catch the optimization to a solid color.
                    newlist.clear();
                }
            }
            zerostop = new StopImpl(0.0, zerocolor);
        } else if (zerostop.getOffset() < 0.0)
            zerostop = new StopImpl(0.0, zerostop.getColor());
        newlist.add(0, zerostop);

        if (onestop == null)
            onestop = new StopImpl(1.0, newlist.get(newlist.size()-1).getColor());
        else if (onestop.getOffset() > 1.0)
            onestop = new StopImpl(1.0, onestop.getColor());
        newlist.add(onestop);

        return Collections.unmodifiableList(newlist);
    }

}