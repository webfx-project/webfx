package naga.framework.activity.uiroute;

import naga.framework.activity.activeproperty.ActivePropertyActivityContextMixin;
import naga.framework.activity.i18n.I18nActivityContextMixin;
import naga.framework.session.Session;
import naga.framework.ui.session.UiSession;
import naga.framework.ui.session.UiSessionMixin;
import naga.framework.ui.router.UiRouter;
import naga.platform.client.url.history.History;
import naga.platform.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public interface UiRouteActivityContextMixin
        <C extends UiRouteActivityContext<C>>

        extends ActivePropertyActivityContextMixin<C>,
        I18nActivityContextMixin<C>,
        UiRouteActivityContext<C>,
        UiSessionMixin {

    @Override
    default UiRouter getUiRouter() { return getActivityContext().getUiRouter(); }

    @Override
    default History getHistory() { return getActivityContext().getHistory(); }

    @Override
    default JsonObject getParams() { return getActivityContext().getParams(); }

    @Override
    default Session getSession() {
        return getActivityContext().getSession();
    }

    @Override
    default <T> T getParameter(String key) {
        return getActivityContext().getParameter(key);
    }

    @Override
    default String getRoutingPath() {
        return getActivityContext().getRoutingPath();
    }

    @Override
    default UiSession getUiSession() {
        return getActivityContext().getUiSession();
    }
}
