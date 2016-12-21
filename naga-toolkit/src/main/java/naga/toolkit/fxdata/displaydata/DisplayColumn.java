package naga.toolkit.fxdata.displaydata;

import naga.commons.type.Type;
import naga.toolkit.fxdata.displaydata.impl.DisplayColumnImpl;
import naga.toolkit.fxdata.cell.renderer.ValueRenderer;

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

    /**
     * @return The style information to apply when rendering this column.
     */
    DisplayStyle getStyle();

    /**
     * @return The value renderer to be used for this column to transform the cell values into graphical nodes.
     */
    ValueRenderer getValueRenderer();

    /**
     * Quick factory method for a simple DisplayColumn creation with just a label and type. Use the DisplayColumnBuilder
     * for more complex cases.
     */
    static DisplayColumn create(Object label, Type type) {
        return new DisplayColumnImpl(label, type);
    }

    static DisplayColumn create(Object label, Type type, DisplayStyle style) {
        return new DisplayColumnImpl(label, label, type, null, style, null);
    }

    static DisplayColumn create(ValueRenderer valueRenderer) {
        return new DisplayColumnImpl(null, null, null, null, null, valueRenderer);
    }
}
