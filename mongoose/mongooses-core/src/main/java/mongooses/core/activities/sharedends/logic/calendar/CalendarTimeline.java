package mongooses.core.activities.sharedends.logic.calendar;

import javafx.beans.property.Property;
import mongooses.core.activities.sharedends.logic.time.DateTimeRange;
import mongooses.core.activities.sharedends.logic.time.DayTimeRange;
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
