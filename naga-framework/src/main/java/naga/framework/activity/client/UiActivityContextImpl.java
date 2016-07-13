package naga.framework.activity.client;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;
import naga.platform.activity.ActivityContextImpl;
import naga.platform.activity.HasActivityContext;
import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class UiActivityContextImpl<C extends UiActivityContextImpl<C>> extends ActivityContextImpl<C> implements UiActivityContext<C> {

    private History history;
    private JsonObject params;

    protected UiActivityContextImpl(ActivityContext parentContext, ActivityContextFactory<C> contextFactory) {
        super(parentContext, contextFactory);
    }

    public void setHistory(History history) {
        this.history = history;
    }

    @Override
    public History getHistory() {
        if (history != null)
            return history;
        ActivityContext parentContext = getParentContext();
        if (parentContext instanceof UiActivityContext)
            return ((UiActivityContext) parentContext).getHistory();
        return null;
    }

    public void setParams(JsonObject params) {
        this.params = params;
    }

    @Override
    public JsonObject getParams() {
        return params;
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
