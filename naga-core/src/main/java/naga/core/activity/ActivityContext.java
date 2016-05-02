package naga.core.activity;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.hasproperties.HasNodeProperty;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class ActivityContext implements HasNodeProperty {

    private Map<String, String> params;

    private Property<GuiNode> nodeProperty;
    @Override
    public Property<GuiNode> nodeProperty() {
        if (nodeProperty == null)
            nodeProperty = new SimpleObjectProperty<>();
        return nodeProperty;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
