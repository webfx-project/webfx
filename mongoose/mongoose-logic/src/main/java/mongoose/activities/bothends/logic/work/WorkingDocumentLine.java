package mongoose.activities.bothends.logic.work;

import mongoose.activities.bothends.logic.preselection.OptionPreselection;
import mongoose.activities.bothends.logic.time.DateTimeRange;
import mongoose.activities.bothends.logic.time.DayTimeRange;
import mongoose.activities.bothends.logic.time.DaysArray;
import mongoose.activities.bothends.logic.time.DaysArrayBuilder;
import mongoose.activities.bothends.logic.work.business.logic.OptionLogic;
import mongoose.entities.*;
import mongoose.entities.markers.HasItemFamilyType;
import mongoose.services.EventService;
import naga.framework.orm.entity.Entities;
import naga.util.collection.Collections;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class WorkingDocumentLine implements HasItemFamilyType {

    private WorkingDocument workingDocument;
    private final OptionPreselection optionPreselection;
    private final Option option;
    private DocumentLine documentLine;
    private List<Attendance> attendances;
    private final Site site;
    private final Site arrivalSite;
    private final Item item;
    private final DayTimeRange dayTimeRange;
    private DateTimeRange dateTimeRange;
    private DaysArray daysArray;
    private int price;
    private boolean rounded;

    public WorkingDocumentLine(OptionPreselection optionPreselection) {
        this(optionPreselection, null);
    }

    public WorkingDocumentLine(OptionPreselection optionPreselection, DateTimeRange workingDocumentDateTimeRange) {
        this.optionPreselection = optionPreselection;
        option = optionPreselection.getOption();
        site = option.getSite();
        arrivalSite = option.getArrivalSite();
        item = option.getItem();
        dayTimeRange = optionPreselection.getDayTimeRange();
        initializeDateTimeRange(workingDocumentDateTimeRange);
        documentLine = null;
        attendances = null;
    }

    public WorkingDocumentLine(Option option, WorkingDocument workingDocument) {
        this(option, workingDocument, workingDocument.getDateTimeRange());
    }

    public WorkingDocumentLine(Option option, WorkingDocument workingDocument, DateTimeRange workingDocumentDateTimeRange) {
        optionPreselection = null;
        this.option = option;
        site = option.getSite();
        arrivalSite = option.getArrivalSite();
        item = option.getItem();
        dayTimeRange = option.getParsedTimeRangeOrParent();
        initializeDateTimeRange(workingDocumentDateTimeRange);
        documentLine = null;
        attendances = null;
        setWorkingDocument(workingDocument);
    }

    public WorkingDocumentLine(DocumentLine documentLine, List<Attendance> attendances, EventService eventService) {
        this.documentLine = documentLine;
        this.attendances = attendances;
        optionPreselection = null;
        option = findDocumentLineOption(eventService);
        site = documentLine.getSite();
        arrivalSite = documentLine.getArrivalSite();
        item = documentLine.getItem();
        dayTimeRange = option == null ? null : option.getParsedTimeRangeOrParent();
        DaysArrayBuilder dab = new DaysArrayBuilder();
        Collections.forEach(attendances, a -> dab.addDate(a.getDate()));
        setDaysArray(dab.build());
    }

    public WorkingDocumentLine(WorkingDocumentLine wdl, DateTimeRange workingDocumentDateTimeRange) {
        documentLine = wdl.documentLine;
        attendances = wdl.attendances;
        optionPreselection = wdl.optionPreselection;
        option = wdl.option;
        site = wdl.site;
        arrivalSite = wdl.arrivalSite;
        item = wdl.item;
        dayTimeRange = wdl.dayTimeRange;
        initializeDateTimeRange(workingDocumentDateTimeRange);
    }

    public void initializeDateTimeRange(DateTimeRange workingDocumentDateTimeRange) {
        setDateTimeRange(computeCroppedOptionDateTimeRange(option, dayTimeRange, workingDocumentDateTimeRange, optionPreselection));
    }

    public static DateTimeRange computeCroppedOptionDateTimeRange(Option option, DateTimeRange workingDocumentDateTimeRange) {
        return computeCroppedOptionDateTimeRange(option, option.getParsedTimeRangeOrParent(), workingDocumentDateTimeRange, null);
    }

    public static DateTimeRange computeCroppedOptionDateTimeRange(Option option, DayTimeRange dayTimeRange, DateTimeRange workingDocumentDateTimeRange, OptionPreselection optionPreselection) {
        DateTimeRange dateTimeRangeToCrop;
        if (!OptionLogic.isOptionAttendanceVariable(option))
            dateTimeRangeToCrop = option.getParsedDateTimeRangeOrParent();
        else
            dateTimeRangeToCrop = workingDocumentDateTimeRange != null ?
                    workingDocumentDateTimeRange.intersect(option.getParsedDateTimeRangeOrParent())
                    : optionPreselection != null ? optionPreselection.getDateTimeRange() : null;
        return DateTimeRange.cropDateTimeRangeWithDayTime(dateTimeRangeToCrop, dayTimeRange);
    }

    private Option findDocumentLineOption(EventService eventService) {
        Site site = documentLine.getSite();
        Item item = documentLine.getItem();
        if (site != null && item != null)
            for (Option o : eventService.getEventOptions())
                if (Entities.sameId(o.getSite(), site) && Entities.sameId(o.getItem(), item))
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

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public Site getSite() {
        return site;
    }

    public Site getArrivalSite() {
        return arrivalSite;
    }

    public Item getItem() {
        return item;
    }

    public boolean isConcrete() {
        return option == null || option.isConcrete();
    }

    public void setDateTimeRange(DateTimeRange dateTimeRange) {
        clearAttendanceCache();
        this.dateTimeRange = dateTimeRange;
    }

    private void clearAttendanceCache() {
        dateTimeRange = null; // will be lazy computed on getter from daysArray (if set)
        daysArray = null; // will be lazy computed on getter from dateTimeRange (if set)
        if (workingDocument != null)
            workingDocument.clearComputedDateTimeRange();
    }

    public DateTimeRange getDateTimeRange() {
        // Lazy computation if not set
        if (dateTimeRange == null && daysArray != null)
            dateTimeRange = DateTimeRange.cropDateTimeRangeWithDayTime(new DateTimeRange(daysArray.toSeries()), dayTimeRange);
        return dateTimeRange;
    }

    public DayTimeRange getDayTimeRange() {
        return dayTimeRange;
    }

    public void setDaysArray(DaysArray daysArray) {
        clearAttendanceCache();
        this.daysArray = daysArray;
    }

    public DaysArray getDaysArray() {
        // Lazy computation if not set
        if (daysArray == null && dateTimeRange != null)
            daysArray = dateTimeRange.getDaysArray(dayTimeRange);
        return daysArray;
    }

    public boolean isCancelled() {
        return documentLine != null && documentLine.isCancelled();
    }

    public LocalDate firstDate() {
        //return attendances.get(0).getDate();
        return getDaysArray().getFirstDate();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void incrementPrice(int priceIncrement) {
        price += priceIncrement;
    }

    public boolean isRounded() {
        return rounded;
    }

    public void setRounded(boolean rounded) {
        this.rounded = rounded;
    }

    @Override
    public ItemFamilyType getItemFamilyType() {
        return (item != null ? item : option).getItemFamilyType();
    }
}
