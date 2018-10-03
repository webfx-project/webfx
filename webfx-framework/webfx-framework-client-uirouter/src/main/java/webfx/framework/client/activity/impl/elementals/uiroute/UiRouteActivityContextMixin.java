package webfx.framework.client.activity.impl.elementals.uiroute;

import webfx.framework.client.activity.impl.elementals.activeproperty.ActivePropertyActivityContextMixin;
import webfx.framework.shared.router.session.Session;
import webfx.framework.client.ui.uirouter.UiRouter;
import webfx.framework.client.ui.uirouter.uisession.UiSession;
import webfx.framework.client.ui.uirouter.uisession.UiSessionMixin;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;
import webfx.platform.shared.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public interface UiRouteActivityContextMixin
        <C extends UiRouteActivityContext<C>>

        extends ActivePropertyActivityContextMixin<C>,
        UiRouteActivityContext<C>,
        UiSessionMixin {

    @Override
    default UiRouter getUiRouter() { return getActivityContext().getUiRouter(); }

    @Override
    default BrowsingHistory getHistory() { return getActivityContext().getHistory(); }

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
