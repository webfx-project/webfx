package webfx.framework.activity.impl.elementals.uiroute;

import webfx.framework.activity.impl.elementals.activeproperty.ActivePropertyActivityContextMixin;
import webfx.framework.router.session.Session;
import webfx.framework.ui.uirouter.UiRouter;
import webfx.framework.ui.uirouter.uisession.UiSession;
import webfx.framework.ui.uirouter.uisession.UiSessionMixin;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;
import webfx.platforms.core.services.json.JsonObject;

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
