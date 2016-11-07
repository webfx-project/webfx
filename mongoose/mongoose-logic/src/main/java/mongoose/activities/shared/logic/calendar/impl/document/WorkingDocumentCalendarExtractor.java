package mongoose.activities.shared.logic.calendar.impl.document;

import javafx.beans.property.Property;
import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.CalendarExtractor;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.calendar.impl.CalendarImpl;
import mongoose.activities.shared.logic.calendar.impl.CalendarTimelineImpl;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.activities.shared.logic.time.DaysArray;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.entities.Label;
import mongoose.entities.Option;
import mongoose.util.Labels;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.drawing.paint.Color;
import naga.toolkit.drawing.paint.Paint;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class WorkingDocumentCalendarExtractor implements CalendarExtractor<WorkingDocument> {

    private final static WorkingDocumentCalendarExtractor SINGLETON = new WorkingDocumentCalendarExtractor();

    private final static Paint TEACHING_FILL = Color.web("0xF5A463");
    private final static Paint ACCOMMODATION_FILL = Color.web("0x484A61");
    private final static Paint MEALS_FILL = Color.web("0xA44F5F");
    private final static Paint UNKNOWN_FILL = Color.DARKGRAY;

    public static WorkingDocumentCalendarExtractor get() {
        return SINGLETON;
    }

    private WorkingDocumentCalendarExtractor() {
    }

    @Override
    public Calendar extractCalendar(WorkingDocument wd, I18n i18n) {
        Collection<CalendarTimeline> timelines = new ArrayList<>();
        for (WorkingDocumentLine wdl : wd.getWorkingDocumentLines()) {
            Option o = wdl.getOption();
            String optionTimeRange = o == null ? null : o.getTimeRangeOrParent();
            if (optionTimeRange != null) {
                DaysArray daysArray = wdl.getDaysArray();
                if (!daysArray.isEmpty()) {
                    DateTimeRange dateTimeRange = new DateTimeRange(daysArray);
                    DayTimeRange dayTimeRange = DayTimeRange.parse(optionTimeRange);
                    Label label = Labels.bestLabelOrName(!o.isAccommodation() ? o : o.getParent() /* normally: night */);
                    Property<String> translation = Labels.translateLabel(label, i18n);
                    Paint fill = o.isTeaching() ? TEACHING_FILL : o.isAccommodation() ? ACCOMMODATION_FILL : o.isMeals() ? MEALS_FILL : UNKNOWN_FILL;
                    timelines.add(new CalendarTimelineImpl(dateTimeRange, dayTimeRange, translation, fill));
                }
            }
        }
        return new CalendarImpl(wd.getDateTimeRange().getInterval(), timelines);
    }
}
