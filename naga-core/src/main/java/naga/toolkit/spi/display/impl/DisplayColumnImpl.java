package naga.toolkit.spi.display.impl;

import naga.framework.orm.domainmodel.Label;
import naga.commons.type.Type;
import naga.toolkit.spi.display.DisplayColumn;

/**
 * @author Bruno Salmon
 */
public class DisplayColumnImpl implements DisplayColumn {

    private final Object headerValue;
    private final Label label;
    private final Type type;
    private final String role;

    public DisplayColumnImpl(Object label, Type type) {
        this(label, label, type, null);
    }

    public DisplayColumnImpl(Object headerValue, Object label, Type type, String role) {
        this.headerValue = headerValue;
        this.label = Label.from(label);
        this.type = type;
        this.role = role;
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
}
