package mongoose.client.controls.calendargraphic;

import javafx.beans.property.Property;
import webfx.platform.shared.util.async.Handler;

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
