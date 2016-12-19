package naga.toolkit.spi;

import naga.commons.scheduler.UiScheduler;
import naga.commons.util.function.Factory;
import naga.commons.util.serviceloader.ServiceLoaderHelper;
import naga.toolkit.fx.scene.Scene;
import naga.toolkit.fx.stage.Screen;
import naga.toolkit.fx.stage.Window;

/**
 * @author Bruno Salmon
 */
public abstract class Toolkit {

    private final UiScheduler uiScheduler;
    private final Factory<Window> windowFactory;
    private final Factory<Scene> sceneFactory;
    private Window primaryWindow;

    public Toolkit(UiScheduler uiScheduler, Factory<Window> windowFactory, Factory<Scene> sceneFactory) {
        this.uiScheduler = uiScheduler;
        this.windowFactory = windowFactory;
        this.sceneFactory = sceneFactory;
    }

    public Scene createScene() {
        return sceneFactory.create();
    }

    public Scene createScene(double width, double height) {
        Scene scene = createScene();
        scene.setWidth(width);
        scene.setHeight(height);
        return scene;
    }

    public abstract Screen getPrimaryScreen();

    public Window getPrimaryWindow() {
        if (primaryWindow == null)
            primaryWindow = windowFactory.create();
        return primaryWindow;
    }

    public boolean isReady() {
        return true;
    }

    public void onReady(Runnable runnable) {
        get().scheduler().runInUiThread(runnable);
    }

    public UiScheduler scheduler() {
        return uiScheduler;
    }

    public static boolean isUiThread() {
        return get().scheduler().isUiThread();
    }

    private static Toolkit TOOLKIT;

    public static synchronized Toolkit get() {
        if (TOOLKIT == null) {
            //Platform.log("Getting toolkit");
            TOOLKIT = ServiceLoaderHelper.loadService(Toolkit.class);
            //Platform.log("Toolkit ok");
        }
        return TOOLKIT;
    }

}
