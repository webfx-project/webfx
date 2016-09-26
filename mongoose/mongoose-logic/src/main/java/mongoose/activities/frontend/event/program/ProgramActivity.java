package mongoose.activities.frontend.event.program;

import mongoose.activities.frontend.event.booking.BookingProcessActivity;

/**
 * @author Bruno Salmon
 */
public class ProgramActivity extends BookingProcessActivity<ProgramViewModel, ProgramPresentationModel> {

    public ProgramActivity() {
        super(ProgramPresentationModel::new, null);
        registerViewBuilder(getClass(), new ProgramViewModelBuilder());
    }

}
