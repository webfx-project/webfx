package naga.toolkit.display;

import naga.commons.type.Type;
import naga.toolkit.display.impl.DisplayColumnImpl;

/**
 * @author Bruno Salmon
 */
public interface DisplayColumn {

    /**
     * @return a header value that a column may hold.
     */
    Object getHeaderValue();

    /**
     * @return the label (icon and text) to use if the column header is displayed on the screen.
     */
    Label getLabel();

    /**
     * @return the name of the column (which is the text defined in label)
     */
    String getName();

    /**
     * @return The expected type for the column values (for all rows).
     */
    Type getType();

    /**
     * @return a specific role that this column is playing (ex: if the column contains the css class to apply to the row in a
     * table, or the series names in a chart using the column format, ...). Should be null if the column has the default
     * role for the visual component it is used for (the default role usually means the column contains domain data).
     */
    String getRole();

    Double getPrefWidth();

    static DisplayColumn create(Object label, Type type) {
        return new DisplayColumnImpl(label, type);
    }

    static DisplayColumn create(Object label, Type type, Double prefWidth) {
        return new DisplayColumnImpl(label, label, type, null, prefWidth);
    }

    static DisplayColumn create(Object headerValue, Object label, Type type, String role) {
        return new DisplayColumnImpl(headerValue, label, type, role, null);
    }
}
