package mongooses.core.sharedends.logic.ui.calendargraphic;

import mongooses.core.sharedends.logic.calendar.Calendar;
import mongooses.core.sharedends.logic.ui.calendargraphic.impl.CalendarGraphicImpl;
import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface CalendarGraphic extends HasCalendarClickHandlerProperty {

    Calendar getCalendar();

    Node getNode();

    void setCalendar(Calendar calendar);

    static CalendarGraphic create(Calendar calendar) {
        return new CalendarGraphicImpl(calendar);
    }
}
