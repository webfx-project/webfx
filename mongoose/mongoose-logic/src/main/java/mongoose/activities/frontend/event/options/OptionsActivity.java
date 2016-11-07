package mongoose.activities.frontend.event.options;

import mongoose.activities.frontend.event.shared.BookingProcessActivity;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.work.WorkingDocument;
import naga.platform.spi.Platform;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.util.Properties;

/**
 * @author Bruno Salmon
 */
public class OptionsActivity extends BookingProcessActivity<OptionsViewModel, OptionsPresentationModel> {

    public OptionsActivity() {
        super(OptionsPresentationModel::new, "person");
        registerViewBuilder(getClass(), new OptionsViewModelBuilder());
    }

    private OptionsViewModel optionsViewModel;
    private CalendarGraphic workingDocumentCalendarGraphic;

    @Override
    protected void initializePresentationModel(OptionsPresentationModel optionsPresentationModel) {
        super.initializePresentationModel(optionsPresentationModel);
        createAndShowCalendarIfBothLogicAndViewAreReady();
    }

    @Override
    protected void bindViewModelWithPresentationModel(OptionsViewModel vm, OptionsPresentationModel pm) {
        super.bindViewModelWithPresentationModel(vm, pm);
        this.optionsViewModel = vm;
        showCalendarIfBothLogicAndViewAreReady();
    }

    private void createAndShowCalendarIfBothLogicAndViewAreReady() {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null) {
            workingDocumentCalendarGraphic = createOrUpdateCalendarGraphicFromWorkingDocument(workingDocument, workingDocumentCalendarGraphic);
            showCalendarIfBothLogicAndViewAreReady();
        }
    }

    private void showCalendarIfBothLogicAndViewAreReady() {
        if (workingDocumentCalendarGraphic != null && optionsViewModel != null)
            Toolkit.get().scheduler().runInUiThread(() -> optionsViewModel.getCalendarPanel().setCenter(workingDocumentCalendarGraphic.getDrawingNode()));
    }

    @Override
    protected void bindPresentationModelWithLogic(OptionsPresentationModel pm) {
        // Doing now but also on event change
        Properties.runNowAndOnPropertiesChange(property -> bindPresentationModelWithLogicNow(), pm.eventIdProperty());
    }

    private void bindPresentationModelWithLogicNow() {
        onFeesGroup().setHandler(async -> {
            if (async.failed())
                Platform.log(async.cause());
            else
                createAndShowCalendarIfBothLogicAndViewAreReady();
        });
    }
}
