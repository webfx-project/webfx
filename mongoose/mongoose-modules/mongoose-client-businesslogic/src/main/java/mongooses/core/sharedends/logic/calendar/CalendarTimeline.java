package mongooses.core.sharedends.logic.calendar;

import javafx.beans.property.Property;
import mongooses.core.shared.domainmodel.time.DateTimeRange;
import mongooses.core.shared.domainmodel.time.DayTimeRange;
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
