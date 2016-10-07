package mongoose.activities.shared.logic.work;

import mongoose.entities.Document;
import mongoose.entities.Option;
import mongoose.services.EventService;
import naga.commons.util.collection.Collections;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class WorkingDocument {

    private final EventService eventService;
    private final Document document;
    private final List<WorkingDocumentLine> workingDocumentLines;

    public WorkingDocument(EventService eventService, Document document, List<WorkingDocumentLine> workingDocumentLines) {
        this.eventService = eventService;
        this.document = document;
        this.workingDocumentLines = workingDocumentLines;
        Collections.forEach(workingDocumentLines, wdl -> wdl.setWorkingDocument(this));
    }

    public EventService getEventService() {
        return eventService;
    }

    public Document getDocument() {
        return document;
    }

    public List<WorkingDocumentLine> getWorkingDocumentLines() {
        return workingDocumentLines;
    }

    public WorkingDocument applyBusinessRules() {
        applyBreakfastRule();
        return this;
    }

    private void applyBreakfastRule() {
        if (!hasAccommodation())
            workingDocumentLines.remove(getBreakfastLine());
        else if (!hasBreakfast()) {
            Option breakfastOption = eventService.getBreakfastOption();
            if (breakfastOption != null)
                breakfastLine = addNewDependantLine(breakfastOption, getAccommodationLine(), 1);
        }
    }

    //// Accommodation line

    private WorkingDocumentLine accommodationLine;

    private WorkingDocumentLine getAccommodationLine() {
        if (accommodationLine == null)
            accommodationLine = Collections.findFirst(workingDocumentLines, wdl -> wdl.getOption().isAccommodation());
        return accommodationLine;
    }

    private boolean hasAccommodation() {
        return getAccommodationLine() != null;
    }

    //// Breakfast line

    private WorkingDocumentLine breakfastLine;

    private WorkingDocumentLine getBreakfastLine() {
        if (breakfastLine == null)
            breakfastLine = Collections.findFirst(workingDocumentLines, wdl -> wdl.getOption().isBreakfast());
        return breakfastLine;
    }

    private boolean hasBreakfast() {
        return getBreakfastLine() != null;
    }

    private WorkingDocumentLine addNewDependantLine(Option dependantOption, WorkingDocumentLine masterLine, long shiftDays) {
        WorkingDocumentLine dependantLine = new WorkingDocumentLine(dependantOption, this);
        workingDocumentLines.add(dependantLine);
        applySameAttendances(dependantLine, masterLine, shiftDays);
        return dependantLine;
    }

    private void applySameAttendances(WorkingDocumentLine dependantLine, WorkingDocumentLine masterLine, long shiftDays) {
        dependantLine.setDaysArray(masterLine.getDaysArray().shift(shiftDays));
    }

}
