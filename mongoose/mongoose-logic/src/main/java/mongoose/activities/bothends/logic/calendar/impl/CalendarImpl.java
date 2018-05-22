package mongoose.activities.bothends.logic.calendar.impl;

import mongoose.activities.bothends.logic.calendar.Calendar;
import mongoose.activities.bothends.logic.calendar.CalendarTimeline;
import mongoose.activities.bothends.logic.time.TimeInterval;

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
