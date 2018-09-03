package webfx.fxkits.extra.cell.renderer;

import emul.javafx.beans.value.ObservableValue;

/**
 * @author Bruno Salmon
 */
public class ValueRenderingContext {

    public final static ValueRenderingContext DEFAULT_READONLY_CONTEXT = new ValueRenderingContext(true, null, null);

    private final boolean readOnly;
    private final Object labelKey;
    private final Object placeholderKey;
    private ObservableValue editedValueProperty;

    public ValueRenderingContext(boolean readOnly, Object labelKey, Object placeholderKey) {
        this.readOnly = readOnly;
        this.labelKey = labelKey;
        this.placeholderKey = placeholderKey;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public Object getLabelKey() {
        return labelKey;
    }

    public Object getPlaceholderKey() {
        return placeholderKey;
    }

    public ObservableValue getEditedValueProperty() {
        return editedValueProperty;
    }

    public void setEditedValueProperty(ObservableValue editedValueProperty) {
        this.editedValueProperty = editedValueProperty;
    }
}
