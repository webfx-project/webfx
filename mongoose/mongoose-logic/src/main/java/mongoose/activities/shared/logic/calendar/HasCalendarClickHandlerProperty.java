package mongoose.activities.shared.logic.calendar;

import javafx.beans.property.Property;
import naga.commons.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public interface HasCalendarClickHandlerProperty {

    Property<Handler<CalendarClickEvent>> calendarClickHandlerProperty();
    default void setCalendarClickHandler(Handler<CalendarClickEvent> fcalendarClickEventHandlernt) {
        calendarClickHandlerProperty().setValue(fcalendarClickEventHandlernt);
    }
    default Handler<CalendarClickEvent> getCalendarClickHandler() {
        return calendarClickHandlerProperty().getValue();
    }

}
