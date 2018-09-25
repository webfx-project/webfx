package mongooses.core.sharedends.logic.calendar.impl;

import mongooses.core.sharedends.logic.calendar.Calendar;
import mongooses.core.sharedends.logic.calendar.CalendarTimeline;
import mongooses.core.shared.domainmodel.time.TimeInterval;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class CalendarImpl implements Calendar {

    private final TimeInterval period;
    private final Collection<CalendarTimeline> timelines;

    public CalendarImpl(TimeInterval period, Collection<CalendarTimeline> timelines) {
        this.period = period;
        this.timelines = timelines;
    }

    @Override
    public TimeInterval getPeriod() {
        return period;
    }

    @Override
    public Collection<CalendarTimeline> getTimelines() {
        return timelines;
    }
}
