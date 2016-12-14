package naga.framework.activity.client;

import javafx.beans.property.Property;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.ActivityContextMixin;
import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;
import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface UiActivityContextMixin<C extends UiActivityContext<C>> extends ActivityContextMixin<C>, UiActivityContext<C> {

    @Override
    default UiRouter getUiRouter() { return getActivityContext().getUiRouter(); }

    @Override
    default History getHistory() { return getActivityContext().getHistory(); }

    @Override
    default JsonObject getParams() { return getActivityContext().getParams(); }

    @Override
    default <T> T getParameter(String key) {
        return getActivityContext().getParameter(key);
    }

    @Override
    default Property<Node> nodeProperty() { return getActivityContext().nodeProperty(); }

    @Override
    default Property<Node> mountNodeProperty() { return getActivityContext().mountNodeProperty(); }

    @Override
    default I18n getI18n() { return getActivityContext().getI18n(); }
}
