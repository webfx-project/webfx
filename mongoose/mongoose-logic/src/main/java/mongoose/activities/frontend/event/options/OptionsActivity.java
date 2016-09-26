package mongoose.activities.frontend.event.options;

import mongoose.activities.frontend.event.booking.BookingProcessActivity;

/**
 * @author Bruno Salmon
 */
public class OptionsActivity extends BookingProcessActivity<OptionsViewModel, OptionsPresentationModel> {

    public OptionsActivity() {
        super(OptionsPresentationModel::new, "person");
        registerViewBuilder(getClass(), new OptionsViewModelBuilder());
    }

}
