package naga.framework.activity.client;

import javafx.beans.property.Property;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.ActivityContext;
import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;
import naga.toolkit.properties.markers.HasNodeProperty;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface UiActivityContext<C extends UiActivityContext<C>> extends ActivityContext<C>, HasNodeProperty {

    UiRouter getUiRouter();

    default History getHistory() {
        return getUiRouter().getHistory();
    }

    JsonObject getParams();

    default <T> T getParameter(String key) { return getParams().get(key); }

    Property<GuiNode> nodeProperty();

    Property<GuiNode> mountNodeProperty();

    static ActivityContext create(ActivityContext parent) {
        return new UiActivityContextImpl(parent, UiActivityContext::create);
    }

}
