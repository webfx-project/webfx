package naga.framework.activity.uiroute;

import naga.framework.activity.activeproperty.ActivePropertyActivityContextMixin;
import naga.framework.activity.i18n.I18nActivityContextMixin;
import naga.framework.ui.router.UiRouter;
import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;

/**
 * @author Bruno Salmon
 */
public interface UiRouteActivityContextMixin
        <C extends UiRouteActivityContext<C>>

        extends ActivePropertyActivityContextMixin<C>,
        I18nActivityContextMixin<C>,
        UiRouteActivityContext<C> {

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

}
