package mongoose.activities.bothends.book.shared;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import mongoose.activities.bothends.logic.calendar.Calendar;
import mongoose.activities.bothends.logic.calendar.CalendarExtractor;
import mongoose.activities.bothends.logic.preselection.OptionsPreselection;
import mongoose.activities.bothends.logic.time.DateTimeRange;
import mongoose.activities.bothends.logic.time.TimeInterval;
import mongoose.activities.bothends.logic.ui.calendargraphic.CalendarCell;
import mongoose.activities.bothends.logic.ui.calendargraphic.CalendarClickEvent;
import mongoose.activities.bothends.logic.ui.calendargraphic.CalendarGraphic;
import mongoose.activities.bothends.logic.work.WorkingDocument;
import mongoose.activities.bothends.logic.work.WorkingDocumentLine;
import mongoose.activities.bothends.logic.work.merge.WorkingDocumentMerger;
import mongoose.domainmodel.formatters.PriceFormatter;
import mongoose.aggregates.EventAggregate;
import mongoose.util.PerformanceLogger;
import webfx.framework.orm.entity.Entities;
import webfx.fx.spi.Toolkit;
import webfx.platform.services.log.Logger;

import java.util.concurrent.TimeUnit;


/**
 * @author Bruno Salmon
 */
public class BookingCalendar {

    private final Property<Node> calendarNodeProperty = new SimpleObjectProperty<>();
    private final Property<Integer> bookingPrice = new SimpleObjectProperty<>();
    private final Property<String> formattedBookingPrice = new SimpleObjectProperty<>();
    private final boolean amendable;
    protected WorkingDocument workingDocument;
    private CalendarGraphic calendarGraphic;
    private Runnable onAttendanceChangedRunnable;

    public BookingCalendar(boolean amendable) {
        this.amendable = amendable;
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

    public void setOnAttendanceChangedRunnable(Runnable onAttendanceChangedRunnable) {
        this.onAttendanceChangedRunnable = onAttendanceChangedRunnable;
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
                    calendarGraphic = CalendarGraphic.create(calendar);
                    calendarGraphic.setCalendarClickHandler(this::onCalendarClick);
                } else
                    calendarGraphic.setCalendar(calendar);
                perf.log("CalendarGraphic creation");
                calendarNodeProperty.setValue(calendarGraphic.getNode());
                perf.log("CalendarNode set");
                displayWorkingTotalPrice();
                perf.log("Price computation");
            });
        }
    }

    private Calendar createCalendarFromWorkingDocument() {
        return CalendarExtractor.createFromWorkingDocument(workingDocument, createNewMaxDateTimeRangeWorkingDocument());
    }

    protected WorkingDocument createNewMaxDateTimeRangeWorkingDocument() {
        return createNewDateTimeRangeWorkingDocument(workingDocument.getEventAggregate().getEvent().computeMaxDateTimeRange(), true);
    }

    protected WorkingDocument createNewDateTimeRangeWorkingDocument(DateTimeRange workingDocumentDateTimeRange, boolean applyBusinessRules) {
        OptionsPreselection selectedOptionsPreselection = workingDocument.getEventAggregate().getSelectedOptionsPreselection();
        if (selectedOptionsPreselection == null)
            selectedOptionsPreselection = findWorkingDocumentOptionsPreselection();
        WorkingDocument newWorkingDocument = selectedOptionsPreselection.createNewWorkingDocument(workingDocumentDateTimeRange);
        if (applyBusinessRules)
            newWorkingDocument.applyBusinessRules();
        return newWorkingDocument;
    }

    private OptionsPreselection findWorkingDocumentOptionsPreselection() {
        for (FeesGroup feesGroup : workingDocument.getEventAggregate().getFeesGroups()) {
            for (OptionsPreselection optionsPreselection : feesGroup.getOptionsPreselections()) {
                if (workingDocumentMatchesOptionsPreselection(optionsPreselection))
                    return optionsPreselection;
            }
        }
        Logger.log("Warning: no OptionsPreselection found for this working document");
        return null;
    }

    private boolean workingDocumentMatchesOptionsPreselection(OptionsPreselection optionsPreselection) {
        return sameLine(workingDocument.getAccommodationLine(), optionsPreselection.getAccommodationLine());
    }

    private static boolean sameLine(WorkingDocumentLine wdl1, WorkingDocumentLine wdl2) {
        return wdl1 == wdl2 || wdl1 != null && wdl2 != null && Entities.sameId(wdl1.getSite(), wdl2.getSite()) && Entities.sameId(wdl1.getItem(), wdl2.getItem());
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

    private void updateArrivalOrDepartureDateTime(CalendarClickEvent clickEvent, boolean arrival) {
        updateCalendarWorkingDocumentInterval(computeNewRequestedDocumentInterval(clickEvent, arrival));
        if (onAttendanceChangedRunnable != null)
            onAttendanceChangedRunnable.run();
        else
            createOrUpdateCalendarGraphicFromWorkingDocument(workingDocument, true);
    }

    private TimeInterval computeNewRequestedDocumentInterval(CalendarClickEvent clickEvent, boolean arrival) {
        //Logger.log("currentWorkingDocument: " + workingDocument.getDateTimeRange().getText());
        CalendarCell clickedCell = clickEvent.getCalendarCell();
        long clickedDayMinute = clickedCell.getEpochDay() * 24 * 60;
        TimeInterval clickedDayTimeMinuteInterval = clickedCell.getDayTimeMinuteInterval();
        long clickedIncludedStart = clickedDayMinute + clickedDayTimeMinuteInterval.getIncludedStart();
        long clickedExcludedEnd = clickedDayMinute + clickedDayTimeMinuteInterval.getExcludedEnd();
        TimeInterval currentWorkingDocumentInterval = workingDocument.getDateTimeRange().getInterval().changeTimeUnit(TimeUnit.MINUTES);
        long workingIncludedStart = currentWorkingDocumentInterval.getIncludedStart();
        long workingExcludedEnd = currentWorkingDocumentInterval.getExcludedEnd();
        // If the document is empty (no cell selected)
        if (workingIncludedStart >= workingExcludedEnd) { // this indicates the document is empty
            // In this case, we select the clicked cell (the cell behaves like an empty checkbox that the user ticks)
            workingIncludedStart = clickedIncludedStart;
            workingExcludedEnd = clickedExcludedEnd;
        } else if (arrival) { // The user specified the arrival cell
            // If that clicked cell was already the arrival cell, we deselect it (the cell behaves like a checked checkbox that the user unticks)
            if (workingIncludedStart >= clickedIncludedStart && workingIncludedStart < clickedExcludedEnd)
                clickedIncludedStart = clickedExcludedEnd; // This will unselect the clicked cell
        } else { // The user specified the departure cell
            // If that clicked cell was already the departure cell, we deselect it (the cell behaves like a checked checkbox that the user unticks)
            if (workingExcludedEnd > clickedIncludedStart && workingExcludedEnd <= clickedExcludedEnd)
                clickedExcludedEnd = clickedIncludedStart; // This will unselect the clicked cell
        }
        return arrival ? new TimeInterval(clickedIncludedStart, workingExcludedEnd, TimeUnit.MINUTES)
                       : new TimeInterval(workingIncludedStart, clickedExcludedEnd, TimeUnit.MINUTES);
    }

    private void updateCalendarWorkingDocumentInterval(TimeInterval newRequestedDocumentInterval) {
        EventAggregate eventAggregate = workingDocument.getEventAggregate();
        DateTimeRange newWorkingDocumentDateTimeRange = eventAggregate.getEvent().computeMaxDateTimeRange().intersect(newRequestedDocumentInterval.toSeries());
        //Logger.log("newWorkingDocumentDateTimeRange: " + newWorkingDocumentDateTimeRange.getText());
        WorkingDocument newCalendarWorkingDocument = createNewDateTimeRangeWorkingDocument(newWorkingDocumentDateTimeRange, false);
        //Logger.log("newCalendarWorkingDocument: " + newCalendarWorkingDocument.getDateTimeRange().getText());
        WorkingDocument newWorkingDocument = WorkingDocumentMerger.mergeWorkingDocuments(workingDocument, newCalendarWorkingDocument, newWorkingDocumentDateTimeRange);
        //Logger.log("newWorkingDocument: " + newWorkingDocument.getDateTimeRange().getText());
        eventAggregate.setWorkingDocument(workingDocument = newWorkingDocument);
    }

    private void displayWorkingTotalPrice() {
        int documentPrice = workingDocument.getComputedPrice();
        bookingPrice.setValue(documentPrice);
        formattedBookingPrice.setValue(PriceFormatter.formatWithCurrency(documentPrice, workingDocument.getEventAggregate().getEvent()));
    }

}
