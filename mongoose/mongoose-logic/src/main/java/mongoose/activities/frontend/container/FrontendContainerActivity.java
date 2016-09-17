package mongoose.activities.frontend.container;

import mongoose.activities.shared.container.ContainerActivity;
import naga.commons.util.function.Factory;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class FrontendContainerActivity extends ContainerActivity<FrontendContainerViewModel, FrontendContainerPresentationModel> {

    public FrontendContainerActivity() {
        this(() -> new FrontendContainerPresentationModel());
    }

    public FrontendContainerActivity(Factory<FrontendContainerPresentationModel> presentationModelFactory) {
        super(presentationModelFactory);
    }

}
