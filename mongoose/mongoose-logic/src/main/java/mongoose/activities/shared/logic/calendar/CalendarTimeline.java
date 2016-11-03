package mongoose.activities.shared.logic.calendar;

import javafx.beans.property.Property;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.DayTimeRange;

/**
 * @author Bruno Salmon
 */
public interface CalendarTimeline {

    Property<String> displayNameProperty();

    DateTimeRange getDateTimeRange();

    DayTimeRange getDayTimeRange();

}
