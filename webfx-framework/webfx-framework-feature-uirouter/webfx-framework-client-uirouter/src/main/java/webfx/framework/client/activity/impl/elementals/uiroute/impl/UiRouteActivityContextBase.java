package webfx.framework.client.activity.impl.elementals.uiroute.impl;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleBooleanProperty;
import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.shared.router.session.Session;
import webfx.framework.client.ui.uirouter.UiRouter;
import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.ActivityContextFactory;
import webfx.framework.client.activity.impl.ActivityContextBase;
import webfx.platform.client.services.windowhistory.WindowHistory;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public class UiRouteActivityContextBase
        <THIS extends UiRouteActivityContextBase<THIS>>

        extends ActivityContextBase<THIS>
        implements UiRouteActivityContext<THIS> {

    private UiRouter uiRouter;
    private JsonObject params;
    private Session session;
    private String routingPath;

    protected UiRouteActivityContextBase(ActivityContext parentContext, ActivityContextFactory<THIS> contextFactory) {
        super(parentContext, contextFactory);
    }

    public void setUiRouter(UiRouter uiRouter) {
        this.uiRouter = uiRouter;
    }

    @Override
    public UiRouter getUiRouter() {
        UiRouter thisOrParentUiRouter = getThisOrParentUiRouter();
        if (thisOrParentUiRouter == null)
            setUiRouter(thisOrParentUiRouter = UiRouter.create(this));
        return thisOrParentUiRouter;
    }

    private UiRouter getThisOrParentUiRouter() {
        if (uiRouter != null)
            return uiRouter;
        ActivityContext parentContext = getParentContext();
        if (parentContext instanceof UiRouteActivityContext)
            return ((UiRouteActivityContext) parentContext).getUiRouter();
        return null;
    }

    @Override
    public BrowsingHistory getHistory() {
        UiRouter thisOrParentUiRouter = getThisOrParentUiRouter();
        if (thisOrParentUiRouter == null)
            return WindowHistory.getProvider();
        return thisOrParentUiRouter.getHistory();
    }

    public void setParams(JsonObject params) {
        this.params = params;
    }

    @Override
    public JsonObject getParams() {
        if (params == null)
            params = Json.createObject();
        return params;
    }

    @Override
    public <T> T getParameter(String key) {
        T value = getParams().get(key);
        if (value == null && !params.has(key)) {
            ActivityContext parentContext = getParentContext();
            if (parentContext instanceof UiRouteActivityContext)
                return ((UiRouteActivityContext<?>) parentContext).getParameter(key);
        }
        return value;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public String getRoutingPath() {
        String routingPath = this.routingPath;
        if (routingPath == null) {
            ActivityContext parentContext = getParentContext();
            if (parentContext instanceof UiRouteActivityContext)
                return ((UiRouteActivityContext<?>) parentContext).getRoutingPath();
        }
        return routingPath;
    }

    public void setRoutingPath(String routingPath) {
        this.routingPath = routingPath;
    }

    public static <IC extends ActivityContext<IC>, OC extends UiRouteActivityContextBase<OC>> OC toUiRouterActivityContextBase(IC activityContext) {
        return from(activityContext, ac -> ac instanceof UiRouteActivityContextBase);
    }

    private final BooleanProperty activeProperty = new SimpleBooleanProperty(false);
    @Override
    public ReadOnlyProperty<Boolean> activeProperty() {
        return activeProperty;
    }

}
