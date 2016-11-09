package mongoose.activities.frontend.event.options;

import mongoose.activities.frontend.event.shared.BookingProcessActivity;
import mongoose.activities.shared.logic.calendar.graphic.CalendarCell;
import mongoose.activities.shared.logic.calendar.graphic.CalendarClickEvent;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.price.DocumentPricing;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.TimeInterval;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.domainmodel.format.PriceFormatter;
import naga.platform.spi.Platform;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.util.Properties;

import java.util.concurrent.TimeUnit;

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
            workingDocumentCalendarGraphic.setCalendarClickHandler(this::onCalendarClick);
            showCalendarIfBothLogicAndViewAreReady();
        }
    }

    private void onCalendarClick(CalendarClickEvent event) {
        updateArrivalDateTime(event);
    }

    private void updateArrivalDateTime(CalendarClickEvent event) {
        CalendarCell cell = event.getCalendarCell();
        DateTimeRange workingDocumentDateTimeRange = getWorkingDocument().getDateTimeRange();
        TimeInterval newArrivalToDocumentEndInterval = new TimeInterval(cell.getEpochDay() * 24 * 60 + cell.getDayTimeMinuteInterval().getIncludedStart(), workingDocumentDateTimeRange.getInterval().changeTimeUnit(TimeUnit.MINUTES).getExcludedEnd(), TimeUnit.MINUTES);
        workingDocumentDateTimeRange = workingDocumentDateTimeRange.intersect(newArrivalToDocumentEndInterval.toSeries());
        setWorkingDocument(getSelectedOptionsPreselection().createNewWorkingDocument(workingDocumentDateTimeRange).applyBusinessRules());
        createAndShowCalendarIfBothLogicAndViewAreReady();
        Platform.log("Price: " + PriceFormatter.SINGLETON.format(DocumentPricing.computeDocumentPrice(getWorkingDocument())));
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
