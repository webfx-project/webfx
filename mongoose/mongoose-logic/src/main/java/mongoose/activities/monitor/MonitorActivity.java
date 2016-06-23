package mongoose.activities.monitor;

import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.nodes.Button;
import naga.core.ui.presentation.PresentationActivity;

/**
 * @author Bruno Salmon
 */
public class MonitorActivity extends PresentationActivity<MonitorViewModel, MonitorPresentationModel> {

    public MonitorActivity() {
        super(MonitorPresentationModel::new);
    }

    protected MonitorViewModel buildView(Toolkit toolkit) {
        // Building the UI components
        return new MonitorViewModel(toolkit.createButton());
    }

    protected void bindViewModelWithPresentationModel(MonitorViewModel vm, MonitorPresentationModel pm) {
        // Hard coded initialization
        ((Button) vm.getContentNode()).setText("Chart is coming soon...");
    }

    protected void bindPresentationModelWithLogic(MonitorPresentationModel pm) {
    }
}
