package mongoose.activities.frontend.event.program;

import mongoose.activities.frontend.event.shared.BookingProcessActivity;
import mongoose.activities.frontend.event.shared.FeesGroup;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import naga.platform.spi.Platform;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.util.Properties;

/**
 * @author Bruno Salmon
 */
public class ProgramActivity extends BookingProcessActivity<ProgramViewModel, ProgramPresentationModel> {

    public ProgramActivity() {
        super(ProgramPresentationModel::new, null);
        registerViewBuilder(getClass(), new ProgramViewModelBuilder());
    }

    private ProgramViewModel programViewModel;
    private CalendarGraphic programCalendarGraphic;

    @Override
    protected void bindViewModelWithPresentationModel(ProgramViewModel vm, ProgramPresentationModel pm) {
        super.bindViewModelWithPresentationModel(vm, pm);
        this.programViewModel = vm;
        showCalendarIfBothLogicAndViewAreReady();
    }

    private void showCalendarIfBothLogicAndViewAreReady() {
        if (programCalendarGraphic != null && programViewModel != null)
            Toolkit.get().scheduler().runInUiThread(() -> programViewModel.getCalendarPanel().setCenter(programCalendarGraphic.getNode()));
    }

    @Override
    protected void bindPresentationModelWithLogic(ProgramPresentationModel pm) {
        // Doing now but also on event change
        Properties.runNowAndOnPropertiesChange(property -> bindPresentationModelWithLogicNow(), pm.eventIdProperty());
    }

    private void bindPresentationModelWithLogicNow() {
        onFeesGroup().setHandler(async -> {
            if (async.failed())
                Platform.log(async.cause());
            else {
                programCalendarGraphic = createOrUpdateCalendarGraphicFromOptionsPreselection(findNoAccommodationOptionsPreselection(async.result()), programCalendarGraphic);
                showCalendarIfBothLogicAndViewAreReady();
            }
        });
    }

    private static OptionsPreselection findNoAccommodationOptionsPreselection(FeesGroup[] feesGroups) {
        for (FeesGroup fg : feesGroups) {
            for (OptionsPreselection op : fg.getOptionsPreselections())
                if (!op.hasAccommodation())
                    return op;
        }
        return null;
    }
}
