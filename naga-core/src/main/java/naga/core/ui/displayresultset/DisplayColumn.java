package naga.core.ui.displayresultset;

import naga.core.orm.domainmodel.Label;
import naga.core.type.Type;

/**
 * @author Bruno Salmon
 */
public class DisplayColumn {

    /**
     *  A header value that a column may hold.
     */
    private final Object headerValue;
    /**
     * The label (icon and text) to use if the column header is displayed on the screen.
     */
    private final Label label;
    /**
     * The expected type for the column values (for all rows).
     */
    private final Type type;
    /**
     * A specific role that this column is playing (ex: if the column contains the css class to apply to the row in a
     * table, or the series names in a chart using the column format, ...). Should be null if the column has the default
     * role for the visual component it is used for (the default role usually means the column contains domain data).
     */
    private final String role;

    public DisplayColumn(Object label, Type type) {
        this(label, label, type, null);
    }

    public DisplayColumn(Object headerValue, Object label, Type type, String role) {
        this.headerValue = headerValue;
        this.label = Label.from(label);
        this.type = type;
        this.role = role;
    }

    public Object getHeaderValue() {
        return headerValue;
    }

    public Label getLabel() {
        return label;
    }

    public String getName() {
        return label.getText();
    }

    public Type getType() {
        return type;
    }

    public String getRole() {
        return role;
    }
}
