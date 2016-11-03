package mongoose.activities.frontend.event.program;

import mongoose.activities.frontend.event.shared.BookingProcessActivity;
import mongoose.activities.frontend.event.shared.FeesGroup;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public class ProgramActivity extends BookingProcessActivity<ProgramViewModel, ProgramPresentationModel> {

    public ProgramActivity() {
        super(ProgramPresentationModel::new, null);
        registerViewBuilder(getClass(), new ProgramViewModelBuilder());
    }

    @Override
    protected void bindPresentationModelWithLogic(ProgramPresentationModel programPresentationModel) {
        onFeesGroup().setHandler(async -> {
            if (async.failed())
                Platform.log(async.cause());
            else {
                FeesGroup[] feesGroups = async.result();
            }
        });
    }
}
