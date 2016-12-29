package mongoose.activities.shared.logic.calendar.graphic;

import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.graphic.impl.CalendarGraphicImpl;
import naga.framework.ui.i18n.I18n;
import naga.fx.scene.Node;

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
