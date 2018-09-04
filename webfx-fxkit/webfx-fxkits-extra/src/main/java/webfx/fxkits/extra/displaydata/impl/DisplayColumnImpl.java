package webfx.fxkits.extra.displaydata.impl;

import webfx.fxkits.extra.displaydata.ColumnWidthCumulator;
import webfx.fxkits.extra.cell.renderer.ValueRenderer;
import webfx.fxkits.extra.displaydata.DisplayColumn;
import webfx.fxkits.extra.displaydata.DisplayStyle;
import webfx.fxkits.extra.displaydata.Label;
import webfx.fxkits.extra.type.Type;

/**
 * @author Bruno Salmon
 */
public class DisplayColumnImpl implements DisplayColumn {

    private final Object headerValue;
    private final Label label;
    private final Type type;
    private final String role;
    private final DisplayStyle style;
    private ValueRenderer valueRenderer;
    private ColumnWidthCumulator cumulator;

    public DisplayColumnImpl(Object label, Type type) {
        this(label, label, type, null, null, null, null);
    }

    public DisplayColumnImpl(Object headerValue, Object label, Type type, String role, DisplayStyle style, ValueRenderer valueRenderer, ColumnWidthCumulator cumulator) {
        this.headerValue = headerValue;
        this.label = Label.from(label);
        this.type = type;
        this.role = role;
        this.style = style != null ? style : DisplayStyle.NO_STYLE;
        this.valueRenderer = valueRenderer;
        this.cumulator = cumulator;
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
    public ColumnWidthCumulator getCumulator() {
        return cumulator;
    }
}
