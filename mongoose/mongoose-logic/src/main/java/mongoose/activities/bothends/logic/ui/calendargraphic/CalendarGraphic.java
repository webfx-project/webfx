package mongoose.activities.bothends.logic.ui.calendargraphic;

import mongoose.activities.bothends.logic.calendar.Calendar;
import mongoose.activities.bothends.logic.ui.calendargraphic.impl.CalendarGraphicImpl;
import naga.framework.ui.i18n.I18n;
import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface CalendarGraphic extends HasCalendarClickHandlerProperty {

    Calendar getCalendar();

    Node getNode();

    void setCalendar(Calendar calendar);

    static CalendarGraphic create(Calendar calendar, I18n i18n) {
        return new CalendarGraphicImpl(calendar, i18n);
    }
}
