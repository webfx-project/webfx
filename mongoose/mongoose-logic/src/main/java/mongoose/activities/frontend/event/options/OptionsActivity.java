package mongoose.activities.frontend.event.options;

import mongoose.activities.frontend.event.shared.BookingProcessActivity;
import mongoose.activities.shared.logic.calendar.graphic.CalendarCell;
import mongoose.activities.shared.logic.calendar.graphic.CalendarClickEvent;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.TimeInterval;
import mongoose.activities.shared.logic.work.WorkingDocument;
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
        CalendarCell cell = event.getCalendarCell();
        long clickedCellMinuteStart = cell.getEpochDay() * 24 * 60 + cell.getDayTimeMinuteInterval().getIncludedStart();
        TimeInterval workingDocumentInterval = getWorkingDocument().getDateTimeRange().getInterval().changeTimeUnit(TimeUnit.MINUTES);
        long fromArrival = Math.abs(workingDocumentInterval.getIncludedStart() - clickedCellMinuteStart);
        long fromDeparture = Math.abs(workingDocumentInterval.getExcludedEnd() - clickedCellMinuteStart);
        updateArrivalOrDepartureDateTime(event, fromArrival < fromDeparture);
        //Platform.log("Price: " + PriceFormatter.SINGLETON.format(DocumentPricing.computeDocumentPrice(getWorkingDocument())));
    }

    private void updateArrivalOrDepartureDateTime(CalendarClickEvent event, boolean arrival) {
        CalendarCell cell = event.getCalendarCell();
        TimeInterval dayTimeMinuteInterval = cell.getDayTimeMinuteInterval();
        long clickedDayMinute = cell.getEpochDay() * 24 * 60;
        TimeInterval currentWorkingDocumentInterval = getWorkingDocument().getDateTimeRange().getInterval().changeTimeUnit(TimeUnit.MINUTES);
        TimeInterval newRequestedDocumentInterval =
                arrival ? new TimeInterval(clickedDayMinute + dayTimeMinuteInterval.getIncludedStart(), currentWorkingDocumentInterval.getExcludedEnd(), TimeUnit.MINUTES)
                :         new TimeInterval(currentWorkingDocumentInterval.getIncludedStart(), clickedDayMinute + cell.getDayTimeMinuteInterval().getExcludedEnd(), TimeUnit.MINUTES);
        DateTimeRange newWorkingDocumentDateTimeRange = getEventMaxDateTimeRange().intersect(newRequestedDocumentInterval.toSeries());
        setWorkingDocument(createNewDateTimeRangeWorkingDocument(newWorkingDocumentDateTimeRange));
        createAndShowCalendarIfBothLogicAndViewAreReady();
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
