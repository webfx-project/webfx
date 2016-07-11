package naga.framework.ui.activity;

import javafx.beans.property.Property;
import naga.platform.activity.ActivityContextDirectAccess;
import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface UiActivityContextDirectAccess<C extends UiActivityContext<C>> extends ActivityContextDirectAccess<C>, UiActivityContext<C> {

    @Override
    default History getHistory() { return getActivityContext().getHistory(); }

    @Override
    default JsonObject getParams() { return getActivityContext().getParams(); }

    @Override
    default Property<GuiNode> nodeProperty() { return getActivityContext().nodeProperty(); }

    @Override
    default Property<GuiNode> mountNodeProperty() { return getActivityContext().mountNodeProperty(); }

}
