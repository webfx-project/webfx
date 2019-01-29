package javafx.scene.text;

/**
 * Specifies whether the font is italicized
 * @since JavaFX 2.0
 */
public enum FontPosture {
    /**
     * represents regular.
     */
    REGULAR("", "regular"),
    /**
     * represents italic.
     */
    ITALIC("italic");

    private final String[] names;

    FontPosture(String... names) {
        this.names = names;
    }

    /**
     * Returns {@code FontPosture} by its name.
     *
     * @param name name of the {@code FontPosture}
     */
    public static FontPosture findByName(String name) {
        if (name == null)
            return null;

        for (FontPosture s : FontPosture.values())
            for (String n : s.names)
                if (n.equalsIgnoreCase(name))
                    return s;

        return null;
    }
}
