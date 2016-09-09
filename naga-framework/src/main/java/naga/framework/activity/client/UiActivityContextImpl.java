package naga.framework.activity.client;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;
import naga.platform.activity.ActivityContextImpl;
import naga.platform.activity.HasActivityContext;
import naga.platform.json.Json;
import naga.platform.json.spi.JsonObject;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class UiActivityContextImpl<C extends UiActivityContextImpl<C>> extends ActivityContextImpl<C> implements UiActivityContext<C> {

    private UiRouter uiRouter;
    private JsonObject params;

    protected UiActivityContextImpl(ActivityContext parentContext, ActivityContextFactory<C> contextFactory) {
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
        if (parentContext instanceof UiActivityContext)
            return ((UiActivityContext) parentContext).getUiRouter();
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
            if (parentContext instanceof UiActivityContext)
                return ((UiActivityContext<?>) parentContext).getParameter(key);
        }
        return null;
    }

    private final Property<GuiNode> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> nodeProperty() {
        return nodeProperty;
    }

    private final Property<GuiNode> mountNodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> mountNodeProperty() {
        return mountNodeProperty;
    }

    public static <C extends ActivityContext> UiActivityContextImpl from(C activityContext) {
        if (activityContext instanceof UiActivityContextImpl)
            return (UiActivityContextImpl) activityContext;
        if (activityContext instanceof HasActivityContext) // including ActivityContextDirectAccess
            return from(((HasActivityContext) activityContext).getActivityContext());
        return null;
    }

}
