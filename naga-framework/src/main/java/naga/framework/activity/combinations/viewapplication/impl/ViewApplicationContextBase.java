package naga.framework.activity.combinations.viewapplication.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import naga.framework.activity.view.impl.ViewActivityContextBase;
import naga.framework.activity.combinations.viewapplication.ViewApplicationContext;
import naga.framework.ui.uirouter.UiRouter;
import naga.fx.spi.Toolkit;
import naga.framework.activity.ActivityContextFactory;
import naga.framework.activity.application.ApplicationContext;
import naga.framework.activity.application.impl.ApplicationContextBase;
import naga.platform.client.url.history.History;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public class ViewApplicationContextBase
        <C extends ViewApplicationContextBase<C>>

        extends ViewActivityContextBase<C>
        implements ViewApplicationContext<C>,
        ApplicationContext<C> {

    public ViewApplicationContextBase(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(null, contextFactory);
        ApplicationContextBase.registerRootFields(this, mainArgs);
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

    @Override
    public UiRouter getUiRouter() {
        UiRouter uiRouter = super.getUiRouter();
        if (uiRouter == null)
            setUiRouter(uiRouter = UiRouter.create(this));
        return uiRouter;
    }

    @Override
    public History getHistory() {
        if (super.getUiRouter() == null)
            return Platform.get().getBrowserHistory();
        return super.getHistory();
    }

    private Property<Boolean> windowBoundProperty = new SimpleObjectProperty<>(false);
    @Override
    public Property<Boolean> windowBoundProperty() {
        return windowBoundProperty;
    }

}
