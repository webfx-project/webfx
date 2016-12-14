package naga.framework.activity.client;

import naga.framework.ui.i18n.I18n;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.ActivityContext;
import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;
import naga.toolkit.properties.markers.HasNodeProperty;

/**
 * @author Bruno Salmon
 */
public interface UiActivityContext<C extends UiActivityContext<C>> extends ActivityContext<C>,
        HasNodeProperty,
        HasMountNodeProperty {

    UiRouter getUiRouter();

    default History getHistory() {
        return getUiRouter().getHistory();
    }

    JsonObject getParams();

    default <T> T getParameter(String key) { return getParams().get(key); }

    I18n getI18n();

    static ActivityContext create(ActivityContext parent) {
        return new UiActivityContextImpl(parent, UiActivityContext::create);
    }

}
