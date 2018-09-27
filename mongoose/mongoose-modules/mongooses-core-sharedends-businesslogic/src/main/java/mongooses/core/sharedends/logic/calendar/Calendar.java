package mongooses.core.sharedends.logic.calendar;

import mongooses.core.shared.domainmodel.time.TimeInterval;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface Calendar {

    TimeInterval getPeriod();

    Collection<CalendarTimeline> getTimelines();

}
