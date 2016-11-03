package mongoose.activities.frontend.event.options;

import mongoose.activities.frontend.event.shared.BookingsProcessViewModelBuilder;

/**
 * @author Bruno Salmon
 */
public class OptionsViewModelBuilder extends BookingsProcessViewModelBuilder<OptionsViewModel> {

    @Override
    protected OptionsViewModel createViewModel() {
        return new OptionsViewModel(contentNode, previousButton, nextButton);
    }

}
