package naga.toolkit.display.impl;

import naga.commons.type.Type;
import naga.toolkit.cell.renderers.ValueRenderer;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayStyle;
import naga.toolkit.display.Label;

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
    private naga.toolkit.fx.ext.cell.renderer.ValueRenderer fxValueRenderer;

    public DisplayColumnImpl(Object label, Type type) {
        this(label, label, type, null, null, null, null);
    }

    public DisplayColumnImpl(Object headerValue, Object label, Type type, String role, DisplayStyle style, ValueRenderer valueRenderer, naga.toolkit.fx.ext.cell.renderer.ValueRenderer fxValueRenderer) {
        this.headerValue = headerValue;
        this.label = Label.from(label);
        this.type = type;
        this.role = role;
        this.style = style != null ? style : DisplayStyle.NO_STYLE;
        this.valueRenderer = valueRenderer;
        this.fxValueRenderer = fxValueRenderer;
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
    public naga.toolkit.fx.ext.cell.renderer.ValueRenderer getFxValueRenderer() {
        if (fxValueRenderer == null)
            fxValueRenderer = naga.toolkit.fx.ext.cell.renderer.ValueRenderer.create(getType());
        return fxValueRenderer;
    }
}
