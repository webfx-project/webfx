package webfx.fxkit.javafx.launcher;

import javafx.application.Application;
import javafx.stage.Stage;
import webfx.fxkit.extra.cell.collator.grid.GridCollator;
import webfx.fxkit.extra.controls.displaydata.chart.*;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;
import webfx.fxkit.extra.controls.html.HtmlText;
import webfx.fxkit.extra.controls.html.HtmlTextEditor;
import webfx.fxkit.javafx.mapper.peer.extra.*;
import webfx.fxkit.launcher.spi.impl.FxKitLauncherProviderBase;
import webfx.platform.shared.util.function.Factory;

import java.util.ArrayList;
import java.util.List;

import static webfx.fxkit.javafxgraphics.mapper.spi.NodePeerFactoryRegistry.registerNodePeerFactory;

/**
 * @author Bruno Salmon
 */
public final class JavaFxFxKitLauncherProvider extends FxKitLauncherProviderBase {

    private static List<Runnable> readyRunnables = new ArrayList<>();
    private static Factory<Application> applicationFactory;

    private static Stage primaryStage;

    static {
        registerNodePeerFactory(HtmlText.class, FxHtmlTextPeer::new);
        registerNodePeerFactory(HtmlTextEditor.class, FxHtmlTextEditorPeer::new);
        registerNodePeerFactory(DataGrid.class, FxDataGridPeer::new);
        registerNodePeerFactory(AreaChart.class, FxAreaChartPeer::new);
        registerNodePeerFactory(BarChart.class, FxBarChartPeer::new);
        registerNodePeerFactory(LineChart.class, FxLineChartPeer::new);
        registerNodePeerFactory(PieChart.class, FxPieChartPeer::new);
        registerNodePeerFactory(ScatterChart.class, FxScatterChartPeer::new);
        registerNodePeerFactory(GridCollator.class, GridCollator.GridCollatorPeer::new);
    }

    public JavaFxFxKitLauncherProvider() {
        super("JavaFx", true);
    }

    @Override
    public double getVerticalScrollbarExtraWidth() {
        return 16;
    }

    @Override
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void launchApplication(Factory<Application> applicationFactory, String... args) {
        JavaFxFxKitLauncherProvider.applicationFactory = applicationFactory;
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
        synchronized (JavaFxFxKitLauncherProvider.class) {
            if (readyRunnables != null)
                readyRunnables.add(runnable);
            else
                super.onReady(runnable);
        }
    }

    private static void executeReadyRunnables() {
        synchronized (JavaFxFxKitLauncherProvider.class) {
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

        Application application;

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
            JavaFxFxKitLauncherProvider.primaryStage = primaryStage;
            onJavaFxToolkitReady();
            if (application != null)
                application.start(primaryStage);
        }

    }
}
