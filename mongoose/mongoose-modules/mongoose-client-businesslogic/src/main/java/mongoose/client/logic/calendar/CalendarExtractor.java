package mongoose.client.logic.calendar;

import mongoose.client.logic.calendar.impl.document.WorkingDocumentCalendarExtractor;
import mongoose.client.businesslogic.workingdocument.WorkingDocument;

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
