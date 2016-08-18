package naga.toolkit.display.impl;

import naga.commons.type.Type;
import naga.toolkit.cell.renderers.CellRenderer;
import naga.toolkit.cell.renderers.CellRendererFactory;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.Label;

/**
 * @author Bruno Salmon
 */
public class DisplayColumnImpl implements DisplayColumn {

    private final Object headerValue;
    private final Label label;
    private final Type type;
    private final String role;
    private final Double prefWidth;
    private final String textAlign;
    private CellRenderer cellRenderer;

    public DisplayColumnImpl(Object label, Type type) {
        this(label, label, type, null, null, null);
    }

    public DisplayColumnImpl(Object headerValue, Object label, Type type, String role, Double prefWidth, String textAlign) {
        this.headerValue = headerValue;
        this.label = Label.from(label);
        this.type = type;
        this.role = role;
        this.prefWidth = prefWidth;
        this.textAlign = textAlign;
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
    public Double getPrefWidth() {
        return prefWidth;
    }

    @Override
    public String getTextAlign() {
        return textAlign;
    }

    @Override
    public CellRenderer getCellRenderer() {
        if (cellRenderer == null)
            cellRenderer = CellRendererFactory.getDefault().createCellRenderer(getType());
        return cellRenderer;
    }
}
