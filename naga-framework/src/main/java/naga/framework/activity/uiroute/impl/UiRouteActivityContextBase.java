package naga.framework.activity.uiroute.impl;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleBooleanProperty;
import naga.framework.activity.uiroute.UiRouteActivityContext;
import naga.framework.session.Session;
import naga.framework.ui.uirouter.UiRouter;
import naga.framework.activity.ActivityContext;
import naga.framework.activity.ActivityContextFactory;
import naga.framework.activity.ActivityContextBase;
import naga.platform.services.json.Json;
import naga.platform.services.json.JsonObject;

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
        if (uiRouter != null)
            return uiRouter;
        ActivityContext parentContext = getParentContext();
        if (parentContext instanceof UiRouteActivityContext)
            return ((UiRouteActivityContext) parentContext).getUiRouter();
        return null;
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
