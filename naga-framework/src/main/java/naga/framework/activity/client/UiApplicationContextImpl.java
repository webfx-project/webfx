package naga.framework.activity.client;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.ActivityContextFactory;
import naga.platform.activity.client.ApplicationContextImpl;
import naga.platform.client.url.history.History;
import naga.platform.json.Json;
import naga.platform.json.spi.JsonObject;
import naga.platform.spi.Platform;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class UiApplicationContextImpl<C extends UiApplicationContextImpl<C>> extends ApplicationContextImpl<C> implements UiApplicationContext<C> {

    protected UiApplicationContextImpl(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
        nodeProperty().addListener(new ChangeListener<GuiNode>() {
            @Override
            public void changed(ObservableValue<? extends GuiNode> observable, GuiNode oldValue, GuiNode newValue) {
                observable.removeListener(this);
                //Platform.log("Binding application window node property");
                Toolkit.get().getApplicationWindow().nodeProperty().bind(observable);
                windowBoundProperty.setValue(true);
            }
        });
    }

    private Property<Boolean> windowBoundProperty = new SimpleObjectProperty<>(false);
    @Override
    public Property<Boolean> windowBoundProperty() {
        return windowBoundProperty;
    }

    private UiRouter uiRouter;
    @Override
    public UiRouter getUiRouter() {
        if (uiRouter == null)
            uiRouter = UiRouter.create(this);
        return uiRouter;
    }

    public void setUiRouter(UiRouter uiRouter) {
        this.uiRouter = uiRouter;
    }

    @Override
    public History getHistory() {
        return uiRouter != null ? uiRouter.getHistory() : Platform.get().getBrowserHistory();
    }

    private JsonObject params = Json.createObject();
    @Override
    public JsonObject getParams() {
        return params;
    }

    private final Property<GuiNode> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> nodeProperty() {
        return nodeProperty;
    }

    @Override
    public Property<GuiNode> mountNodeProperty() {
        return null;
    }

    private I18n i18n;
    @Override
    public C setI18n(I18n i18n) {
        this.i18n = i18n;
        return (C) this;
    }

    @Override
    public I18n getI18n() {
        return i18n;
    }
}
