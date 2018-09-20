package webfx.framework.activity.impl.elementals.uiroute;

import webfx.framework.activity.impl.elementals.activeproperty.ActivePropertyActivityContext;
import webfx.framework.router.session.Session;
import webfx.framework.ui.uirouter.UiRouter;
import webfx.framework.ui.uirouter.uisession.UiSession;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;
import webfx.platforms.core.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public interface UiRouteActivityContext
        <THIS extends UiRouteActivityContext<THIS>>

        extends ActivePropertyActivityContext<THIS> {

    UiRouter getUiRouter();

    default BrowsingHistory getHistory() {
        return getUiRouter().getHistory();
    }

    JsonObject getParams();

    default <T> T getParameter(String key) { return getParams().get(key); }

    Session getSession();

    String getRoutingPath();

    default UiSession getUiSession() {
        return getUiRouter().getUiSession();
    }
}
