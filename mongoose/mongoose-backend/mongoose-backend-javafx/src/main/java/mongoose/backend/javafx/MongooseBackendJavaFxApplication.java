package mongoose.backend.javafx;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import mongoose.activities.tester.TesterViewModel;
import mongoose.application.MongooseBackendApplication;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.javafx.JavaFxToolkit;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendJavaFxApplication {

    public static void main(String[] args) {
        JavaFxToolkit.get().registerNodeFactory(naga.core.spi.toolkit.controls.Slider.class, FxGauge::new);
        //PresentationActivity.registerViewBuilder(TesterActivity.class, MongooseBackendJavaFxApplication::buildView);
        MongooseBackendApplication.main(args);
    }

    protected static TesterViewModel buildView(Toolkit toolkit) {
        VBox vBox = new VBox();
        Slider requestedSlider = new Slider();
        Gauge startedSlider = GaugeBuilder.create().skinType(Gauge.SkinType.HORIZONTAL).build();
        Button startButton = new Button();
        Button exitButton = new Button();
        vBox.getChildren().setAll(requestedSlider, startedSlider, startButton, exitButton);
        return new TesterViewModel(toolkit.wrapNativeNode(vBox), toolkit.wrapNativeNode(requestedSlider), new FxGauge(startedSlider), toolkit.wrapNativeNode(startButton), toolkit.wrapNativeNode(exitButton));
    }

}
