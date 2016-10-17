package naga.providers.toolkit.javafx;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import naga.commons.util.function.Consumer;
import naga.commons.util.function.Factory;
import naga.providers.toolkit.javafx.drawing.FxDrawingNode;
import naga.providers.toolkit.javafx.nodes.charts.*;
import naga.providers.toolkit.javafx.nodes.controls.*;
import naga.providers.toolkit.javafx.nodes.gauges.FxGauge;
import naga.providers.toolkit.javafx.nodes.layouts.*;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplayResultSetBuilder;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.properties.conversion.ConvertedProperty;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.charts.*;
import naga.toolkit.spi.nodes.controls.*;
import naga.toolkit.spi.nodes.gauges.Gauge;
import naga.toolkit.spi.nodes.layouts.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class JavaFxToolkit extends Toolkit {

    private static Consumer<Scene> sceneHook;
    private static List<Runnable> readyRunnables = new ArrayList<>();

    public JavaFxToolkit() {
        this(() -> FxApplication.applicationWindow = new FxWindow(FxApplication.primaryStage));
    }

    protected JavaFxToolkit(Factory<Window> windowFactory) {
        super(FxScheduler.SINGLETON, windowFactory);
        new Thread(() -> Application.launch(FxApplication.class), "JavaFxToolkit-Launcher").start();
        registerNodeFactoryAndWrapper(Table.class, FxTable::new, TableView.class, FxTable::new);
        registerNodeFactoryAndWrapper(CheckBox.class, FxCheckBox::new, javafx.scene.control.CheckBox.class, FxCheckBox::new);
        registerNodeFactoryAndWrapper(RadioButton.class, FxRadioButton::new, javafx.scene.control.RadioButton.class, FxRadioButton::new);
        registerNodeFactoryAndWrapper(ToggleSwitch.class, FxToggleSwitch::new, naga.providers.toolkit.javafx.nodes.controlsfx.ToggleSwitch.class, FxToggleSwitch::new);
        registerNodeFactoryAndWrapper(Image.class, FxImage::new, javafx.scene.image.ImageView.class, FxImage::new);
        registerNodeFactoryAndWrapper(TextView.class, FxTextView::new, javafx.scene.text.Text.class, FxTextView::new);
        registerNodeFactoryAndWrapper(TextField.class, FxTextField::new, javafx.scene.control.TextField.class, FxTextField::new);
        registerNodeFactoryAndWrapper(HtmlView.class, FxHtmlView::new, WebView.class, FxHtmlView::new);
        registerNodeFactoryAndWrapper(Button.class, FxButton::new, javafx.scene.control.Button.class, FxButton::new);
        registerNodeFactoryAndWrapper(Slider.class, FxSlider::new, javafx.scene.control.Slider.class, FxSlider::new);
        registerNodeFactoryAndWrapper(Gauge.class, FxGauge::new, eu.hansolo.medusa.Gauge.class, FxGauge::new);
        registerNodeFactory(SearchBox.class, FxSearchBox::new);
        registerNodeFactory(VPage.class, FxVPage::new);
        registerNodeFactoryAndWrapper(VBox.class, FxVBox::new, javafx.scene.layout.VBox.class, FxVBox::new);
        registerNodeFactoryAndWrapper(HBox.class, FxHBox::new, javafx.scene.layout.HBox.class, FxHBox::new);
        registerNodeFactoryAndWrapper(FlowPane.class, FxFlowPane::new, javafx.scene.layout.FlowPane.class, FxFlowPane::new);
        registerNodeFactoryAndWrapper(LineChart.class, FxLineChart::new, javafx.scene.chart.LineChart.class, FxLineChart::new);
        registerNodeFactoryAndWrapper(AreaChart.class, FxAreaChart::new, javafx.scene.chart.AreaChart.class, FxAreaChart::new);
        registerNodeFactoryAndWrapper(BarChart.class, FxBarChart::new, javafx.scene.chart.BarChart.class, FxBarChart::new);
        registerNodeFactoryAndWrapper(ScatterChart.class, FxScatterChart::new, javafx.scene.chart.ScatterChart.class, FxScatterChart::new);
        registerNodeFactoryAndWrapper(PieChart.class, FxPieChart::new, javafx.scene.chart.PieChart.class, FxPieChart::new);
        registerNodeFactory(DrawingNode.class, FxDrawingNode::new);
    }

    @Override
    public boolean isReady() {
        return readyRunnables == null;
    }

    @Override
    public void onReady(Runnable runnable) {
        synchronized (readyRunnables) {
            if (readyRunnables != null)
                readyRunnables.add(runnable);
            else
                super.onReady(runnable);
        }
    }

    private static void executeReadyRunnables() {
        synchronized (readyRunnables) {
            if (readyRunnables != null) {
                readyRunnables.forEach(Runnable::run);
                readyRunnables = null;
            }
        }
    }

    public static void setSceneHook(Consumer<Scene> sceneHook) {
        JavaFxToolkit.sceneHook = sceneHook;
    }

    public static Consumer<Scene> getSceneHook() {
        return sceneHook;
    }

    public static DisplayResultSet transformDisplayResultSetValuesToProperties(DisplayResultSet rs) {
        return DisplayResultSetBuilder.convertDisplayResultSet(rs, SimpleObjectProperty::new);
    }

    public static class FxApplication extends Application {
        public static Stage primaryStage;
        public static FxWindow applicationWindow;

        @Override
        public void start(Stage primaryStage) throws Exception {
            // Activating SVG support
            SvgImageLoaderFactory.install();
            FxApplication.primaryStage = primaryStage;
            primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
            if (applicationWindow != null)
                applicationWindow.setStage(primaryStage);
            executeReadyRunnables();
        }
    }

    public static ConvertedProperty<Integer, Number> numberToIntegerProperty(Property<Number> numberProperty) {
        return new ConvertedProperty<>(numberProperty, Integer::doubleValue, Number::intValue);
    }
}
