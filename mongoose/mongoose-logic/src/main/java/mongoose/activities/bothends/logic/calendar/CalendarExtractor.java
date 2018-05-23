package mongoose.activities.bothends.logic.calendar;

import mongoose.activities.bothends.logic.calendar.impl.document.WorkingDocumentCalendarExtractor;
import mongoose.activities.bothends.logic.work.WorkingDocument;

/**
 * @author Bruno Salmon
 */
public interface CalendarExtractor<T> {

    Calendar extractCalendar(T object);

    static Calendar createFromWorkingDocument(WorkingDocument wd) {
        return WorkingDocumentCalendarExtractor.get().extractCalendar(wd);
    }

    static Calendar createFromWorkingDocument(WorkingDocument wd, WorkingDocument maxWd) {
        return WorkingDocumentCalendarExtractor.get().extractCalendar(wd, maxWd);
    }
}
