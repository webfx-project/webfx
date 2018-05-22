package mongoose.activities.bothends.logic.calendar;

import mongoose.activities.bothends.logic.calendar.impl.document.WorkingDocumentCalendarExtractor;
import mongoose.activities.bothends.logic.work.WorkingDocument;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public interface CalendarExtractor<T> {

    Calendar extractCalendar(T object, I18n i18n);

    static Calendar createFromWorkingDocument(WorkingDocument wd, I18n i18n) {
        return WorkingDocumentCalendarExtractor.get().extractCalendar(wd, i18n);
    }

    static Calendar createFromWorkingDocument(WorkingDocument wd, WorkingDocument maxWd, I18n i18n) {
        return WorkingDocumentCalendarExtractor.get().extractCalendar(wd, maxWd, i18n);
    }
}
