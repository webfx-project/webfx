package naga.toolkit.fx.scene.text;

/**
 * @author Bruno Salmon
 */
public class Font {

    private final String family;
    private final FontWeight weight;
    private final FontPosture posture;
    private final double size;

    public Font(String family, FontWeight weight, FontPosture posture, double size) {
        this.family = family;
        this.weight = weight;
        this.posture = posture;
        this.size = size;
    }

    public String getFamily() {
        return family;
    }

    public FontWeight getWeight() {
        return weight;
    }

    public FontPosture getPosture() {
        return posture;
    }

    public double getSize() {
        return size;
    }

    public static Font font(String family, FontWeight weight, FontPosture posture, double size) {
        return new Font(family, weight, posture, size);
    }

    public static Font font(String family, double size) {
        return font(family, null, null, size);
    }
}
