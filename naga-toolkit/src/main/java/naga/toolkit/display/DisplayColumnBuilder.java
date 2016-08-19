package naga.toolkit.display;

import naga.commons.type.Type;
import naga.toolkit.cell.renderers.ValueRenderer;
import naga.toolkit.display.impl.DisplayColumnImpl;

/**
 * @author Bruno Salmon
 */
public class DisplayColumnBuilder {

    private Object headerValue;
    private Object label;
    private Type type;
    private String role;
    private Double prefWidth;
    private String textAlign;
    private ValueRenderer valueRenderer;

    private DisplayColumnBuilder(Object label, Type type) {
        this.label = headerValue = label;
        this.type = type;
    }

    public DisplayColumnBuilder setHeaderValue(Object headerValue) {
        this.headerValue = headerValue;
        return this;
    }

    public DisplayColumnBuilder setLabel(Object label) {
        this.label = label;
        return this;
    }

    public DisplayColumnBuilder setType(Type type) {
        this.type = type;
        return this;
    }

    public DisplayColumnBuilder setRole(String role) {
        this.role = role;
        return this;
    }

    public DisplayColumnBuilder setPrefWidth(Double prefWidth) {
        this.prefWidth = prefWidth;
        return this;
    }

    public DisplayColumnBuilder setTextAlign(String textAlign) {
        this.textAlign = textAlign;
        return this;
    }

    public DisplayColumnBuilder setValueRenderer(ValueRenderer valueRenderer) {
        this.valueRenderer = valueRenderer;
        return this;
    }

    public DisplayColumn build() {
        return new DisplayColumnImpl(headerValue, label, type, role, prefWidth, textAlign, valueRenderer);
    }

    public static DisplayColumnBuilder create(Object label, Type type) {
        return new DisplayColumnBuilder(label, type);
    }

}
