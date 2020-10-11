package webfx.demos.mandelbrot.canvastracer;


/**
 * A simple class that simply holds a palette length and a palette offset.
 * These values determine how the colors from a palette are assigned to
 * iteration counts in a MandelbrotDisplay.
 */
public class PaletteMapping {

    private final int length;
    private final int offset;

    public PaletteMapping(int length, int offset) {
        this.length = length;
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public int getOffset() {
        return offset;
    }

}
