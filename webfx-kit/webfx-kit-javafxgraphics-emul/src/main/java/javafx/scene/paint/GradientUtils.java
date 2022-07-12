package javafx.scene.paint;

import dev.webfx.platform.util.collection.Collections;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
class GradientUtils {

    static String lengthToString(double value, boolean proportional) {
        return proportional ? (value * 100) + "%" : value + "px";
    }

    static class Point {

        static final Point MIN = new Point(0, true);
        static final Point MAX = new Point(1, true);
        double value;
        boolean proportional;

        @Override
        public String toString() {
            return "value = " + value + ", proportional = " + proportional;
        }

        Point(double value, boolean proportional) {
            this.value = value;
            this.proportional = proportional;
        }

        Point() {
        }
    }

    static class Parser {

        private int index;
        private String[] tokens;
        private boolean proportional;
        private boolean proportionalSet = false;

        private interface Delimiter {
            boolean isDelimiter(char value);
        }

        private String[] splitString(String string, Delimiter delimiter, boolean canRepeat) {
            List<String> tokenList = new LinkedList<>();
            StringBuilder token = new StringBuilder();
            int i = 0;
            char[] input = string.toCharArray();
            while (i < input.length) {
                char currentChar = input[i];

                if (delimiter.isDelimiter(currentChar)) {
                    if (!canRepeat || token.length() > 0)
                        tokenList.add(token.toString());
                    token.setLength(0);
                } else if (currentChar == '(') {
                    while (i < input.length) {
                        token.append(input[i]);
                        if (input[i] == ')') {
                            break;
                        }
                        i++;
                    }
                } else
                    token.append(input[i]);
                i++;
            }
            if (!canRepeat || token.length() > 0)
                tokenList.add(token.toString());

            return Collections.toArray(tokenList, String[]::new);
        }

        Parser(String content) {
            tokens = splitString(content, value -> (value == ','), false);
            index = 0;
        }

        int getSize() {
            return tokens.length;
        }

        void shift() {
            index++;
        }

        String getCurrentToken() {
            String currentToken = tokens[index].trim();
            if (currentToken.isEmpty())
                throw new IllegalArgumentException("Invalid gradient specification: found empty token.");
            return currentToken;
        }

        String[] splitCurrentToken() {
            return getCurrentToken().split("\\s");
        }

        static void checkNumberOfArguments(String[] tokens, int count) {
            if (tokens.length < count + 1)
                throw new IllegalArgumentException("Invalid gradient specification: parameter '"+ tokens[0] + "' needs " + count + " argument(s).");
        }

        static double parseAngle(String value) {
            if (value.endsWith("deg"))
                return Double.parseDouble(value.substring(0, value.length() - 3));
            if (value.endsWith("grad"))
                return Double.parseDouble(value.substring(0, value.length() - 4)) * 9 / 10;
            if (value.endsWith("rad"))
                return Double.parseDouble(value.substring(0, value.length() - 3)) * 180 / Math.PI;
            if (value.endsWith("turn"))
                return Double.parseDouble(value.substring(0, value.length() - 4)) * 360;
            throw new IllegalArgumentException("Invalid gradient specification: angle must end in deg, rad, grad, or turn");
        }

        static double parsePercentage(String value) {
            if (!value.endsWith("%"))
                throw new IllegalArgumentException("Invalid gradient specification: focus-distance must be specified as percentage");
            return Double.parseDouble(value.substring(0, value.length() - 1)) / 100;
        }

        Point parsePoint(String value) {
            Point p = new Point();
            if (p.proportional = value.endsWith("%"))
                value = value.substring(0, value.length() - 1);
            else if (value.endsWith("px"))
                value = value.substring(0, value.length() - 2);
            p.value = Double.parseDouble(value);
            if (p.proportional)
                p.value /= 100;

            if (proportionalSet && proportional != p.proportional)
                throw new IllegalArgumentException("Invalid gradient specification: cannot mix proportional and non-proportional values");

            proportionalSet = true;
            proportional = p.proportional;

            return p;
        }

        // length specifies the length of gradient line used when recalculating
        // non-proportional color-stops
        Stop[] parseStops(boolean proportional, double length) {
            int stopsCount = tokens.length - index;
            Color[] colors = new Color[stopsCount];
            double[] offsets = new double[stopsCount];
            Stop[] stops = new Stop[stopsCount];

            for (int i = 0; i < stopsCount; i++) {
                String stopString = tokens[i + index].trim();
                String[] stopTokens = splitString(stopString, Character::isWhitespace, true);

                if (stopTokens.length == 0)
                    throw new IllegalArgumentException("Invalid gradient specification, empty stop found");

                String currentToken = stopTokens[0];
                double offset = -1;

                Color c = Color.web(currentToken);
                if (stopTokens.length == 2) {
                    // parsing offset
                    String o = stopTokens[1];
                    if (o.endsWith("%")) {
                        o = o.substring(0, o.length() - 1);
                        offset = Double.parseDouble(o) / 100;
                    } else if (!proportional) {
                        if (o.endsWith("px"))
                            o = o.substring(0, o.length() - 2);
                        offset = Double.parseDouble(o) / length;
                    } else
                        throw new IllegalArgumentException("Invalid gradient specification, non-proportional stops not permitted in proportional gradient: " + o);
                } else if (stopTokens.length > 2)
                    throw new IllegalArgumentException("Invalid gradient specification, unexpected content in stop specification: " + stopTokens[2]);

                colors[i] = c;
                offsets[i] = offset;
            }

            // normalize based on CSS specification
            // If the first color-stop does not have a position, set its position to 0%.
            // If the last color-stop does not have a position, set its position to 100%.
            if (offsets[0] < 0)
                offsets[0] = 0;
            if (offsets[offsets.length - 1] < 0)
                offsets[offsets.length - 1] = 1;

            // If a color-stop has a position that is less than the specified position
            // of any color-stop before it in the list, set its position to be equal
            // to the largest specified position of any color-stop before it.
            double max = offsets[0];
            for (int i = 1; i < offsets.length; i++) {
                if (offsets[i] < max && offsets[i] > 0)
                    offsets[i] = max;
                else
                    max = offsets[i];
            }

            // If any color-stop still does not have a position, then,
            // for each run of adjacent color-stops without positions,
            // set their positions so that they are evenly spaced
            // between the preceding and following color-stops with positions.
            for (int i = 1, firstIndex = -1; i < offsets.length; i++) {
                double offset = offsets[i];
                if (offset < 0 && firstIndex < 0)
                    firstIndex = i;
                else if (offset >= 0 && firstIndex > 0) {
                    int n = i - firstIndex + 1;
                    double part = (offsets[i] - offsets[firstIndex - 1]) / n;
                    for (int j = 0; j < n - 1; j++)
                        offsets[firstIndex + j] = offsets[firstIndex - 1] + part * (j + 1);
                }
            }

            for (int i = 0; i < stops.length; i++)
                stops[i] = new Stop(offsets[i], colors[i]);

            return stops;
        }
    }
}
