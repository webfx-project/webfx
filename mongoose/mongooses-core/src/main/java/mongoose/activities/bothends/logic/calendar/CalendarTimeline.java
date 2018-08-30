package mongoose.activities.bothends.logic.calendar;

import javafx.beans.property.Property;
import mongoose.activities.bothends.logic.time.DateTimeRange;
import mongoose.activities.bothends.logic.time.DayTimeRange;
import javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public interface CalendarTimeline {

    DateTimeRange getDateTimeRange();

    DayTimeRange getDayTimeRange();

    Property<String> displayNameProperty();

    Paint getBackgroundFill();

    Object getSource();

}
