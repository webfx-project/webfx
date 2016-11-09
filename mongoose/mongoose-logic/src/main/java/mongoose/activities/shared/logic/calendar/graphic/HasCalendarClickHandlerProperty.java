package mongoose.activities.shared.logic.calendar.graphic;

import javafx.beans.property.Property;
import naga.commons.util.async.Handler;

/**
 * @author Bruno Salmon
 */
interface HasCalendarClickHandlerProperty {

    Property<Handler<CalendarClickEvent>> calendarClickHandlerProperty();
    default void setCalendarClickHandler(Handler<CalendarClickEvent> calendarClickEventHandler) {
        calendarClickHandlerProperty().setValue(calendarClickEventHandler);
    }
    default Handler<CalendarClickEvent> getCalendarClickHandler() {
        return calendarClickHandlerProperty().getValue();
    }

}
