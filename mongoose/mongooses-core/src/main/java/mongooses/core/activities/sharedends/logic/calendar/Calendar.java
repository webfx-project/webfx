package mongooses.core.activities.sharedends.logic.calendar;

import mongooses.core.activities.sharedends.logic.time.TimeInterval;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface Calendar {

    TimeInterval getPeriod();

    Collection<CalendarTimeline> getTimelines();

}
