package mongoose.activities.frontend.event.program;

import mongoose.activities.frontend.event.shared.BookingProcessActivity;
import mongoose.activities.frontend.event.shared.FeesGroup;
import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.CalendarExtractor;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public class ProgramActivity extends BookingProcessActivity<ProgramViewModel, ProgramPresentationModel> {

    public ProgramActivity() {
        super(ProgramPresentationModel::new, null);
        registerViewBuilder(getClass(), new ProgramViewModelBuilder());
    }

    private ProgramViewModel programViewModel;

    @Override
    protected void bindViewModelWithPresentationModel(ProgramViewModel programViewModel, ProgramPresentationModel programPresentationModel) {
        super.bindViewModelWithPresentationModel(programViewModel, programPresentationModel);
        this.programViewModel = programViewModel;
        showCalendarIfBothLogicAndViewAreReady();
    }

    private void showCalendarIfBothLogicAndViewAreReady() {
        if (programCalendarGraphic != null && programViewModel != null)
            programViewModel.getCalendarPanel().setCenter(programCalendarGraphic.getDrawingNode());
    }

    private CalendarGraphic programCalendarGraphic;
    @Override
    protected void bindPresentationModelWithLogic(ProgramPresentationModel programPresentationModel) {
        onFeesGroup().setHandler(async -> {
            if (async.failed())
                Platform.log(async.cause());
            else {
                OptionsPreselection noAccommodationOptionsPreselection = findNoAccommodationOptionsPreselection(async.result());
                Calendar programCalendar = CalendarExtractor.fromWorkingDocument(noAccommodationOptionsPreselection.getWorkingDocument());
                programCalendarGraphic = CalendarGraphic.create(programCalendar, getI18n());
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
