package mongoose.activities.shared.bookingform.shared;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.CalendarExtractor;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.activities.shared.logic.price.DocumentPricing;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.TimeInterval;
import mongoose.activities.shared.logic.ui.calendargraphic.CalendarCell;
import mongoose.activities.shared.logic.ui.calendargraphic.CalendarClickEvent;
import mongoose.activities.shared.logic.ui.calendargraphic.CalendarGraphic;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.domainmodel.format.PriceFormatter;
import mongoose.entities.Option;
import mongoose.services.EventService;
import naga.framework.orm.entity.UpdateStore;
import naga.framework.ui.i18n.I18n;
import naga.fx.spi.Toolkit;
import naga.platform.spi.Platform;

import java.util.concurrent.TimeUnit;


/**
 * @author Bruno Salmon
 */
public class BookingCalendar {

    private final Property<Node> calendarNodeProperty = new SimpleObjectProperty<>();
    private final Property<Integer> bookingPrice = new SimpleObjectProperty<>();
    private final Property<String> formattedBookingPrice = new SimpleObjectProperty<>();
    private final boolean amendable;
    private final I18n i18n;
    private final Node parentOwner;
    private WorkingDocument workingDocument;
    private CalendarGraphic calendarGraphic;

    public BookingCalendar(boolean amendable, I18n i18n, Node parentOwner) {
        this.amendable = amendable;
        this.i18n = i18n;
        this.parentOwner = parentOwner;
    }

    public Node getCalendarNode() {
        return calendarNodeProperty.getValue();
    }

    public ReadOnlyProperty<Node> calendarNodeProperty() {
        return calendarNodeProperty;
    }

    public Integer getBookingPrice() {
        return bookingPrice.getValue();
    }

    public ReadOnlyProperty<Integer> bookingPriceProperty() {
        return bookingPrice;
    }

    public String getFormattedBookingPrice() {
        return formattedBookingPrice.getValue();
    }

    public ReadOnlyProperty<String> formattedBookingPriceProperty() {
        return formattedBookingPrice;
    }

    public void createOrUpdateCalendarGraphicFromOptionsPreselection(OptionsPreselection optionsPreselection) {
        createOrUpdateCalendarGraphicFromWorkingDocument(optionsPreselection.getWorkingDocument());
    }

    public void createOrUpdateCalendarGraphicFromWorkingDocument(WorkingDocument workingDocument) {
        if (this.workingDocument != workingDocument) {
            this.workingDocument = workingDocument;
            PerformanceLogger perf = new PerformanceLogger();
            Calendar calendar = createCalendarFromWorkingDocument();
            perf.log("Calendar creation");
            Toolkit.get().scheduler().runInUiThread(() -> {
                if (calendarGraphic == null) {
                    calendarGraphic = CalendarGraphic.create(calendar, i18n);
                    calendarGraphic.setCalendarClickHandler(this::onCalendarClick);
                } else
                    calendarGraphic.setCalendar(calendar);
                perf.log("CalendarGraphic creation");
                calendarNodeProperty.setValue(calendarGraphic.getNode());
                perf.log("CalendarNode set");
                computeAndDisplayWorkingTotalPrice();
                perf.log("Price computation");
            });
        }
    }

    private Calendar createCalendarFromWorkingDocument() {
        return CalendarExtractor.createFromWorkingDocument(workingDocument, createNewMaxDateTimeRangeWorkingDocument(), i18n);
    }

    protected WorkingDocument createNewMaxDateTimeRangeWorkingDocument() {
        return createNewDateTimeRangeWorkingDocument(workingDocument.getEventService().getEvent().computeMaxDateTimeRange());
    }

    protected WorkingDocument createNewDateTimeRangeWorkingDocument(DateTimeRange workingDocumentDateTimeRange) {
        OptionsPreselection selectedOptionsPreselection = workingDocument.getEventService().getSelectedOptionsPreselection();
        return selectedOptionsPreselection == null ? null : selectedOptionsPreselection.createNewWorkingDocument(workingDocumentDateTimeRange).applyBusinessRules();
    }

    private void onCalendarClick(CalendarClickEvent event) {
        boolean editMode = event.getMouseEvent().isControlDown(); // temporary criteria
        if (!editMode) {
            if (amendable) {
                CalendarCell cell = event.getCalendarCell();
                long clickedCellMinuteStart = cell.getEpochDay() * 24 * 60 + cell.getDayTimeMinuteInterval().getIncludedStart();
                TimeInterval workingDocumentInterval = workingDocument.getDateTimeRange().getInterval().changeTimeUnit(TimeUnit.MINUTES);
                long fromArrival = Math.abs(workingDocumentInterval.getIncludedStart() - clickedCellMinuteStart);
                long fromDeparture = Math.abs(workingDocumentInterval.getExcludedEnd() - clickedCellMinuteStart);
                updateArrivalOrDepartureDateTime(event, fromArrival < fromDeparture);
            }
        } else {
            CalendarTimeline calendarTimeline = event.getCalendarTimeline();
            Option option = getCalendarTimelineOption(calendarTimeline);
            if (option != null) {
                DayTimeRangeEditor.showDayTimeRangeEditorDialog(calendarTimeline.getDayTimeRange(),
                        event.getCalendarCell().getEpochDay(),
                        calendarTimeline,
                        (newDayTimeRange, dialogCallback) -> {
                            // Creating an update store
                            UpdateStore store = UpdateStore.create(workingDocument.getEventService().getEventDataSourceModel());
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
                                    createOrUpdateCalendarGraphicFromWorkingDocument(workingDocument);
                                }
                            });
                        }, parentOwner, i18n
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
        TimeInterval currentWorkingDocumentInterval = workingDocument.getDateTimeRange().getInterval().changeTimeUnit(TimeUnit.MINUTES);
        TimeInterval newRequestedDocumentInterval =
                arrival ? new TimeInterval(clickedDayMinute + dayTimeMinuteInterval.getIncludedStart(), currentWorkingDocumentInterval.getExcludedEnd(), TimeUnit.MINUTES)
                        : new TimeInterval(currentWorkingDocumentInterval.getIncludedStart(), clickedDayMinute + cell.getDayTimeMinuteInterval().getExcludedEnd(), TimeUnit.MINUTES);
        EventService eventService = workingDocument.getEventService();
        DateTimeRange newWorkingDocumentDateTimeRange = eventService.getEvent().computeMaxDateTimeRange().intersect(newRequestedDocumentInterval.toSeries());
        WorkingDocument newWorkingDocument = createNewDateTimeRangeWorkingDocument(newWorkingDocumentDateTimeRange);
        eventService.setWorkingDocument(newWorkingDocument);
        createOrUpdateCalendarGraphicFromWorkingDocument(newWorkingDocument);
    }

    private void computeAndDisplayWorkingTotalPrice() {
        int documentPrice = DocumentPricing.computeDocumentPrice(workingDocument);
        bookingPrice.setValue(documentPrice);
        formattedBookingPrice.setValue(PriceFormatter.SINGLETON.formatWithCurrency(documentPrice, workingDocument.getEventService().getEvent()));
    }

    private static class PerformanceLogger {
        long t0 = System.currentTimeMillis();

        void log(String message) {
            long t1 = System.currentTimeMillis();
            Platform.log(message + ": " + (t1 - t0) + "ms");
            t0 = t1;
        }
    }

}
