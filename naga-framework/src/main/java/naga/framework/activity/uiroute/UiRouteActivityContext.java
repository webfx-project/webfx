package naga.framework.activity.uiroute;

import naga.framework.activity.activeproperty.ActivePropertyActivityContext;
import naga.framework.activity.i18n.I18nActivityContext;
import naga.framework.ui.router.UiRouter;
import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;

/**
 * @author Bruno Salmon
 */
public interface UiRouteActivityContext
        <THIS extends UiRouteActivityContext<THIS>>

        extends ActivePropertyActivityContext<THIS>,
        I18nActivityContext<THIS> {

    UiRouter getUiRouter();

    default History getHistory() {
        return getUiRouter().getHistory();
    }

    JsonObject getParams();

    default <T> T getParameter(String key) { return getParams().get(key); }

}
