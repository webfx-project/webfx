package naga.core.ui.displayresultset;

import naga.core.orm.domainmodel.Label;
import naga.core.type.Type;

/**
 * @author Bruno Salmon
 */
public class DisplayColumn {

    private final Label label;
    private final Type type;

    public DisplayColumn(Object label, Type type) {
        this.label = Label.from(label);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Object getHeaderValue() {
        return label.getText();
    }

    public Type getHeaderType() {
        return null;
    }
}
