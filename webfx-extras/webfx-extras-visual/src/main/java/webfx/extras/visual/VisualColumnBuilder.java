package webfx.extras.visual;

import webfx.extras.visual.impl.VisualColumnImpl;
import webfx.extras.type.Type;
import webfx.extras.cell.renderer.ValueRenderer;

/**
 * @author Bruno Salmon
 */
public final class VisualColumnBuilder {

    private Object headerValue;
    private Object label;
    private Type type;
    private String role;
    private VisualStyle style;
    private ValueRenderer valueRenderer;
    private ColumnWidthCumulator cumulator;
    private Object source;

    public VisualColumnBuilder(Object label, Type type) {
        this.label = headerValue = label;
        this.type = type;
    }

    public VisualColumnBuilder setHeaderValue(Object headerValue) {
        this.headerValue = headerValue;
        return this;
    }

    public VisualColumnBuilder setLabel(Object label) {
        this.label = label;
        return this;
    }

    public VisualColumnBuilder setType(Type type) {
        this.type = type;
        return this;
    }

    public VisualColumnBuilder setRole(String role) {
        this.role = role;
        return this;
    }

    public VisualColumnBuilder setStyle(VisualStyle style) {
        this.style = style;
        return this;
    }

    public VisualColumnBuilder setValueRenderer(ValueRenderer valueRenderer) {
        this.valueRenderer = valueRenderer;
        return this;
    }

    public VisualColumnBuilder setCumulator(ColumnWidthCumulator cumulator) {
        this.cumulator = cumulator;
        return this;
    }

    public VisualColumnBuilder setSource(Object source) {
        this.source = source;
        return this;
    }

    public VisualColumn build() {
        return new VisualColumnImpl(headerValue, label, type, role, style, valueRenderer, cumulator, source);
    }

    public static VisualColumnBuilder create(Object label) {
        return new VisualColumnBuilder(label, null);
    }

    public static VisualColumnBuilder create(Object label, Type type) {
        return new VisualColumnBuilder(label, type);
    }
}
