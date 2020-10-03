package webfx.demos.mandelbrot.mandelbrotmodel;


/**
 * A simple class that simply holds a palette length and a palette offset.
 * These values determine how the colors from a palette are assigned to
 * iteration counts in a MandelbrotDisplay.
 */
public class PaletteMapping implements Cloneable {

/*
    private final ChangeEvent changeEvent = new ChangeEvent(this);
    private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
*/

    private int length;
    private int offset;

    public PaletteMapping() {
    }

    public PaletteMapping(int length, int offset) {
        this.length = length;
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    /**
     * Set the palette length, which should be greater than or equal to zero.
     * A value of zero means that the number of colors in the palette will be
     * equal to the maximum number of iterations in the MandelbrotDisplay.
     */
    public void setLength(int length) {
        if (length < 0)
            length = 0;
        if (this.length == length)
            return;
        this.length = length;
        //changed();
    }

    public int getOffset() {
        return offset;
    }

    /**
     * Set the offset for the palette.  Colors in the palette are rotated by
     * this many positions withion the array of colors.
     */
    public void setOffset(int offset) {
        if (this.offset == offset)
            return;
        this.offset = offset;
        //changed();
    }

    /**
     * Add a listener that will be notified of any changes in this object.
     */
/*
    public void addChangeListener(ChangeListener l) {
        if (!listeners.contains(l))
            listeners.add(l);
    }
*/

    /**
     * Remove a registered listener from this object (if present).
     */
/*
    public void removeChangeListener(ChangeListener l) {
        listeners.remove(l);
    }
*/

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PaletteMapping))
            return false;
        PaletteMapping that = (PaletteMapping)obj;
        return that.length == length && that.offset == offset;
    }

    public PaletteMapping clone() {
        return new PaletteMapping(length,offset);
    }

/*
    private void changed() {
        for (ChangeListener l : listeners)
            l.stateChanged(changeEvent);
    }
*/

}
