package mongoose.activities.shared.logic.calendar.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.CalendarClickEvent;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.time.TimeInterval;
import naga.commons.util.async.Handler;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class CalendarImpl implements Calendar {

    private final TimeInterval period;
    private final Collection<CalendarTimeline> timelines;

    public CalendarImpl(Collection<CalendarTimeline> timelines) {
        this(null, timelines);
    }

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

    Property<Handler<CalendarClickEvent>> calendarClickHandlerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Handler<CalendarClickEvent>> calendarClickHandlerProperty() {
        return calendarClickHandlerProperty;
    }
}
