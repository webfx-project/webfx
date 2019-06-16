package webfx.fxkit.extra.displaydata.impl;

import webfx.fxkit.extra.cell.renderer.ValueRenderer;
import webfx.fxkit.extra.cell.renderer.ValueRenderingContext;
import webfx.fxkit.extra.displaydata.ColumnWidthCumulator;
import webfx.fxkit.extra.displaydata.DisplayColumn;
import webfx.fxkit.extra.displaydata.DisplayStyle;
import webfx.fxkit.extra.label.Label;
import webfx.fxkit.extra.type.Type;

/**
 * @author Bruno Salmon
 */
public final class DisplayColumnImpl implements DisplayColumn {

    private final Object headerValue;
    private final Label label;
    private final Type type;
    private final String role;
    private final DisplayStyle style;
    private ValueRenderer valueRenderer;
    private final ValueRenderingContext valueRenderingContext;
    private final ColumnWidthCumulator cumulator;
    private final Object source;

    public DisplayColumnImpl(Object label, Type type) {
        this(label, label, type, null, null, null, null, null);
    }

    public DisplayColumnImpl(Object headerValue, Object label, Type type, String role, DisplayStyle style, ValueRenderer valueRenderer, ColumnWidthCumulator cumulator, Object source) {
        this.headerValue = headerValue;
        this.label = Label.from(label);
        this.type = type;
        this.role = role;
        this.style = style != null ? style : DisplayStyle.NO_STYLE;
        this.valueRenderer = valueRenderer;
        valueRenderingContext = this.label == null ? ValueRenderingContext.DEFAULT_READONLY_CONTEXT : new ValueRenderingContext(true, this.label, null);
        this.cumulator = cumulator;
        this.source = source;
    }

    @Override
    public Object getHeaderValue() {
        return headerValue;
    }

    @Override
    public Label getLabel() {
        return label;
    }

    @Override
    public String getName() {
        return label.getText();
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public DisplayStyle getStyle() {
        return style;
    }

    @Override
    public ValueRenderer getValueRenderer() {
        if (valueRenderer == null)
            valueRenderer = ValueRenderer.create(getType());
        return valueRenderer;
    }

    @Override
    public ValueRenderingContext getValueRenderingContext() {
        return valueRenderingContext;
    }

    @Override
    public ColumnWidthCumulator getCumulator() {
        return cumulator;
    }

    @Override
    public Object getSource() {
        return source;
    }
}
