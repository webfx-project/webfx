package mongoose.activities.shared.logic.work;

import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.DaysArrayBuilder;
import mongoose.activities.shared.logic.time.TimeInterval;
import mongoose.entities.Document;
import mongoose.entities.Option;
import mongoose.entities.Person;
import mongoose.entities.markers.HasPersonDetails;
import mongoose.services.EventService;
import mongoose.services.PersonService;
import naga.commons.util.collection.Collections;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.UpdateStore;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class WorkingDocument {

    private final EventService eventService;
    private final Document document;
    private final List<WorkingDocumentLine> workingDocumentLines;

    public WorkingDocument(EventService eventService, List<WorkingDocumentLine> workingDocumentLines) {
        this(eventService, PersonService.getOrCreate(eventService.getEventDataSourceModel()).getPreselectionProfilePerson(), workingDocumentLines);
    }

    public WorkingDocument(EventService eventService, Person person, List<WorkingDocumentLine> workingDocumentLines) {
        this(eventService, createDocument(person, person.getStore().getDataSourceModel()), workingDocumentLines);
    }

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

    private DateTimeRange dateTimeRange;
    public DateTimeRange getDateTimeRange() {
        if (dateTimeRange == null) {
            long includedStart = Long.MAX_VALUE, excludedEnd = Long.MIN_VALUE;
            for (WorkingDocumentLine wdl : getWorkingDocumentLines()) {
                DateTimeRange wdlDateTimeRange = wdl.getDateTimeRange();
                if (wdlDateTimeRange != null && !wdlDateTimeRange.isEmpty()) {
                    TimeInterval interval = wdlDateTimeRange.getInterval().changeTimeUnit(TimeUnit.MINUTES);
                    includedStart = Math.min(includedStart, interval.getIncludedStart());
                    excludedEnd = Math.max(excludedEnd, interval.getExcludedEnd());
                }
            }
            dateTimeRange = new DateTimeRange(new TimeInterval(includedStart, excludedEnd, TimeUnit.MINUTES));
        }
        return dateTimeRange;
    }

    public WorkingDocument applyBusinessRules() {
        applyBreakfastRule();
        applyDietRule();
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
    
    private void applyDietRule() {
        if (!hasLunch() && !hasSupper()) {
            if (hasDiet())
                workingDocumentLines.remove(getDietLine());
        } else {
            WorkingDocumentLine dietLine = getDietLine();
            if (dietLine == null) {
                Option dietOption = eventService.getDietOption();
                if (dietOption == null)
                    return;
                dietLine = new WorkingDocumentLine(dietOption, this);
                workingDocumentLines.add(dietLine);
            }
            DaysArrayBuilder dab = new DaysArrayBuilder();
            if (hasLunch())
                dab.addSeries(getLunchLine().getDaysArray().toSeries(), null);
            if (hasSupper())
                dab.addSeries(getSupperLine().getDaysArray().toSeries(), null);
            dietLine.setDaysArray(dab.build());
        }
    }

    //// Accommodation line

    private WorkingDocumentLine accommodationLine;

    public WorkingDocumentLine getAccommodationLine() {
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

    //// Lunch line

    private WorkingDocumentLine lunchLine;

    private WorkingDocumentLine getLunchLine() {
        if (lunchLine == null)
            lunchLine = Collections.findFirst(workingDocumentLines, wdl -> wdl.getOption().isLunch());
        return lunchLine;
    }

    private boolean hasLunch() {
        return getLunchLine() != null;
    }

    //// Supper line

    private WorkingDocumentLine supperLine;

    private WorkingDocumentLine getSupperLine() {
        if (supperLine == null)
            supperLine = Collections.findFirst(workingDocumentLines, wdl -> wdl.getOption().isSupper());
        return supperLine;
    }

    private boolean hasSupper() {
        return getSupperLine() != null;
    }


    //// Diet line

    private WorkingDocumentLine dietLine;

    private WorkingDocumentLine getDietLine() {
        if (dietLine == null)
            dietLine = Collections.findFirst(workingDocumentLines, wdl -> wdl.getOption().isDiet());
        return dietLine;
    }

    private boolean hasDiet() {
        return getDietLine() != null;
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

    public void syncPersonDetails(HasPersonDetails p) {
        syncPersonDetails(p, document);
    }

    private static Document createDocument(Person person, DataSourceModel dataSourceModel) {
        UpdateStore store = UpdateStore.create(dataSourceModel);
        Document document = store.insertEntity(Document.class);
        syncPersonDetails(person, document);
        return document;
    }

    private static void syncPersonDetails(HasPersonDetails p1, HasPersonDetails p2) {
        p2.setFirstName(p1.getFirstName());
        p2.setLastName(p1.getLastName());
        p2.setAge(p1.getAge());
        p2.setUnemployed(p1.isUnemployed());
        p2.setFacilityFee(p1.isFacilityFee());
        p2.setWorkingVisit(p1.isWorkingVisit());
        p2.setDiscovery(p1.isDiscovery());
        p2.setDiscoveryReduced(p1.isDiscoveryReduced());
        p2.setGuest(p1.isGuest());
        p2.setResident(p1.isResident());
        p2.setResident2(p1.isResident2());
    }

}
