package mongoose.client.logic.calendar.impl;

import mongoose.client.logic.calendar.Calendar;
import mongoose.client.logic.calendar.CalendarTimeline;
import mongoose.shared.time.TimeInterval;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public final class CalendarImpl implements Calendar {

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
