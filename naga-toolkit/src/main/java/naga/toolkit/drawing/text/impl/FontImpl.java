package naga.toolkit.drawing.text.impl;

import naga.toolkit.drawing.text.Font;
import naga.toolkit.drawing.text.FontPosture;
import naga.toolkit.drawing.text.FontWeight;

/**
 * @author Bruno Salmon
 */
public class FontImpl implements Font {

    private final String family;
    private final FontWeight weight;
    private final FontPosture posture;
    private final double size;

    public FontImpl(String family, FontWeight weight, FontPosture posture, double size) {
        this.family = family;
        this.weight = weight;
        this.posture = posture;
        this.size = size;
    }

    @Override
    public String getFamily() {
        return family;
    }

    @Override
    public FontWeight getWeight() {
        return weight;
    }

    @Override
    public FontPosture getPosture() {
        return posture;
    }

    @Override
    public double getSize() {
        return size;
    }
}
