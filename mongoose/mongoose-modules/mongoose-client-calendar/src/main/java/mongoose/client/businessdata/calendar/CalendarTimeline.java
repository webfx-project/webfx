package mongoose.client.businessdata.calendar;

import javafx.beans.property.Property;
import mongoose.shared.businessdata.time.DateTimeRange;
import mongoose.shared.businessdata.time.DayTimeRange;
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
