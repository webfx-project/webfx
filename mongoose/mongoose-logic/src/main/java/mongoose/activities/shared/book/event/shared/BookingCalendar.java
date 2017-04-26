package mongoose.activities.shared.book.event.shared;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.CalendarExtractor;
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
import mongoose.services.EventService;
import mongoose.util.PerformanceLogger;
import naga.framework.orm.entity.Entity;
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
    protected final I18n i18n;
    protected WorkingDocument workingDocument;
    private CalendarGraphic calendarGraphic;

    public BookingCalendar(boolean amendable, I18n i18n) {
        this.amendable = amendable;
        this.i18n = i18n;
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
        createOrUpdateCalendarGraphicFromWorkingDocument(optionsPreselection.getWorkingDocument(), false);
    }

    public void createOrUpdateCalendarGraphicFromWorkingDocument(WorkingDocument workingDocument, boolean forceRefresh) {
        if (this.workingDocument != workingDocument || forceRefresh) {
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
        if (selectedOptionsPreselection == null)
            selectedOptionsPreselection = findWorkingDocumentOptionsPreselection();
        return selectedOptionsPreselection.createNewWorkingDocument(workingDocumentDateTimeRange).applyBusinessRules();
    }

    private OptionsPreselection findWorkingDocumentOptionsPreselection() {
        for (FeesGroup feesGroup : workingDocument.getEventService().getFeesGroups()) {
            for (OptionsPreselection optionsPreselection : feesGroup.getOptionsPreselections()) {
                if (workingDocumentMatchesOptionsPreselection(optionsPreselection))
                    return optionsPreselection;
            }
        }
        Platform.log("Warning: no OptionsPreselection found for this working document");
        return null;
    }

    private boolean workingDocumentMatchesOptionsPreselection(OptionsPreselection optionsPreselection) {
        return sameLine(workingDocument.getAccommodationLine(), optionsPreselection.getAccommodationLine());
    }

    private static boolean sameLine(WorkingDocumentLine wdl1, WorkingDocumentLine wdl2) {
        return wdl1 == wdl2 || wdl1 != null && Entity.sameId(wdl1.getSite(), wdl2.getSite()) && Entity.sameId(wdl1.getItem(), wdl2.getItem());
    }

    protected void onCalendarClick(CalendarClickEvent event) {
        if (amendable) {
            CalendarCell cell = event.getCalendarCell();
            long clickedCellMinuteStart = cell.getEpochDay() * 24 * 60 + cell.getDayTimeMinuteInterval().getIncludedStart();
            TimeInterval workingDocumentInterval = workingDocument.getDateTimeRange().getInterval().changeTimeUnit(TimeUnit.MINUTES);
            long fromArrival = Math.abs(workingDocumentInterval.getIncludedStart() - clickedCellMinuteStart);
            long fromDeparture = Math.abs(workingDocumentInterval.getExcludedEnd() - clickedCellMinuteStart);
            updateArrivalOrDepartureDateTime(event, fromArrival < fromDeparture);
        }
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
        createOrUpdateCalendarGraphicFromWorkingDocument(newWorkingDocument, false);
    }

    private void computeAndDisplayWorkingTotalPrice() {
        int documentPrice = DocumentPricing.computeDocumentPrice(workingDocument);
        bookingPrice.setValue(documentPrice);
        formattedBookingPrice.setValue(PriceFormatter.SINGLETON.formatWithCurrency(documentPrice, workingDocument.getEventService().getEvent()));
    }

}
