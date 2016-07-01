package mongoose.activities.tester;

import mongoose.activities.tester.drive.Drive;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.controls.Button;
import naga.core.spi.toolkit.controls.Slider;
import naga.core.spi.toolkit.controls.Table;
import naga.core.spi.toolkit.controls.TextField;
import naga.core.spi.toolkit.layouts.HBox;
import naga.core.spi.toolkit.layouts.VBox;
import naga.core.ui.presentation.PresentationActivity;

/**
 * @author Bruno Salmon
 */
public class TesterActivity extends PresentationActivity<TesterViewModel, TesterPresentationModel> {

    public TesterActivity() {
        super(TesterPresentationModel::new);
    }

    protected TesterViewModel buildView(Toolkit toolkit) {
        Table systemTable = toolkit.createTable();
        TextField textField = toolkit.createNode(TextField.class);
//        TableColumn<LongProperty, String> usedMemColumn;
        Slider requestedSlider = toolkit.createSlider();
        Slider startedSlider = toolkit.createSlider();
        Button<Integer> startButton = toolkit.createButton();
        Button<Integer> stopButton = toolkit.createButton();
        Button<Integer> exitButton = toolkit.createButton();
        HBox hBox = toolkit.createHBox();
        hBox.getChildren().setAll(startButton, stopButton, exitButton);
        VBox vBox = toolkit.createVBox();
        vBox.getChildren().setAll(requestedSlider, startedSlider);
        // Building the UI components
        return new TesterViewModel(toolkit.createVPage()
                .setHeader(vBox)
                .setCenter(systemTable)
                .setFooter(hBox), systemTable, requestedSlider, startedSlider, startButton, stopButton, exitButton);
    }

    protected void bindViewModelWithPresentationModel(TesterViewModel vm, TesterPresentationModel pm) {
        // Sliders
        vm.getStartedSlider().setMin(0);
        vm.getStartedSlider().setMax(3000);
        vm.getRequestSlider().setMin(0);
        vm.getRequestSlider().setMax(3000);
        pm.requestedConnectionsProperty().bind(vm.getRequestSlider().valueProperty());
        vm.getStartedSlider().valueProperty().bind(pm.startedConnectionsProperty());
        // Buttons
        vm.getStartButton().setText("Start");
        vm.getExitButton().setText("Exit");
    }

    protected void bindPresentationModelWithLogic(TesterPresentationModel pm) {
        Drive.getInstance().start(true);
        Drive.getInstance().requestedConnectionCountProperty().bind(pm.requestedConnectionsProperty());
        pm.startedConnectionsProperty().bind(Drive.getInstance().startedConnectionCountProperty());
    }
}
