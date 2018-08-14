package naga.framework.activity.uiroute;

import naga.framework.activity.activeproperty.ActivePropertyActivityContext;
import naga.framework.session.Session;
import naga.framework.ui.uirouter.UiRouter;
import naga.framework.ui.session.UiSession;
import naga.platform.client.url.history.History;
import naga.platform.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public interface UiRouteActivityContext
        <THIS extends UiRouteActivityContext<THIS>>

        extends ActivePropertyActivityContext<THIS> {

    UiRouter getUiRouter();

    default History getHistory() {
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
