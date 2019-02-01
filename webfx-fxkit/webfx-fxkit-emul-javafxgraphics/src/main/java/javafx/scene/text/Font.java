package javafx.scene.text;

/**
 * @author Bruno Salmon
 */
public class Font {

    private static final String DEFAULT_FAMILY = "System";

    private final String family;
    private final FontWeight weight;
    private final FontPosture posture;
    private final double size;

    /**
     * Constructs a font using the default face "System".
     * The underlying font used is determined by the implementation
     * based on the typical UI font for the current UI environment.
     *
     * @param size the font size to use
     */
    public Font(double size) {
        this(null, size);
    }


    /**
     * Constructs a font using the specified full face name and size
     * @param name full name of the font.
     * @param size the font size to use
     */
    public Font(String name, double size) {
        this(name, null, null, size);
    }

    public Font(String family, FontWeight weight, FontPosture posture, double size) {
        this.family = family != null ? family : DEFAULT_FAMILY;
        this.weight = weight != null ? weight : FontWeight.NORMAL;
        this.posture = posture != null ? posture : FontPosture.REGULAR;
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

    private static Font DEFAULT;
    /**
     * Gets the default font which will be from the family "System",
     * and typically the style "Regular", and be of a size consistent
     * with the user's desktop environment, to the extent that can
     * be determined.
     * @return The default font.
     */
    public static synchronized Font getDefault() {
        if (DEFAULT == null)
            DEFAULT = font("Roboto", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 15);
        return DEFAULT;
    }

}
