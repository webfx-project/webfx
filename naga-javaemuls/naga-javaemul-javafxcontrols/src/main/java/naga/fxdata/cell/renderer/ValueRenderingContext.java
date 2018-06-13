package naga.fxdata.cell.renderer;

import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public class ValueRenderingContext {

    public final static ValueRenderingContext DEFAULT_READONLY_CONTEXT = new ValueRenderingContext(true, null, null);

    private final boolean readOnly;
    private final Object labelKey;
    private final Object placeholderKey;
    private Property renderedValueProperty;

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

    public Property getRenderedValueProperty() {
        return renderedValueProperty;
    }

    public void setRenderedValueProperty(Property renderedValueProperty) {
        this.renderedValueProperty = renderedValueProperty;
    }
}
