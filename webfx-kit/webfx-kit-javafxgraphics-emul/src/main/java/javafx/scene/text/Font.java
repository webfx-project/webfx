package javafx.scene.text;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Bruno Salmon
 */
public class Font {

    private static final String DEFAULT_FAMILY = "System";

    private final String name;
    private final String family;
    private final FontWeight weight;
    private final FontPosture posture;
    private final double size;

    private final String url;

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
     * Searches for an appropriate font based on the given font family name and
     * default font size.
     * This method is not guaranteed to return a specific font, but does
     * its best to find one that fits the specified requirements. A null or empty
     * value for family allows the implementation to select any suitable font.
     *
     * @param family The family of the font
     * @return The font that best fits the specified requirements.
     */
    public static Font font(String family) {
        return font(family, null, null, -1);
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
        this(family, family, weight, posture, size);
    }

    public Font(String name, String family, FontWeight weight, FontPosture posture, double size) {
        this(name, family, weight, posture, size, null);
    }

    private Font(String name, String family, FontWeight weight, FontPosture posture, double size, String url) {
        this.name = name;
        this.family = family != null ? family : DEFAULT_FAMILY;
        this.weight = weight != null ? weight : FontWeight.NORMAL;
        this.posture = posture != null ? posture : FontPosture.REGULAR;
        this.size = size;
        this.url = url;
    }

    public String getName() {
        return name;
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

    public String getUrl() {
        return url;
    }

    public static Font font(String family, FontWeight weight, FontPosture posture, double size) {
        return new Font(family, weight, posture, size);
    }

    public static Font font(String family, FontPosture posture, double size) {
        return font(family, null, posture, size);
    }

    public static Font font(String family, FontWeight weight, double size) {
        return font(family, weight, null, size);
    }

    public static Font font(String family, double size) {
        return new Font(family, size);
    }

    public static Font font(double size) {
        return new Font(size);
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
            DEFAULT = font(DEFAULT_FAMILY, FontWeight.SEMI_BOLD, FontPosture.REGULAR, 15);
        return DEFAULT;
    }

    public static Font loadFont(String url, double size) {
        // Guessing the font properties from the url:
        String name = url;
        FontWeight weight = FontWeight.NORMAL;
        FontPosture posture = FontPosture.REGULAR;
        // 1) removing the path (for both name and family)
        int slashIndex = name.lastIndexOf('/');
        if (slashIndex > 0)
            name = name.substring(slashIndex + 1);
        String family = name;
        // 2) removing the extension from family (ex: .ttf)
        int dotIndex = family.lastIndexOf('.');
        if (dotIndex > 0)
            family = family.substring(0, dotIndex);
        // 3) Removing the dash from family (ex: Bitwise-m19x => Bitwise). TODO: Also setting the posture (ex: Roboto-Italic)
        int dashIndex = family.lastIndexOf('-');
        if (dashIndex > 0)
            family = family.substring(0, dashIndex);
        Font font = new Font(name, family, weight, posture, size, url);
        // Adding the font to those that need to be loaded by the peer (see HtmlScenePeer)
        REQUESTED_FONTS.add(font);
        return font;
    }

    private final static ObservableList<Font> REQUESTED_FONTS = FXCollections.observableArrayList();
    private final static ObservableList<Font> LOADING_FONTS = FXCollections.observableArrayList();

    public static ObservableList<Font> getRequestedFonts() {
        return REQUESTED_FONTS;
    }

    public static ObservableList<Font> getLoadingFonts() {
        return LOADING_FONTS;
    }


}
