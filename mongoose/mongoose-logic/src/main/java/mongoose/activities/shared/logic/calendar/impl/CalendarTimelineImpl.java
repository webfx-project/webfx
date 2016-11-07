package mongoose.activities.shared.logic.calendar.impl;

import javafx.beans.property.Property;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.DayTimeRange;
import naga.toolkit.drawing.paint.Paint;

/**
 * @author Bruno Salmon
 */
public class CalendarTimelineImpl implements CalendarTimeline {

    private final DateTimeRange dateTimeRange;
    private final DayTimeRange dayTimeRange;
    private final Property<String> displayNameProperty;
    private final Paint timelineFill;


    public CalendarTimelineImpl(DateTimeRange dateTimeRange, DayTimeRange dayTimeRange, Property<String> displayNameProperty, Paint timelineFill) {
        this.dateTimeRange = dateTimeRange;
        this.dayTimeRange = dayTimeRange;
        this.displayNameProperty = displayNameProperty;
        this.timelineFill = timelineFill;
    }

    @Override
    public DateTimeRange getDateTimeRange() {
        return dateTimeRange;
    }

    @Override
    public DayTimeRange getDayTimeRange() {
        return dayTimeRange;
    }

    @Override
    public Property<String> displayNameProperty() {
        return displayNameProperty;
    }

    @Override
    public Paint getTimelineFill() {
        return timelineFill;
    }
}
