package mongoose.activities.shared.logic.calendar.impl.document;

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
import mongoose.entities.Option;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class WorkingDocumentCalendarExtractor implements CalendarExtractor<WorkingDocument> {

    private static WorkingDocumentCalendarExtractor SINGLETON = new WorkingDocumentCalendarExtractor();

    public static WorkingDocumentCalendarExtractor get() {
        return SINGLETON;
    }

    private WorkingDocumentCalendarExtractor() {
    }

    @Override
    public Calendar extractCalendar(WorkingDocument wd) {
        Collection<CalendarTimeline> timelines = new ArrayList<>();
        for (WorkingDocumentLine wdl : wd.getWorkingDocumentLines()) {
            Option o = wdl.getOption();
            if (o != null && o.getTimeRange() != null) {
                DaysArray daysArray = wdl.getDaysArray();
                if (!daysArray.isEmpty()) {
                    DateTimeRange dateTimeRange = new DateTimeRange(daysArray);
                    DayTimeRange dayTimeRange = DayTimeRange.parse(o.getTimeRange());
                    timelines.add(new CalendarTimelineImpl(null, dateTimeRange, dayTimeRange));
                }
            }
        }
        return new CalendarImpl(wd.getDateTimeRange().getInterval(), timelines);
    }
}
