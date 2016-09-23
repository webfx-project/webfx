package mongoose.activities.frontend.container;

import mongoose.activities.shared.container.ContainerActivity;
import naga.commons.util.function.Factory;

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
