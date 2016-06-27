package mongoose.activities.monitor;

import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.controls.Button;
import naga.core.spi.toolkit.controls.Slider;
import naga.core.spi.toolkit.layouts.VBox;
import naga.core.ui.presentation.PresentationActivity;

/**
 * @author Bruno Salmon
 */
public class MonitorActivity extends PresentationActivity<MonitorViewModel, MonitorPresentationModel> {

    public MonitorActivity() {
        super(MonitorPresentationModel::new);
    }

    protected MonitorViewModel buildView(Toolkit toolkit) {
        Slider requestedSlider = toolkit.createSlider();
        Slider startedSlider = toolkit.createSlider();
        VBox vBox = toolkit.createVBox();
        vBox.getChildren().setAll(requestedSlider, startedSlider);
        // Building the UI components
        return new MonitorViewModel(vBox, requestedSlider, startedSlider);
    }

    protected void bindViewModelWithPresentationModel(MonitorViewModel vm, MonitorPresentationModel pm) {
        vm.getStartedSlider().setMin(0);
        vm.getStartedSlider().setMax(3000);
        vm.getRequestSlider().setMin(0);
        vm.getRequestSlider().setMax(3000);
        pm.requestedConnectionsProperty().bind(vm.getRequestSlider().valueProperty());
        vm.getStartedSlider().valueProperty().bind(pm.startedConnectionsProperty());
    }

    protected void bindPresentationModelWithLogic(MonitorPresentationModel pm) {
        pm.startedConnectionsProperty().bind(pm.requestedConnectionsProperty());
    }
}
