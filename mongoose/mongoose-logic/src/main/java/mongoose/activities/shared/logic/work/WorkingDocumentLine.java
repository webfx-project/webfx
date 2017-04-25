package mongoose.activities.shared.logic.work;

import mongoose.activities.shared.logic.preselection.OptionPreselection;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.activities.shared.logic.time.DaysArray;
import mongoose.activities.shared.logic.time.DaysArrayBuilder;
import mongoose.entities.*;
import naga.commons.util.Objects;
import naga.commons.util.collection.Collections;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class WorkingDocumentLine {

    private WorkingDocument workingDocument;
    private final OptionPreselection optionPreselection;
    private final Option option;
    private DocumentLine documentLine;
    private final List<Attendance> attendances;
    private final Site site;
    private final Item item;
    private final DateTimeRange dateTimeRange;
    private final DayTimeRange dayTimeRange;
    private DaysArray daysArray;

    public WorkingDocumentLine(OptionPreselection optionPreselection) {
        this(optionPreselection, null);
    }

    public WorkingDocumentLine(OptionPreselection optionPreselection, DateTimeRange workingDocumentDateTimeRange) {
        this.optionPreselection = optionPreselection;
        option = optionPreselection.getOption();
        site = option.getSite();
        item = option.getItem();
        dayTimeRange = optionPreselection.getDayTimeRange();
        DateTimeRange croppingDateTimeRange = workingDocumentDateTimeRange == null ? optionPreselection.getDateTimeRange()
                : workingDocumentDateTimeRange.intersect(option.getParsedDateTimeRangeOrParent());
        dateTimeRange = cropDateTimeRange(croppingDateTimeRange, dayTimeRange);
        daysArray = dateTimeRange.getDaysArray(dayTimeRange);
        documentLine = null;
        attendances = null;
    }

    public WorkingDocumentLine(Option option, WorkingDocument workingDocument) {
        optionPreselection = null;
        this.option = option;
        site = option.getSite();
        item = option.getItem();
        dayTimeRange = option.getParsedTimeRangeOrParent();
        DateTimeRange workingDocumentDateTimeRange = Objects.coalesce(option.getParsedDateTimeRangeOrParent(), workingDocument.getDateTimeRange());
        DateTimeRange croppingDateTimeRange = workingDocumentDateTimeRange.intersect(option.getParsedDateTimeRangeOrParent());
        dateTimeRange = cropDateTimeRange(croppingDateTimeRange, dayTimeRange);
        documentLine = null;
        attendances = null;
        daysArray = dateTimeRange == null ? null : dateTimeRange.getDaysArray(dayTimeRange);
        setWorkingDocument(workingDocument);
    }

    public WorkingDocumentLine(DocumentLine documentLine, List<Attendance> attendances) {
        this.documentLine = documentLine;
        this.attendances = attendances;
        optionPreselection = null;
        option = findDocumentLineOption();
        site = documentLine.getSite();
        item = documentLine.getItem();
        dayTimeRange = option == null ? null : DayTimeRange.parse(option.getTimeRange());
        dateTimeRange = option == null ? null : cropDateTimeRange(option.getParsedDateTimeRangeOrParent(), dayTimeRange);
        DaysArrayBuilder b = new DaysArrayBuilder();
        Collections.forEach(attendances, a -> b.addDate(a.getDate()));
        daysArray = b.build();
    }

    private static DateTimeRange cropDateTimeRange(DateTimeRange dateTimeRange, DayTimeRange dayTimeRange) {
        return dateTimeRange == null || dayTimeRange == null ? dateTimeRange : dateTimeRange.intersect(dayTimeRange);
    }

    private Option findDocumentLineOption() {
        Site site = documentLine.getSite();
        Item item = documentLine.getItem();
        if (site != null && item != null)
            for (Option o : workingDocument.getEventService().getEventOptions())
                if (o.getSite() == site && o.getItem() == item)
                    return o;
        return null;
    }

    void setWorkingDocument(WorkingDocument workingDocument) {
        this.workingDocument = workingDocument;
    }

    public WorkingDocument getWorkingDocument() {
        return workingDocument;
    }

    public OptionPreselection getOptionPreselection() {
        return optionPreselection;
    }

    public Option getOption() {
        return option;
    }

    public void setDocumentLine(DocumentLine documentLine) {
        this.documentLine = documentLine;
    }

    public DocumentLine getDocumentLine() {
        return documentLine;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public Site getSite() {
        return site;
    }

    public Item getItem() {
        return item;
    }

    public boolean isConcrete() {
        return option == null || option.isConcrete();
    }

    public DateTimeRange getDateTimeRange() {
        return dateTimeRange;
    }

    public DayTimeRange getDayTimeRange() {
        return dayTimeRange;
    }

    public void setDaysArray(DaysArray daysArray) {
        this.daysArray = daysArray;
    }

    public DaysArray getDaysArray() {
        return daysArray;
    }

    public boolean isCancelled() {
        return documentLine != null && documentLine.isCancelled();
    }

    public LocalDate firstDate() {
        //return attendances.get(0).getDate();
        return daysArray.getFirstDate();
    }
}
