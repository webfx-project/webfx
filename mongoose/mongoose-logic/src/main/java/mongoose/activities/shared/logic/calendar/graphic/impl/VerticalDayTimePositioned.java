package mongoose.activities.shared.logic.calendar.graphic.impl;

import mongoose.activities.shared.logic.time.TimeInterval;

/**
 * @author Bruno Salmon
 */
interface VerticalDayTimePositioned {

    TimeInterval getDayTimeMinuteInterval();

    void setYAndHeight(double y, double height);
}
