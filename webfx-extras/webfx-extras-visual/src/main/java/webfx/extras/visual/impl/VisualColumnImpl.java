package webfx.extras.visual.impl;

import webfx.extras.type.Type;
import webfx.extras.cell.renderer.ValueRenderer;
import webfx.extras.cell.renderer.ValueRenderingContext;
import webfx.extras.visual.ColumnWidthCumulator;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualStyle;
import webfx.extras.label.Label;

/**
 * @author Bruno Salmon
 */
public final class VisualColumnImpl implements VisualColumn {

    private final Object headerValue;
    private final Label label;
    private final Type type;
    private final String role;
    private final VisualStyle style;
    private ValueRenderer valueRenderer;
    private final ValueRenderingContext valueRenderingContext;
    private final ColumnWidthCumulator cumulator;
    private final Object source;

    public VisualColumnImpl(Object label, Type type) {
        this(label, label, type, null, null, null, null, null);
    }

    public VisualColumnImpl(Object headerValue, Object label, Type type, String role, VisualStyle style, ValueRenderer valueRenderer, ColumnWidthCumulator cumulator, Object source) {
        this.headerValue = headerValue;
        this.label = Label.from(label);
        this.type = type;
        this.role = role;
        this.style = style != null ? style : VisualStyle.NO_STYLE;
        this.valueRenderer = valueRenderer;
        valueRenderingContext = this.label == null ? ValueRenderingContext.DEFAULT_READONLY_CONTEXT : new ValueRenderingContext(true, this.label, null, this.style.getTextAlign());
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
    public VisualStyle getStyle() {
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
