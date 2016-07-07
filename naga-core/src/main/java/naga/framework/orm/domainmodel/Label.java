package naga.framework.orm.domainmodel;

import naga.framework.expression.terms.As;
import naga.framework.expression.terms.Symbol;

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

    public static Label from(Object o) {
        if (o instanceof Label)
            return (Label) o;
        if (o instanceof HasLabel)
            return ((HasLabel) o).getLabel();
        if (o instanceof Symbol)
            return new Label(((Symbol) o).getName());
        if (o instanceof As)
            return new Label(((As) o).getAlias());
        if (o instanceof String)
            return new Label((String) o);
        return emptyLabel;
    }
}
