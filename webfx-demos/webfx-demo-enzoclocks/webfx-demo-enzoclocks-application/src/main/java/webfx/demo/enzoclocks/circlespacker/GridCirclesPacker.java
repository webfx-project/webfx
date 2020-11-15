package webfx.demo.enzoclocks.circlespacker;

/**
 * @author Bruno Salmon
 */
final class GridCirclesPacker extends CirclesPackerBase {

    private int hCount, vCount;
    private double hMargin, vMargin;

    @Override
    protected void computeCirclesRadius() {
        for (int vc = 1; vc <= count; vc++) {
            int hc = count / vc;
            if (hc * vc < count)
                hc++;
            if (hc < vc)
                break;
            double r = Math.min(w / hc, h / vc) / 2;
            if (r > radius) {
                radius = r;
                hCount = horizontal ? hc : vc;
                vCount = horizontal ? vc : hc;
            }
        }
        hMargin = (width  - hCount * radius * 2) / (hCount + 1);
        vMargin = (height - vCount * radius * 2) / (vCount + 1);
    }

    @Override
    protected void computeCircleCenterCoordinates(int index) {
        int column = index % hCount;
        int row = index / hCount;
        cx = radius * (1 + 2 * column) + hMargin * (1 + column);
        cy = radius * (1 + 2 * row)    + vMargin * (1 + row);
    }
}
