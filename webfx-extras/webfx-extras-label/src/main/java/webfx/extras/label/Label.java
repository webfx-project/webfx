package webfx.extras.label;

/**
 * @author Bruno Salmon
 */
public final class Label {

    private final String code; // Can be used by i18n as translation key
    private String text;
    private final String iconPath;

    public static final Label emptyLabel = new Label("");

    public Label(String text) {
        this(null, text, null);
    }

    public Label(String code, String text, String iconPath) {
        this.code = code != null ? code : text; // temporary while all codes are not set
        this.text = text;
        this.iconPath = iconPath;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconPath() {
        return iconPath;
    }

    public static Label from(Object o) {
        if (o instanceof Label)
            return (Label) o;
        if (o instanceof HasLabel)
            return ((HasLabel) o).getLabel();
/*
        if (o instanceof Symbol)
            return new Label(((Symbol) o).getName());
        if (o instanceof As)
            return new Label(((As) o).getAlias());
*/
        if (o instanceof String)
            return new Label((String) o);
        return emptyLabel;
    }
}
