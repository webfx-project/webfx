package mongoose.activities.shared.bookingform.options;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mongoose.activities.shared.bookingform.shared.BookingProcessViewActivity;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.price.DocumentPricing;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.TimeInterval;
import mongoose.activities.shared.logic.ui.calendargraphic.CalendarCell;
import mongoose.activities.shared.logic.ui.calendargraphic.CalendarClickEvent;
import mongoose.activities.shared.logic.ui.calendargraphic.CalendarGraphic;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.domainmodel.format.PriceFormatter;
import mongoose.entities.Option;
import naga.framework.orm.entity.UpdateStore;
import naga.fx.spi.Toolkit;
import naga.platform.spi.Platform;

import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class OptionsViewActivity extends BookingProcessViewActivity {

    public OptionsViewActivity() {
        super("person");
    }

    @Override
    public void onResume() {
        bindPresentationModelWithLogicNow();
        super.onResume();
    }

    protected BorderPane calendarPanel;
    protected Text priceText;

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        calendarPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/calendar.svg', width: 16, height: 16}", "Attendance", getI18n());
        priceText = new Text();
        createAndShowCalendarIfBothLogicAndViewAreReady();
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(calendarPanel, null, null, new HBox(previousButton, nextButton), null);
    }

    private CalendarGraphic workingDocumentCalendarGraphic;

    private void createAndShowCalendarIfBothLogicAndViewAreReady() {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null)
            Toolkit.get().scheduler().runInUiThread(() -> {
            workingDocumentCalendarGraphic = createOrUpdateCalendarGraphicFromWorkingDocument(workingDocument, workingDocumentCalendarGraphic);
            workingDocumentCalendarGraphic.setCalendarClickHandler(this::onCalendarClick);
            showCalendarIfBothLogicAndViewAreReady();
        });
    }

    private void onCalendarClick(CalendarClickEvent event) {
        boolean editMode = event.getMouseEvent().isControlDown(); // temporary criteria
        if (!editMode) {
            CalendarCell cell = event.getCalendarCell();
            long clickedCellMinuteStart = cell.getEpochDay() * 24 * 60 + cell.getDayTimeMinuteInterval().getIncludedStart();
            TimeInterval workingDocumentInterval = getWorkingDocument().getDateTimeRange().getInterval().changeTimeUnit(TimeUnit.MINUTES);
            long fromArrival = Math.abs(workingDocumentInterval.getIncludedStart() - clickedCellMinuteStart);
            long fromDeparture = Math.abs(workingDocumentInterval.getExcludedEnd() - clickedCellMinuteStart);
            updateArrivalOrDepartureDateTime(event, fromArrival < fromDeparture);
            computeAndDisplayWorkingTotalPrice();
        } else {
            CalendarTimeline calendarTimeline = event.getCalendarTimeline();
            Option option = getCalendarTimelineOption(calendarTimeline);
            if (option != null) {
                DayTimeRangeEditor.showDayTimeRangeEditorDialog(calendarTimeline.getDayTimeRange(),
                        "Option: " + calendarTimeline.displayNameProperty().getValue(),
                        (newDayTimeRange, dialogCallback) -> {
                            // Creating an update store
                            UpdateStore store = UpdateStore.create(getDataSourceModel());
                            // Creating an instance of Option entity
                            Option updatingOption = store.getOrCreateEntity(option.getId());
                            // Updating the option time range
                            updatingOption.setTimeRange(newDayTimeRange.getText());
                            // Asking the update record this change in the database
                            store.executeUpdate().setHandler(asyncResult -> {
                                if (asyncResult.failed())
                                    dialogCallback.showException(asyncResult.cause());
                                else {
                                    dialogCallback.closeDialog();
                                    // Updating the UI
                                    option.setTimeRange(newDayTimeRange.getText());
                                    createAndShowCalendarIfBothLogicAndViewAreReady();
                                }
                            });
                        }, calendarPanel.getScene().getWindow()
                );
            }
        }
    }

    private static Option getCalendarTimelineOption(CalendarTimeline calendarTimeline) {
        Object source = calendarTimeline.getSource();
        if (source instanceof Option)
            return (Option) source;
        if (source instanceof WorkingDocumentLine)
            return ((WorkingDocumentLine) source).getOption();
        return null;
    }


    private void updateArrivalOrDepartureDateTime(CalendarClickEvent event, boolean arrival) {
        CalendarCell cell = event.getCalendarCell();
        TimeInterval dayTimeMinuteInterval = cell.getDayTimeMinuteInterval();
        long clickedDayMinute = cell.getEpochDay() * 24 * 60;
        TimeInterval currentWorkingDocumentInterval = getWorkingDocument().getDateTimeRange().getInterval().changeTimeUnit(TimeUnit.MINUTES);
        TimeInterval newRequestedDocumentInterval =
                arrival ? new TimeInterval(clickedDayMinute + dayTimeMinuteInterval.getIncludedStart(), currentWorkingDocumentInterval.getExcludedEnd(), TimeUnit.MINUTES)
                        : new TimeInterval(currentWorkingDocumentInterval.getIncludedStart(), clickedDayMinute + cell.getDayTimeMinuteInterval().getExcludedEnd(), TimeUnit.MINUTES);
        DateTimeRange newWorkingDocumentDateTimeRange = getEvent().computeMaxDateTimeRange().intersect(newRequestedDocumentInterval.toSeries());
        setWorkingDocument(createNewDateTimeRangeWorkingDocument(newWorkingDocumentDateTimeRange));
        createAndShowCalendarIfBothLogicAndViewAreReady();
    }

    private void computeAndDisplayWorkingTotalPrice() {
        priceText.setText(PriceFormatter.SINGLETON.formatWithCurrency(DocumentPricing.computeDocumentPrice(getWorkingDocument()), getEvent()));
    }

    private void showCalendarIfBothLogicAndViewAreReady() {
        if (workingDocumentCalendarGraphic != null && calendarPanel != null) {
            calendarPanel.setCenter(new VBox(priceText, workingDocumentCalendarGraphic.getNode()));
            computeAndDisplayWorkingTotalPrice();
        }
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
