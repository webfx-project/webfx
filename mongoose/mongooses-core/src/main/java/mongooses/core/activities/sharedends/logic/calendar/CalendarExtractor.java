package mongooses.core.activities.sharedends.logic.calendar;

import mongooses.core.activities.sharedends.logic.calendar.impl.document.WorkingDocumentCalendarExtractor;
import mongooses.core.activities.sharedends.logic.work.WorkingDocument;

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
