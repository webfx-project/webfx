package mongoose.activities.shared.logic.calendar;

import mongoose.activities.shared.logic.calendar.impl.document.WorkingDocumentCalendarExtractor;
import mongoose.activities.shared.logic.work.WorkingDocument;

/**
 * @author Bruno Salmon
 */
public interface CalendarExtractor<T> {

    Calendar extractCalendar(T object);

    static Calendar fromWorkingDocument(WorkingDocument wd) {
        return WorkingDocumentCalendarExtractor.get().extractCalendar(wd);
    }

}
