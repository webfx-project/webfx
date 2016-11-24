package naga.toolkit.drawing.text;

import naga.toolkit.drawing.text.impl.FontImpl;

/**
 * @author Bruno Salmon
 */
public interface Font {

     /**
     * Returns the family of this font.
     */
    String getFamily();

    FontWeight getWeight();

    FontPosture getPosture();

    /**
     * The point size for this font. This may be a fractional value such as
     * {@code 11.5}. If the specified value is < 0 the default size will be
     * used.
     */
    double getSize();

    static Font font(String family, FontWeight weight, FontPosture posture, double size) {
        return new FontImpl(family, weight, posture, size);
    }

    static Font font(String family, double size) {
        return font(family, null, null, size);
    }


}
