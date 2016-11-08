package mongoose.activities.shared.logic.calendar.graphic.impl;

import mongoose.activities.shared.logic.calendar.HasEpochDay;

/**
 * @author Bruno Salmon
 */
interface HorizontalDayPositioned extends HasEpochDay {

    void setXAndWidth(double x, double width);
}
