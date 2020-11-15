package webfx.demo.enzoclocks.circlespacker;

/**
 * @author Bruno Salmon
 */
final class HoneyCombCirclesPacker extends CirclesPackerBase {

    private double patternHeight;
    private double bigRadius; // Before truncation
    private double dx;
    private int vc;
    private int count1, count2, count12;

    @Override
    protected void computeCirclesRadius() {
        // Note: we always think horizontally in the computation (we always have w >= h)
        final double w2 = w * w, h2 = h * h;
        for (int vc = 2; vc <= count; vc++) { // vc = number of vertical circles (min 2 for honey comb)
            int hc1 = count / vc, hc2 = hc1; // hc1 = number of horizontal circles on first line of the pattern (second line for hc2)
            if (hc1 * vc < count) { // if not enough circles, we will need to increase hc1 or hc2 or both
                int patternCount = (hc1 + hc2 + 1) * (vc / 2);
                if (vc % 2 == 0) {
                    hc1++;
                    if (patternCount < count)
                        hc2++;
                } else if (patternCount + hc1 >= count)
                    hc2++;
                else if (patternCount + hc1 + 1 >= count)
                    hc1++;
                else {
                    hc1++;
                    hc2++;
                }
            }
            int hc12 = hc1 + hc2;
            if (hc12 < vc)
                break;
            boolean sameHc = hc1 == hc2;
            int maxHc = Math.max(hc1, hc2);
            int minHc = Math.min(hc1, hc2);
            final double a, b, c; // a * r2 + b * r + c = 0
            final double p = vc - 1, p2 = p * p;
            if (sameHc) { // Same count for every lines
                int q = 2 * hc1 - 1, q2 = q * q;
                a = 4 * (1 - q2 * (1 - 1 / p2));
                b = 4 * (w + q2 / p2 * h);
                c = w2 + q2 / p2 * h2;
            } else { // Alternate count for even and odd lines
                double q2 = minHc * minHc;
                a = 4 * (1 / p2 + 1d / 4 / q2 - 1);
                b = 4 * h / p2 + w / q2;
                c = h2 / p2 + w2 / 4 / q2;
            }
            final double d = b * b - 4 * a * c;
            if (d < 0)
                continue;
            double br = (b - Math.sqrt(d)) / 2 / a;
            int maxVRadiusCount = vc == 2 ? 2 : vc + 1;
            br = Math.min(br, h / maxVRadiusCount);
            int maxHRadiusCount = 2 * maxHc + (sameHc ? 1 : 0);
            double r = Math.min(br, w / maxHRadiusCount);
            if (r > radius) {
                radius = r;
                bigRadius = br;
                dx = (w - 2 * r) / (maxHRadiusCount - 2);
                double dy = (2 * vc * br - h) / p;
                patternHeight = 4 * br - 2 * dy;
                count1 = hc1;
                count2 = hc2;
                count12 = hc12;
                this.vc = vc;
            }
        }
    }

    @Override
    protected void computeCircleCenterCoordinates(int index) {  // index of the clock
        // Computing the pattern number (0 = first pattern, 1 = first repetition, 2 = second repetition, etc...) and
        // the pattern index within that repeated pattern. The pattern has 2 lines of circles (count1 + count2 = count12).
        int patternNumber = index / count12;
        int patternIndex  = index % count12;
        // Determining what row to shift to right (or bottom) to make the honey comb effect (either row1 or row2)
        int shiftedRow = count1 < count2 ? 0 : 1; // It will the row with less circles
        // Now we need to map the pattern index into a row and column in the pattern
        int patternRow, patternColumn;
        if (vc > 2) { // General case, we use a simple mapping: walking through first row (=0) and then second row (=1)
            patternRow    = patternIndex < count1 ? 0 : 1;
            patternColumn = patternIndex < count1 ? patternIndex : patternIndex - count1;
        } else { // Using another mapping when we have just 2 lines of circles (either horizontally or vertically) in
            // order to get a smooth transition if the user continue shrinking the window up to a single horizontal row
            // or vertical column (which will then be displayed by the grid circles packer). To get this smooth
            // transition (ie with no circles positions sudden change), the circles must be ordered in the same way as
            // for the grid circles packer (when displaying 1 single row or single column), so according to their
            // horizontal (or vertical) position.
            patternRow = patternIndex % 2;
            if (shiftedRow == 0)
                patternRow = 1 - patternRow;
            patternColumn = patternIndex / 2;
        }
        // Finally computing the clock center coordinate for that pattern row and column
        double hcx = radius + patternColumn * 2 * dx + (patternRow == shiftedRow ? dx : 0);
        double hcy = patternNumber * patternHeight + bigRadius + (patternRow == 0 ? 0 : patternHeight / 2);
        cx = horizontal ? hcx : hcy;
        cy = horizontal ? hcy : hcx;
    }
}
