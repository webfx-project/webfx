package mongooses.core.activities.sharedends.logic.ui.calendargraphic;

import javafx.beans.property.Property;
import webfx.platforms.core.util.async.Handler;

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
