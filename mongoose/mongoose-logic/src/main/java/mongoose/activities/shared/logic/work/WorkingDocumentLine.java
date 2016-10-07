package mongoose.activities.shared.logic.work;

import mongoose.activities.shared.logic.preselection.OptionPreselection;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.activities.shared.logic.time.DaysArray;
import mongoose.activities.shared.logic.time.DaysArrayBuilder;
import mongoose.entities.*;
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
    private final DocumentLine documentLine;
    private final List<Attendance> attendances;
    private final Site site;
    private final Item item;
    private final DateTimeRange dateTimeRange;
    private final DayTimeRange dayTimeRange;
    private DaysArray daysArray;

    public WorkingDocumentLine(OptionPreselection optionPreselection) {
        this.optionPreselection = optionPreselection;
        option = optionPreselection.getOption();
        site = option.getSite();
        item = option.getItem();
        dateTimeRange = optionPreselection.getDateTimeRange();
        dayTimeRange = optionPreselection.getDayTimeRange();
        daysArray = dateTimeRange.getDaysArray(dayTimeRange);
        documentLine = null;
        attendances = null;
    }

    public WorkingDocumentLine(Option option, WorkingDocument workingDocument) {
        optionPreselection = null;
        this.option = option;
        site = option.getSite();
        item = option.getItem();
        dateTimeRange = DateTimeRange.parse(option.getDateTimeRangeOrParent());
        dayTimeRange = DayTimeRange.parse(option.getTimeRange());
        documentLine = null;
        attendances = null;
        daysArray = null;
        setWorkingDocument(workingDocument);
    }

    public WorkingDocumentLine(DocumentLine documentLine, List<Attendance> attendances) {
        this.documentLine = documentLine;
        this.attendances = attendances;
        optionPreselection = null;
        option = findDocumentLineOption();
        site = documentLine.getSite();
        item = documentLine.getItem();
        dateTimeRange = option == null ? null : DateTimeRange.parse(option.getDateTimeRangeOrParent());
        dayTimeRange = option == null ? null : DayTimeRange.parse(option.getTimeRange());
        DaysArrayBuilder b = new DaysArrayBuilder();
        Collections.forEach(attendances, a -> b.addDate(a.getDate()));
        daysArray = b.build();
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
