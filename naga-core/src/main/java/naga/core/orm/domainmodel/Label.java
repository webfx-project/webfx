package naga.core.orm.domainmodel;

/**
 * @author Bruno Salmon
 */
public class Label {

    private String text;
    private String iconPath;

    public static Label emptyLabel = new Label("");

    public Label(String text) {
        this(text, null);
    }

    public Label(String text, String iconPath) {
        setText(text);
        this.iconPath = iconPath;
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

    /*public static Label from(Object o) {
        return o instanceof HasLabel ? ((HasLabel) o).getLabel() : null;
    }*/
}
