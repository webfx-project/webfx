package webfx.framework.activity.base.combinations.viewapplication.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import webfx.framework.activity.base.elementals.view.impl.ViewActivityContextBase;
import webfx.framework.activity.base.combinations.viewapplication.ViewApplicationContext;
import webfx.framework.ui.uirouter.UiRouter;
import webfx.fxkits.core.launcher.FxKitLauncher;
import webfx.framework.activity.ActivityContextFactory;
import webfx.framework.activity.base.elementals.application.ApplicationContext;
import webfx.framework.activity.base.elementals.application.impl.ApplicationContextBase;
import webfx.platforms.core.services.windowhistory.WindowHistory;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;

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
            Stage primaryStage = FxKitLauncher.getPrimaryStage();
            Scene scene = primaryStage.getScene();
            if (scene != null)
                scene.setRoot(root);
            else {
                FxKitLauncher.onReady(() -> {
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
    public BrowsingHistory getHistory() {
        if (super.getUiRouter() == null)
            return WindowHistory.getProvider();
        return super.getHistory();
    }

    private Property<Boolean> windowBoundProperty = new SimpleObjectProperty<>(false);
    @Override
    public Property<Boolean> windowBoundProperty() {
        return windowBoundProperty;
    }

}
