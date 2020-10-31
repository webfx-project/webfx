package webfx.demo.raytracer.math;

/**
 * @author Bruno Salmon
 */
public final class RayTracerMath {

    private static int WIDTH, HEIGHT;
    private static View VIEW;

    public static byte[] createLinePixelResultStorage(int canvasWidth, byte[] rgbs) {
        int length = 3 * canvasWidth;
        if (rgbs != null && rgbs.length == length)
            return rgbs;
        return new byte[length];
    }

    public static void init(int width, int height, int placeIndex, int frameIndex) {
        WIDTH = width;
        HEIGHT = height;
        VIEW = Views.VIEWS[placeIndex];
        double q = frameIndex * 0.005;
        double a = 0.55 + q * 6 * 2 * Math.PI;
        double cy = 1.55;
        double r =  2.55;
        double q1 = q - 0.20;
        if (q1 > 0) {
            cy += 20 * Math.min(1, q1);
            r  += 20 * Math.sin(Math.min(1, q1) * Math.PI / 2);
        }
        double cx = r * Math.cos(a);
        double cz = r * Math.sin(a);
        VIEW.camera = Camera.create(Vector.create(0, 1, 0), Vector.create(cx, cy, cz));

    }

    public static void computeLinePixelRGBs(int cy, byte[] rgbs) {
        int index = 0;
        for (int cx = 0; cx < WIDTH; cx++)
            index = computeAndStorePixelResult(cx, cy, rgbs, index);
    }

    public static void computeAndStorePixelResult(int cx, int cy, byte[] rgbs) {
        computeAndStorePixelResult(cx, cy, rgbs, 3 * cx);
    }

    public static int computeAndStorePixelResult(int cx, int cy, byte[] rgbs, int index) {
        Colour colour = RayTracer.renderPixel(cx, cy, VIEW, WIDTH, HEIGHT);
        rgbs[index++] = (byte) (colour.checkColour(colour.R) * 255 + Byte.MIN_VALUE);
        rgbs[index++] = (byte) (colour.checkColour(colour.G) * 255 + Byte.MIN_VALUE);
        rgbs[index++] = (byte) (colour.checkColour(colour.B) * 255 + Byte.MIN_VALUE);
        return index;
    }

}
