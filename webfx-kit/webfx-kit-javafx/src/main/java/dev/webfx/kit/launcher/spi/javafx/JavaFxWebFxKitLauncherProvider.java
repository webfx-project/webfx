package dev.webfx.kit.launcher.spi.javafx;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;
import dev.webfx.kit.launcher.spi.base.WebFxKitLauncherProviderBase;
import dev.webfx.platform.shared.util.function.Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class JavaFxWebFxKitLauncherProvider extends WebFxKitLauncherProviderBase {

    private static List<Runnable> readyRunnables = new ArrayList<>();
    private static Factory<Application> applicationFactory;

    private static Stage primaryStage;
    private static Application application;

    public JavaFxWebFxKitLauncherProvider() {
        super("JavaFx", true);
    }

    @Override
    public double getVerticalScrollbarExtraWidth() {
        return 16;
    }

    @Override
    public HostServices getHostServices() {
        return getApplication().getHostServices();
    }

    @Override
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public void launchApplication(Factory<Application> applicationFactory, String... args) {
        JavaFxWebFxKitLauncherProvider.applicationFactory = applicationFactory;
        new Thread(() -> {
            Application.launch(FxKitWrapperApplication.class, args);
            System.exit(0);
        }).start();
    }

    private static void onJavaFxToolkitReady() {
        // Activating SVG support
        // de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory.install();
        executeReadyRunnables();
    }

    @Override
    public boolean isReady() {
        return readyRunnables == null;
    }

    @Override
    public void onReady(Runnable runnable) {
        synchronized (JavaFxWebFxKitLauncherProvider.class) {
            if (readyRunnables != null)
                readyRunnables.add(runnable);
            else
                super.onReady(runnable);
        }
    }

    private static void executeReadyRunnables() {
        synchronized (JavaFxWebFxKitLauncherProvider.class) {
            if (readyRunnables != null) {
                List<Runnable> runnables = readyRunnables;
                readyRunnables = null;
                //runnables.forEach(Runnable::run); doesn't work on Android
                for (Runnable runnable : runnables)
                    runnable.run();
            }
        }
    }

    public static class FxKitWrapperApplication extends Application {

        @Override
        public void init() throws Exception {
            if (applicationFactory != null)
                application = applicationFactory.create();
            if (application != null) {
                //ParametersImpl.registerParameters(application, getParameters());
                application.init();
            }
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            JavaFxWebFxKitLauncherProvider.primaryStage = primaryStage;
            onJavaFxToolkitReady();
            if (application != null)
                application.start(primaryStage);
        }

    }
}
