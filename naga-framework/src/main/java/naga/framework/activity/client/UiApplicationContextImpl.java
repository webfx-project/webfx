package naga.framework.activity.client;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.router.UiRouter;
import javafx.geometry.Rectangle2D;
import naga.fx.scene.Node;
import naga.fx.scene.Parent;
import naga.fx.scene.Scene;
import naga.fx.spi.Toolkit;
import naga.fx.stage.Screen;
import naga.fx.stage.Stage;
import naga.platform.activity.ActivityContextFactory;
import naga.platform.activity.client.ApplicationContextImpl;
import naga.platform.client.url.history.History;
import naga.platform.json.Json;
import naga.platform.json.spi.JsonObject;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public class UiApplicationContextImpl<C extends UiApplicationContextImpl<C>> extends ApplicationContextImpl<C> implements UiApplicationContext<C> {

    UiApplicationContextImpl(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
        nodeProperty().addListener((observable, oldValue, node) -> {
            Parent root = (Parent) node;
            Stage primaryStage = Toolkit.get().getPrimaryStage();
            Scene scene = primaryStage.getScene();
            if (scene != null)
                scene.setRoot(root);
            else {
                Toolkit.get().onReady(() -> {
                    Rectangle2D screenVisualBounds = Screen.getPrimary().getVisualBounds();
                    double width = screenVisualBounds.getWidth() * 0.8;
                    double height = screenVisualBounds.getHeight() * 0.9;
                    primaryStage.setScene(new Scene(root, width, height));
                    primaryStage.show();
                });
            }
            windowBoundProperty.setValue(true);
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

    private final Property<Node> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Node> nodeProperty() {
        return nodeProperty;
    }

    @Override
    public Property<Node> mountNodeProperty() {
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
