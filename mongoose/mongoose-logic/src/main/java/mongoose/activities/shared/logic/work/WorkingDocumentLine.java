package mongoose.activities.shared.logic.work;

import mongoose.activities.shared.logic.preselection.OptionPreselection;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.activities.shared.logic.time.DaysArray;
import mongoose.activities.shared.logic.time.DaysArrayBuilder;
import mongoose.entities.*;
import mongoose.entities.markers.HasItemFamilyType;
import mongoose.services.EventService;
import naga.framework.orm.entity.Entity;
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
        DateTimeRange croppingDateTimeRange = workingDocumentDateTimeRange == null ? optionPreselection.getDateTimeRange()
                : workingDocumentDateTimeRange.intersect(option.getParsedDateTimeRangeOrParent());
        setDateTimeRange(cropDateTimeRange(croppingDateTimeRange, dayTimeRange));
        documentLine = null;
        attendances = null;
    }

    public WorkingDocumentLine(Option option, WorkingDocument workingDocument) {
        optionPreselection = null;
        this.option = option;
        site = option.getSite();
        arrivalSite = option.getArrivalSite();
        item = option.getItem();
        dayTimeRange = option.getParsedTimeRangeOrParent();
        DateTimeRange workingDocumentDateTimeRange = workingDocument.getDateTimeRange();
        DateTimeRange croppingDateTimeRange = workingDocumentDateTimeRange.intersect(option.getParsedDateTimeRangeOrParent());
        setDateTimeRange(cropDateTimeRange(croppingDateTimeRange, dayTimeRange));
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
        dayTimeRange = option == null ? null : DayTimeRange.parse(option.getTimeRange());
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
        DateTimeRange croppingDateTimeRange = workingDocumentDateTimeRange.intersect(option.getParsedDateTimeRangeOrParent());
        setDateTimeRange(cropDateTimeRange(croppingDateTimeRange, dayTimeRange));
    }

    private static DateTimeRange cropDateTimeRange(DateTimeRange dateTimeRange, DayTimeRange dayTimeRange) {
        return dateTimeRange == null || dayTimeRange == null ? dateTimeRange : dateTimeRange.intersect(dayTimeRange);
    }

    private Option findDocumentLineOption(EventService eventService) {
        Site site = documentLine.getSite();
        Item item = documentLine.getItem();
        if (site != null && item != null)
            for (Option o : eventService.getEventOptions())
                if (Entity.sameId(o.getSite(), site) && Entity.sameId(o.getItem(), item))
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
        this.dateTimeRange = dateTimeRange;
        daysArray = null; // will be lazy computed on getter from daysArray
    }

    public DateTimeRange getDateTimeRange() {
        // Lazy computation if not set
        if (dateTimeRange == null && daysArray != null)
            dateTimeRange = cropDateTimeRange(new DateTimeRange(daysArray.toSeries()), dayTimeRange);
        return dateTimeRange;
    }

    public DayTimeRange getDayTimeRange() {
        return dayTimeRange;
    }

    public void setDaysArray(DaysArray daysArray) {
        this.daysArray = daysArray;
        dateTimeRange = null; // will be lazy computed on getter from daysArray
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
        return item != null ? item.getItemFamilyType() : option.getItemFamilyType();
    }
}
