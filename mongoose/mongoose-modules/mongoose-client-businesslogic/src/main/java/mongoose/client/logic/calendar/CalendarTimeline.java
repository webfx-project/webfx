package mongoose.client.logic.calendar;

import javafx.beans.property.Property;
import mongoose.shared.domainmodel.time.DateTimeRange;
import mongoose.shared.domainmodel.time.DayTimeRange;
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
