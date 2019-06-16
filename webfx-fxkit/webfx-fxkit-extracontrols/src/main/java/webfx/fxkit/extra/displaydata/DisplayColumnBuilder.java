package webfx.fxkit.extra.displaydata;

import webfx.fxkit.extra.type.Type;
import webfx.fxkit.extra.displaydata.impl.DisplayColumnImpl;
import webfx.fxkit.extra.cell.renderer.ValueRenderer;

/**
 * @author Bruno Salmon
 */
public final class DisplayColumnBuilder {

    private Object headerValue;
    private Object label;
    private Type type;
    private String role;
    private DisplayStyle style;
    private ValueRenderer valueRenderer;
    private ColumnWidthCumulator cumulator;
    private Object source;

    public  DisplayColumnBuilder(Object label, Type type) {
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

    public DisplayColumnBuilder setStyle(DisplayStyle style) {
        this.style = style;
        return this;
    }

    public DisplayColumnBuilder setValueRenderer(ValueRenderer valueRenderer) {
        this.valueRenderer = valueRenderer;
        return this;
    }

    public DisplayColumnBuilder setCumulator(ColumnWidthCumulator cumulator) {
        this.cumulator = cumulator;
        return this;
    }

    public DisplayColumnBuilder setSource(Object source) {
        this.source = source;
        return this;
    }

    public DisplayColumn build() {
        return new DisplayColumnImpl(headerValue, label, type, role, style, valueRenderer, cumulator, source);
    }

    public static DisplayColumnBuilder create(Object label) {
        return new DisplayColumnBuilder(label, null);
    }

    public static DisplayColumnBuilder create(Object label, Type type) {
        return new DisplayColumnBuilder(label, type);
    }
}
